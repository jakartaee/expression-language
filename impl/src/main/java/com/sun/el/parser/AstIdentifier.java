/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
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

import jakarta.el.ELClass;
import jakarta.el.ELException;
import jakarta.el.ELResolver;
import jakarta.el.MethodExpression;
import jakarta.el.MethodInfo;
import jakarta.el.MethodNotFoundException;
import jakarta.el.PropertyNotWritableException;
import jakarta.el.ValueExpression;
import jakarta.el.ValueReference;
import jakarta.el.VariableMapper;

import com.sun.el.lang.ELSupport;
import com.sun.el.lang.EvaluationContext;
import com.sun.el.util.MessageFactory;

/**
 * @author Jacob Hookom [jacob@hookom.net]
 * @author Kin-man Chung
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: kchung $
 */
public final class AstIdentifier extends SimpleNode {
    public AstIdentifier(int id) {
        super(id);
    }

    @Override
    public Class getType(EvaluationContext ctx) throws ELException {
        // First check if this is a lambda argument
        if (ctx.isLambdaArgument(this.image)) {
            return Object.class;
        }
        VariableMapper varMapper = ctx.getVariableMapper();
        if (varMapper != null) {
            ValueExpression expr = varMapper.resolveVariable(this.image);
            if (expr != null) {
                return expr.getType(ctx.getELContext());
            }
        }
        ctx.setPropertyResolved(false);
        Class ret = ctx.getELResolver().getType(ctx, null, this.image);
        if (!ctx.isPropertyResolved()) {
            ELSupport.throwUnhandled(null, this.image);
        }
        return ret;
    }

    @Override
    public ValueReference getValueReference(EvaluationContext ctx) throws ELException {
        VariableMapper varMapper = ctx.getVariableMapper();
        if (varMapper != null) {
            ValueExpression expr = varMapper.resolveVariable(this.image);
            if (expr != null) {
                return expr.getValueReference(ctx.getELContext());
            }
        }
        return new ValueReference(null, this.image);
    }

    @Override
    public Object getValue(EvaluationContext ctx) throws ELException {
        // First check if this is a lambda argument
        if (ctx.isLambdaArgument(this.image)) {
            return ctx.getLambdaArgument(this.image);
        }
        VariableMapper varMapper = ctx.getVariableMapper();
        if (varMapper != null) {
            ValueExpression expr = varMapper.resolveVariable(this.image);
            if (expr != null) {
                return expr.getValue(ctx.getELContext());
            }
        }
        ctx.setPropertyResolved(false);
        Object ret = ctx.getELResolver().getValue(ctx, null, this.image);
        if (!ctx.isPropertyResolved()) {
            // Check if this is an imported static field
            if (ctx.getImportHandler() != null) {
                Class<?> c = ctx.getImportHandler().resolveStatic(this.image);
                if (c != null) {
                    return ctx.getELResolver().getValue(ctx, new ELClass(c), this.image);
                }
            }
            ELSupport.throwUnhandled(null, this.image);
        }
        return ret;
    }

    @Override
    public boolean isReadOnly(EvaluationContext ctx) throws ELException {
        // Lambda arguments are read only.
        if (ctx.isLambdaArgument(this.image)) {
            return true;
        }
        VariableMapper varMapper = ctx.getVariableMapper();
        if (varMapper != null) {
            ValueExpression expr = varMapper.resolveVariable(this.image);
            if (expr != null) {
                return expr.isReadOnly(ctx.getELContext());
            }
        }
        ctx.setPropertyResolved(false);
        boolean ret = ctx.getELResolver().isReadOnly(ctx, null, this.image);
        if (!ctx.isPropertyResolved()) {
            ELSupport.throwUnhandled(null, this.image);
        }
        return ret;
    }

    @Override
    public void setValue(EvaluationContext ctx, Object value) throws ELException {
        // First check if this is a lambda argument
        if (ctx.isLambdaArgument(this.image)) {
            throw new PropertyNotWritableException(MessageFactory.get("error.lambda.parameter.readonly", this.image));
        }
        VariableMapper varMapper = ctx.getVariableMapper();
        if (varMapper != null) {
            ValueExpression expr = varMapper.resolveVariable(this.image);
            if (expr != null) {
                expr.setValue(ctx.getELContext(), value);
                return;
            }
        }
        ctx.setPropertyResolved(false);
        ELResolver elResolver = ctx.getELResolver();
        elResolver.setValue(ctx, null, this.image, value);
        if (!ctx.isPropertyResolved()) {
            ELSupport.throwUnhandled(null, this.image);
        }
    }

    @Override
    public Object invoke(EvaluationContext ctx, Class[] paramTypes, Object[] paramValues) throws ELException {
        return this.getMethodExpression(ctx).invoke(ctx.getELContext(), paramValues);
    }

    @Override
    public MethodInfo getMethodInfo(EvaluationContext ctx, Class[] paramTypes) throws ELException {
        return this.getMethodExpression(ctx).getMethodInfo(ctx.getELContext());
    }

    private MethodExpression getMethodExpression(EvaluationContext ctx) throws ELException {
        Object obj = null;

        // case A: ValueExpression exists, getValue which must
        // be a MethodExpression
        VariableMapper varMapper = ctx.getVariableMapper();
        ValueExpression ve = null;
        if (varMapper != null) {
            ve = varMapper.resolveVariable(this.image);
            if (ve != null) {
                obj = ve.getValue(ctx);
            }
        }

        // case B: evaluate the identity against the ELResolver, again, must be
        // a MethodExpression to be able to invoke
        if (ve == null) {
            ctx.setPropertyResolved(false);
            obj = ctx.getELResolver().getValue(ctx, null, this.image);
        }

        // finally provide helpful hints
        if (obj instanceof MethodExpression) {
            return (MethodExpression) obj;
        } else if (obj == null) {
            throw new MethodNotFoundException("Identity '" + this.image + "' was null and was unable to invoke");
        } else {
            throw new ELException("Identity '" + this.image + "' does not reference a MethodExpression instance, returned type: " + obj.getClass().getName());
        }
    }
}
