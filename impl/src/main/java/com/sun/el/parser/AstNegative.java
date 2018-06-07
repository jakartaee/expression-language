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

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.el.ELException;

import com.sun.el.lang.EvaluationContext;

/**
 * @author Jacob Hookom [jacob@hookom.net]
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: kchung $
 */
public final class AstNegative extends SimpleNode {
    public AstNegative(int id) {
        super(id);
    }

    public Class getType(EvaluationContext ctx)
            throws ELException {
        return Number.class;
    }

    public Object getValue(EvaluationContext ctx)
            throws ELException {
        Object obj = this.children[0].getValue(ctx);

        if (obj == null) {
            return Long.valueOf(0);
        }
        if (obj instanceof BigDecimal) {
            return ((BigDecimal) obj).negate();
        }
        if (obj instanceof BigInteger) {
            return ((BigInteger) obj).negate();
        }
        if (obj instanceof String) {
            if (isStringFloat((String) obj)) {
                return Double.valueOf(-Double.parseDouble((String) obj));
            }
            return Long.valueOf(-Long.parseLong((String) obj));
        }
        Class type = obj.getClass();
        if (obj instanceof Long || Long.TYPE == type) {
            return Long.valueOf(-((Long) obj).longValue());
        }
        if (obj instanceof Double || Double.TYPE == type) {
            return Double.valueOf(-((Double) obj).doubleValue());
        }
        if (obj instanceof Integer || Integer.TYPE == type) {
            return Integer.valueOf(-((Integer) obj).intValue());
        }
        if (obj instanceof Float || Float.TYPE == type) {
            return Float.valueOf(-((Float) obj).floatValue());
        }
        if (obj instanceof Short || Short.TYPE == type) {
            return Short.valueOf((short) -((Short) obj).shortValue());
        }
        if (obj instanceof Byte || Byte.TYPE == type) {
            return Byte.valueOf((byte) -((Byte) obj).byteValue());
        }
        Long num = (Long) coerceToNumber(obj, Long.class);
        return Long.valueOf(-num.longValue());
    }
}
