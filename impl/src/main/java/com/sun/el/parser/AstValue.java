/*
 * Copyright (c) 1997, 2024 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.sun.el.parser;

import java.lang.reflect.Method;

import javax.el.ELClass;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.ImportHandler;
import javax.el.MethodInfo;
import javax.el.PropertyNotFoundException;
import javax.el.PropertyNotWritableException;
import javax.el.ValueReference;

import com.sun.el.lang.ELSupport;
import com.sun.el.lang.EvaluationContext;
import com.sun.el.util.MessageFactory;
import com.sun.el.util.ReflectionUtil;

/**
 * @author Jacob Hookom [jacob@hookom.net]
 * @author Kin-man Chung
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: kchung $
 */
public final class AstValue extends SimpleNode {

    protected static class Target {
        protected Object base;
        protected Node suffixNode;

        Target(Object base, Node suffixNode) {
            this.base = base;
            this.suffixNode = suffixNode;
        }

        boolean isMethodCall() {
            return getArguments(suffixNode) != null;
        }
    }

    public AstValue(int id) {
        super(id);
    }

    @Override
    public Class getType(EvaluationContext ctx) throws ELException {
        Target t = getTarget(ctx);
        if (t.isMethodCall()) {
            return null;
        }
        Object property = t.suffixNode.getValue(ctx);
        ctx.setPropertyResolved(false);
        Class ret = ctx.getELResolver().getType(ctx, t.base, property);
        if (!ctx.isPropertyResolved()) {
            ELSupport.throwUnhandled(t.base, property);
        }
        return ret;
    }

    @Override
    public ValueReference getValueReference(EvaluationContext ctx) throws ELException {
        Target t = getTarget(ctx);
        if (t.isMethodCall()) {
            return null;
        }
        Object property = t.suffixNode.getValue(ctx);
        return new ValueReference(t.base, property);
    }

    private static AstMethodArguments getArguments(Node n) {
        if (n instanceof AstDotSuffix && n.jjtGetNumChildren() > 0) {
            return (AstMethodArguments) n.jjtGetChild(0);
        }
        if (n instanceof AstBracketSuffix && n.jjtGetNumChildren() > 1) {
            return (AstMethodArguments) n.jjtGetChild(1);
        }
        return null;
    }

    private Object getValue(Object base, Node child, EvaluationContext ctx) throws ELException {

        Object value = null;
        ELResolver resolver = ctx.getELResolver();
        Object property = child.getValue(ctx);
        AstMethodArguments args = getArguments(child);
        if (args != null) {
            // This is a method call
            if (!(property instanceof String)) {
                throw new ELException(MessageFactory.get("error.method.name", property));
            }
            Class<?>[] paramTypes = args.getParamTypes();
            Object[] params = args.getParameters(ctx);

            ctx.setPropertyResolved(false);
            value = resolver.invoke(ctx, base, property, paramTypes, params);
        } else {
            if (property != null) {
                ctx.setPropertyResolved(false);
                value = resolver.getValue(ctx, base, property);
                if (!ctx.isPropertyResolved()) {
                    ELSupport.throwUnhandled(base, property);
                }
            }
        }
        return value;
    }

    private Object getBase(EvaluationContext ctx) {
        try {
            return this.children[0].getValue(ctx);
        } catch (PropertyNotFoundException ex) {
            // Next check if the base is an imported class
            if (this.children[0] instanceof AstIdentifier) {
                String name = ((AstIdentifier) this.children[0]).image;
                ImportHandler importHandler = ctx.getImportHandler();
                if (importHandler != null) {
                    Class<?> c = importHandler.resolveClass(name);
                    if (c != null) {
                        return new ELClass(c);
                    }
                }
            }
            throw ex;
        }
    }

    private Target getTarget(EvaluationContext ctx) throws ELException {
        // evaluate expr-a to value-a
        Object base = getBase(ctx);

        // if our base is null (we know there are more properites to evaluate)
        if (base == null) {
            throw new PropertyNotFoundException(MessageFactory.get("error.unreachable.base", this.children[0].getImage()));
        }

        // set up our start/end
        Object property = null;
        int propCount = this.jjtGetNumChildren() - 1;
        int i = 1;

        // evaluate any properties before our target
        if (propCount > 1) {
            while (base != null && i < propCount) {
                base = getValue(base, this.children[i], ctx);
                i++;
            }
            // if we are in this block, we have more properties to resolve,
            // but our base was null
            if (base == null) {
                throw new PropertyNotFoundException(MessageFactory.get("error.unreachable.property", property));
            }
        }
        return new Target(base, this.children[propCount]);
    }

    @Override
    public Object getValue(EvaluationContext ctx) throws ELException {
        Object base = getBase(ctx);
        int propCount = this.jjtGetNumChildren();
        int i = 1;
        while (base != null && i < propCount) {
            base = getValue(base, this.children[i], ctx);
            i++;
        }
        return base;
    }

    @Override
    public boolean isReadOnly(EvaluationContext ctx) throws ELException {
        Target t = getTarget(ctx);
        if (t.isMethodCall()) {
            return true;
        }
        Object property = t.suffixNode.getValue(ctx);
        ctx.setPropertyResolved(false);
        boolean ret = ctx.getELResolver().isReadOnly(ctx, t.base, property);
        if (!ctx.isPropertyResolved()) {
            ELSupport.throwUnhandled(t.base, property);
        }
        return ret;
    }

    @Override
    public void setValue(EvaluationContext ctx, Object value) throws ELException {
        Target t = getTarget(ctx);
        if (t.isMethodCall()) {
            throw new PropertyNotWritableException(MessageFactory.get("error.syntax.set"));
        }
        Object property = t.suffixNode.getValue(ctx);
        ELResolver elResolver = ctx.getELResolver();

        /*
         * Note by kchung 10/2013 The spec does not say if the value should be cocerced to the target type before setting the
         * value to the target. The conversion is kept here to be backward compatible.
         */
        ctx.setPropertyResolved(false);
        Class<?> targetType = elResolver.getType(ctx, t.base, property);
        if (ctx.isPropertyResolved()) {
            ctx.setPropertyResolved(false);
            Object targetValue = elResolver.convertToType(ctx, value, targetType);

            if (ctx.isPropertyResolved()) {
                value = targetValue;
            } else {
                if (value != null || (targetType != null && targetType.isPrimitive())) {
                    value = ELSupport.coerceToType(value, targetType);
                }
            }
        }

        ctx.setPropertyResolved(false);
        elResolver.setValue(ctx, t.base, property, value);
        if (!ctx.isPropertyResolved()) {
            ELSupport.throwUnhandled(t.base, property);
        }
    }

    @Override
    public MethodInfo getMethodInfo(EvaluationContext ctx, Class[] paramTypes) throws ELException {
        Target t = getTarget(ctx);
        if (t.isMethodCall()) {
            return null;
        }
        Object property = t.suffixNode.getValue(ctx);
        Method m = ReflectionUtil.findMethod(t.base.getClass(), property.toString(), paramTypes, null);
        return new MethodInfo(m.getName(), m.getReturnType(), m.getParameterTypes());
    }

    @Override
    public Object invoke(EvaluationContext ctx, Class[] paramTypes, Object[] paramValues) throws ELException {
        Target t = getTarget(ctx);
        if (t.isMethodCall()) {
            AstMethodArguments args = getArguments(t.suffixNode);
            // Always use the param types in the expression, and ignore those
            // specified elsewhere, such as TLD
            paramTypes = args.getParamTypes();
            Object[] params = args.getParameters(ctx);
            String method = (String) t.suffixNode.getValue(ctx);

            ctx.setPropertyResolved(false);
            ELResolver resolver = ctx.getELResolver();
            return resolver.invoke(ctx, t.base, method, paramTypes, params);
        }
        Object property = t.suffixNode.getValue(ctx);
        Method m = ReflectionUtil.findMethod(t.base.getClass(), property.toString(), paramTypes, paramValues);
        return ReflectionUtil.invokeMethod(ctx, m, t.base, paramValues);
    }

    @Override
    public boolean isParametersProvided() {
        return getArguments(this.children[this.jjtGetNumChildren() - 1]) != null;
    }
}
