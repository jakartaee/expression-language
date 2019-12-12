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
import jakarta.el.PropertyNotWritableException;
import jakarta.el.ValueExpression;

import com.sun.el.util.MessageFactory;

public final class ValueExpressionLiteral extends ValueExpression implements Externalizable {

    private static final long serialVersionUID = 1L;

    private Object value;
    private Class<?> expectedType;

    public ValueExpressionLiteral() {
        super();
    }

    public ValueExpressionLiteral(Object value, Class<?> expectedType) {
        this.value = value;
        this.expectedType = expectedType;
    }

    @Override
    public Object getValue(ELContext context) {
        if (expectedType != null) {
            try {
                return context.convertToType(value, expectedType);
            } catch (IllegalArgumentException ex) {
                throw new ELException(ex);
            }
        }

        return value;
    }

    @Override
    public void setValue(ELContext context, Object value) {
        throw new PropertyNotWritableException(MessageFactory.get("error.value.literal.write", value));
    }

    @Override
    public boolean isReadOnly(ELContext context) {
        return true;
    }

    @Override
    public Class<?> getType(ELContext context) {
        return value != null ? value.getClass() : null;
    }

    @Override
    public Class<?> getExpectedType() {
        return expectedType;
    }

    @Override
    public String getExpressionString() {
        return value != null ? value.toString() : null;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ValueExpressionLiteral && this.equals((ValueExpressionLiteral) obj);
    }

    public boolean equals(ValueExpressionLiteral ve) {
        return (ve != null && (this.value != null && ve.value != null && (this.value == ve.value || this.value.equals(ve.value))));
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public boolean isLiteralText() {
        return true;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(value);
        out.writeUTF(expectedType != null ? expectedType.getName() : "");
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        value = in.readObject();
        String type = in.readUTF();
        if (!"".equals(type)) {
            expectedType = forName(type);
        }
    }
}
