/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
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
package com.sun.ts.tests.el.spec.coercion;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.sun.ts.tests.el.common.util.ExprEval;
import com.sun.ts.tests.el.common.util.NameValuePair;
import com.sun.ts.tests.el.common.util.TestNum;

public class DateTimeCoercionIT {

    private static final Map<Object,Instant> mapInputExpectedResult;

    static {
        Map<Object,Instant> map = new LinkedHashMap<>();

        map.put(null, null);
        for (Object obj : TestNum.getDateTimeList()) {
            map.put(obj, TestNum.DATE_REFERENCE);
        }

        mapInputExpectedResult = Collections.unmodifiableMap(map);
    }


    @Test
    public void testCoercetoInstant() {
        for (Map.Entry<Object,Instant> entry : mapInputExpectedResult.entrySet()) {
            NameValuePair values[] = NameValuePair.buildUnaryNameValue(entry.getKey());
            Object result = ExprEval.evaluateValueExpression("${A}", values, Instant.class);
            if (entry.getValue() == null) {
                Assertions.assertNull(result);
            } else {
                Assertions.assertTrue(ExprEval.compareClass(result, Instant.class));
                Assertions.assertEquals(result, entry.getValue());
            }
        }
    }


    @Test
    public void testCoercetoDate() {
        for (Map.Entry<Object,Instant> entry : mapInputExpectedResult.entrySet()) {
            NameValuePair values[] = NameValuePair.buildUnaryNameValue(entry.getKey());
            Object result = ExprEval.evaluateValueExpression("${A}", values, Date.class);
            if (entry.getValue() == null) {
                Assertions.assertNull(result);
            } else {
                Assertions.assertTrue(ExprEval.compareClass(result, Date.class));
                Assertions.assertEquals(result, entry.getValue() == null ? null : Date.from(entry.getValue()));
            }
        }
    }
}
