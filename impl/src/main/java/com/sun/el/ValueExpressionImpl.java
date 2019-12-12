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

package com.sun.el;

import static com.sun.el.util.ReflectionUtil.forName;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import jakarta.el.ELContext;
import jakarta.el.ELException;
import jakarta.el.ELResolver;
import jakarta.el.Expression;
import jakarta.el.ExpressionFactory;
import jakarta.el.FunctionMapper;
import jakarta.el.PropertyNotFoundException;
import jakarta.el.PropertyNotWritableException;
import jakarta.el.ValueExpression;
import jakarta.el.ValueReference;
import jakarta.el.VariableMapper;

import com.sun.el.lang.EvaluationContext;
import com.sun.el.lang.ExpressionBuilder;
import com.sun.el.parser.AstLiteralExpression;
import com.sun.el.parser.Node;

/**
 * An <code>Expression</code> that can get or set a value.
 *
 * <p>
 * In previous incarnations of this API, expressions could only be read. <code>ValueExpression</code> objects can now be
 * used both to retrieve a value and to set a value. Expressions that can have a value set on them are referred to as
 * l-value expressions. Those that cannot are referred to as r-value expressions. Not all r-value expressions can be
 * used as l-value expressions (e.g. <code>"${1+1}"</code> or <code>"${firstName} ${lastName}"</code>). See the EL
 * Specification for details. Expressions that cannot be used as l-values must always return <code>true</code> from
 * <code>isReadOnly()</code>.
 * </p>
 *
 * <p>
 * The {@link ExpressionFactory#createValueExpression} method can be used to parse an expression string and return a
 * concrete instance of <code>ValueExpression</code> that encapsulates the parsed expression. The {@link FunctionMapper}
 * is used at parse time, not evaluation time, so one is not needed to evaluate an expression using this class. However,
 * the {@link ELContext} is needed at evaluation time.
 * </p>
 *
 * <p>
 * The {@link #getValue}, {@link #setValue}, {@link #isReadOnly} and {@link #getType} methods will evaluate the
 * expression each time they are called. The {@link ELResolver} in the <code>ELContext</code> is used to resolve the
 * top-level variables and to determine the behavior of the <code>.</code> and <code>[]</code> operators. For any of the
 * four methods, the {@link ELResolver#getValue} method is used to resolve all properties up to but excluding the last
 * one. This provides the <code>base</code> object. At the last resolution, the <code>ValueExpression</code> will call
 * the corresponding {@link ELResolver#getValue}, {@link ELResolver#setValue}, {@link ELResolver#isReadOnly} or
 * {@link ELResolver#getType} method, depending on which was called on the <code>ValueExpression</code>.
 * </p>
 *
 * <p>
 * See the notes about comparison, serialization and immutability in the {@link Expression} javadocs.
 *
 * @see ELResolver
 * @see Expression
 * @see ExpressionFactory
 * @see ValueExpression
 *
 * @author Jacob Hookom [jacob@hookom.net]
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: dochez $
 */
public final class ValueExpressionImpl extends ValueExpression implements Externalizable {

    private Class<?> expectedType;
    private String expr;
    private FunctionMapper fnMapper;
    private VariableMapper varMapper;
    private transient Node node;

    public ValueExpressionImpl() {

    }

    public ValueExpressionImpl(String expr, Node node, FunctionMapper fnMapper, VariableMapper varMapper, Class<?> expectedType) {
        this.expr = expr;
        this.node = node;
        this.fnMapper = fnMapper;
        this.varMapper = varMapper;
        this.expectedType = expectedType;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ValueExpressionImpl) {
            ValueExpressionImpl valueExpressionImpl = (ValueExpressionImpl) obj;
            return getNode().equals(valueExpressionImpl.getNode());
        }

        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see ValueExpression#getExpectedType()
     */
    @Override
    public Class<?> getExpectedType() {
        return expectedType;
    }

    /**
     * Returns the type the result of the expression will be coerced to after evaluation.
     *
     * @return the <code>expectedType</code> passed to the <code>ExpressionFactory.createValueExpression</code> method that
     * created this <code>ValueExpression</code>.
     *
     * @see Expression#getExpressionString()
     */
    @Override
    public String getExpressionString() {
        return expr;
    }

    /**
     * @return The Node for the expression
     * @throws ELException
     */
    private Node getNode() throws ELException {
        if (node == null) {
            node = ExpressionBuilder.createNode(expr);
        }

        return this.node;
    }

    /*
     * (non-Javadoc)
     *
     * @see ValueExpression#getType(ELContext)
     */
    @Override
    public Class<?> getType(ELContext context) throws PropertyNotFoundException, ELException {
        return getNode().getType(new EvaluationContext(context, fnMapper, varMapper));
    }

    /*
     * (non-Javadoc)
     *
     * @see ValueExpression#getValueReference(ELContext)
     */
    @Override
    public ValueReference getValueReference(ELContext context) throws PropertyNotFoundException, ELException {
        return getNode().getValueReference(new EvaluationContext(context, fnMapper, varMapper));
    }

    /*
     * (non-Javadoc)
     *
     * @see ValueExpression#getValue(ELContext)
     */
    @Override
    public Object getValue(ELContext context) throws PropertyNotFoundException, ELException {
        EvaluationContext ctx = new EvaluationContext(context, fnMapper, varMapper);
        ctx.notifyBeforeEvaluation(expr);

        Object value = getNode().getValue(ctx);

        if (expectedType != null) {
            try {
                value = ctx.convertToType(value, expectedType);
            } catch (IllegalArgumentException ex) {
                throw new ELException(ex);
            }
        }
        ctx.notifyAfterEvaluation(expr);
        return value;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return getNode().hashCode();
    }

    /*
     * (non-Javadoc)
     *
     * @see ValueExpression#isLiteralText()
     */
    @Override
    public boolean isLiteralText() {
        try {
            return getNode() instanceof AstLiteralExpression;
        } catch (ELException ele) {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ValueExpression#isReadOnly(ELContext)
     */
    @Override
    public boolean isReadOnly(ELContext context) throws PropertyNotFoundException, ELException {
        return getNode().isReadOnly(new EvaluationContext(context, fnMapper, varMapper));
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        expr = in.readUTF();
        String type = in.readUTF();
        if (!"".equals(type)) {
            expectedType = forName(type);
        }
        fnMapper = (FunctionMapper) in.readObject();
        varMapper = (VariableMapper) in.readObject();
    }

    /*
     * (non-Javadoc)
     *
     * @see ValueExpression#setValue(ELContext, java.lang.Object)
     */
    @Override
    public void setValue(ELContext context, Object value) throws PropertyNotFoundException, PropertyNotWritableException, ELException {
        getNode().setValue(new EvaluationContext(context, fnMapper, varMapper), value);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(expr);
        out.writeUTF(expectedType != null ? expectedType.getName() : "");
        out.writeObject(fnMapper);
        out.writeObject(varMapper);
    }

    @Override
    public String toString() {
        return "ValueExpression[" + expr + "]";
    }
}
