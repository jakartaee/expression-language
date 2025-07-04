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
package com.sun.ts.tests.el.spec.relationaloperator;

import java.lang.System.Logger;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.sun.ts.tests.el.common.util.ExprEval;
import com.sun.ts.tests.el.common.util.NameValuePair;
import com.sun.ts.tests.el.common.util.TestNum;

public class DateTimeOperandsIT {

    private static final Logger logger = System.getLogger(DateTimeOperandsIT.class.getName());

    private List<Object> dateTimeList = Collections.unmodifiableList(TestNum.getDateTimeList());


    @Test
    public void elDateTest() throws Exception {
        doTypeTest(x -> Date.from(x));
    }

    @Test
    public void elClockTest() throws Exception {
        doTypeTest(x -> Clock.fixed(x, ZoneId.of("Z")));
    }

    @Test
    public void elInstantTest() throws Exception {
        doTypeTest(x -> x);
    }

    @Test
    public void elTemporalTest() throws Exception {
        doTypeTest(x -> ZonedDateTime.ofInstant(x, ZoneId.of("+12:00")));
    }

    @Test
    public void elStringTest() throws Exception {
        doTypeTest(x -> x.toString());
    }

    private void doTypeTest(InstantToObject converter) {
        doEqualToTest(converter);
        doNotEqualToTest(converter);
        doLessThanTest(converter);
        doLessThanOrEqualTest(converter);
        doGreaterThanOrEqualTest(converter);
        doGreaterThanTest(converter);
    }


    private void doEqualToTest(InstantToObject converter) {
        doTest(converter, List.of("==", "eq"), Boolean.FALSE, Boolean.TRUE, Boolean.FALSE);
    }


    private void doNotEqualToTest(InstantToObject converter) {
        doTest(converter, List.of("!=", "ne"), Boolean.TRUE, Boolean.FALSE, Boolean.TRUE);
    }


    private void doLessThanTest(InstantToObject converter) {
        doTest(converter, List.of("<", "lt"), Boolean.TRUE, Boolean.FALSE, Boolean.FALSE);
    }


    private void doLessThanOrEqualTest(InstantToObject converter) {
        doTest(converter, List.of("<=", "le"), Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
    }


    private void doGreaterThanOrEqualTest(InstantToObject converter) {
        doTest(converter, List.of(">=", "ge"), Boolean.FALSE, Boolean.TRUE, Boolean.TRUE);
    }


    private void doGreaterThanTest(InstantToObject converter) {
        doTest(converter, List.of(">", "gt"), Boolean.FALSE, Boolean.FALSE, Boolean.TRUE);
    }

    private void doTest(InstantToObject converter, List<String> operators, Boolean beforeResult, Boolean equalResult,
            Boolean afterResult) {
        for (String operator : operators) {
            // Value passed in is before the reference date.
            testOperatorRelational(converter.toObject(TestNum.DATE_REFERENCE_BEFORE), beforeResult, operator);

            // Value passed in is equal to the reference date.
            testOperatorRelational(converter.toObject(TestNum.DATE_REFERENCE), equalResult, operator);

            // Value passed in is after the reference date.
            testOperatorRelational(converter.toObject(TestNum.DATE_REFERENCE_AFTER), afterResult, operator);
        }
    }


    private void testOperatorRelational(Object testValue, Boolean expectedResult, String operator) {

        for (Object testDateTime : dateTimeList) {
            NameValuePair values[] = NameValuePair.buildNameValuePair(testValue, testDateTime);

            try {
                String expr = ExprEval.buildElExpr(false, operator);
                logger.log(Logger.Level.TRACE, "expression to be evaluated is " + expr);
                logger.log(Logger.Level.TRACE, "types are " + testValue.getClass().getName() + " and " +
                        testDateTime.getClass().getName());

                Object result = ExprEval.evaluateValueExpression(expr, values, Object.class);

                Assertions.assertTrue(ExprEval.compareClass(result, Boolean.class));
                Assertions.assertEquals(result, expectedResult);
            } finally {
                ExprEval.cleanup();
            }
        }
    }


    @FunctionalInterface
    public interface InstantToObject {
        Object toObject(Instant instant);
    }
}
