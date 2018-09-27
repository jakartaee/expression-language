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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.MethodExpression;
import javax.el.MethodInfo;

import com.sun.el.util.ReflectionUtil;

public class MethodExpressionLiteral extends MethodExpression implements Externalizable {

    private Class expectedType;

    private String expr;

    private Class[] paramTypes;

    public MethodExpressionLiteral() {
        // do nothing
    }

    public MethodExpressionLiteral(String expr, Class expectedType, Class[] paramTypes) {
        this.expr = expr;
        this.expectedType = expectedType;
        this.paramTypes = paramTypes;
    }

    @Override
    public MethodInfo getMethodInfo(ELContext context) throws ELException {
        return new MethodInfo(this.expr, this.expectedType, this.paramTypes);
    }

    @Override
    public Object invoke(ELContext context, Object[] params) throws ELException {
        if (this.expectedType == null) {
            return this.expr;
        }

        try {
            return context.convertToType(this.expr, this.expectedType);
        } catch (Exception ex) {
            throw new ELException(ex);
        }
    }

    @Override
    public String getExpressionString() {
        return this.expr;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof MethodExpressionLiteral && this.hashCode() == obj.hashCode());
    }

    @Override
    public int hashCode() {
        return this.expr.hashCode();
    }

    @Override
    public boolean isLiteralText() {
        return true;
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.expr = in.readUTF();
        String type = in.readUTF();
        if (!"".equals(type)) {
            this.expectedType = ReflectionUtil.forName(type);
        }
        this.paramTypes = ReflectionUtil.toTypeArray(((String[]) in.readObject()));
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(this.expr);
        out.writeUTF((this.expectedType != null) ? this.expectedType.getName() : "");
        out.writeObject(ReflectionUtil.toTypeNameArray(this.paramTypes));
    }
}
