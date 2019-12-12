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

import static com.sun.el.util.ReflectionUtil.toTypeArray;
import static com.sun.el.util.ReflectionUtil.toTypeNameArray;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import jakarta.el.ELContext;
import jakarta.el.ELException;
import jakarta.el.MethodExpression;
import jakarta.el.MethodInfo;

import com.sun.el.util.ReflectionUtil;

public class MethodExpressionLiteral extends MethodExpression implements Externalizable {

    private Class<?> expectedType;
    private String expr;
    private Class<?>[] paramTypes;

    public MethodExpressionLiteral() {
        // do nothing
    }

    public MethodExpressionLiteral(String expr, Class<?> expectedType, Class<?>[] paramTypes) {
        this.expr = expr;
        this.expectedType = expectedType;
        this.paramTypes = paramTypes;
    }

    @Override
    public MethodInfo getMethodInfo(ELContext context) throws ELException {
        return new MethodInfo(expr, expectedType, paramTypes);
    }

    @Override
    public Object invoke(ELContext context, Object[] params) throws ELException {
        if (expectedType == null) {
            return expr;
        }

        try {
            return context.convertToType(expr, expectedType);
        } catch (Exception ex) {
            throw new ELException(ex);
        }
    }

    @Override
    public String getExpressionString() {
        return expr;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MethodExpressionLiteral && this.hashCode() == obj.hashCode();
    }

    @Override
    public int hashCode() {
        return expr.hashCode();
    }

    @Override
    public boolean isLiteralText() {
        return true;
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        expr = in.readUTF();
        String type = in.readUTF();

        if (!"".equals(type)) {
            expectedType = ReflectionUtil.forName(type);
        }

        paramTypes = toTypeArray(((String[]) in.readObject()));
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(expr);
        out.writeUTF(expectedType != null ? expectedType.getName() : "");
        out.writeObject(toTypeNameArray(paramTypes));
    }
}
