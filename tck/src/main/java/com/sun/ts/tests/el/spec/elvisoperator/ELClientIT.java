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
package com.sun.ts.tests.el.spec.elvisoperator;

import com.sun.ts.tests.el.common.util.ExprEval;
import com.sun.ts.tests.el.common.util.NameValuePair;

import jakarta.el.ELException;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.lang.System.Logger;

public class ELClientIT {

    private static final Logger logger = System.getLogger(ELClientIT.class.getName());

    private final boolean[] deferred = { true, false };

    @AfterEach
    public void cleanup() throws Exception {
        logger.log(Logger.Level.INFO, "Cleanup method called");
    }

    @BeforeEach
    void logStartTest(TestInfo testInfo) {
        logger.log(Logger.Level.INFO, "STARTING TEST : " + testInfo.getDisplayName());
    }

    @AfterEach
    void logFinishTest(TestInfo testInfo) {
        logger.log(Logger.Level.INFO, "FINISHED TEST : " + testInfo.getDisplayName());
    }

    @Test
    public void elEvlisNullTest() throws Exception {

        boolean pass = false;

        String[] symbols = { "$", "#" };
        String expectedResult = "default";

        try {
            for (String prefix : symbols) {
                /*
                 * Elvis operator expects a boolean first operand so null is coerced to false. First operand is false so
                 * the second operand is returned.
                 */
                String expr = prefix + "{null ?: 'default'}";
                Object result = ExprEval.evaluateValueExpression(expr, null, String.class);

                if (result == null) {
                    logger.log(Logger.Level.TRACE, "result is null");
                } else {
                    logger.log(Logger.Level.TRACE, "result is " + result.toString());
                }

                pass = (ExprEval.compareClass(result, String.class) && ExprEval.compareValue(result, expectedResult));

                if (!pass) {
                    throw new Exception("TEST FAILED: pass = false");
                }
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Test
    public void elElvisEmptyStringTest() throws Exception {
        this.testElvisOperator("", "default");
    }

    @Test
    public void elElvisNonTrueStringTest() throws Exception {
        this.testElvisOperator("other", "default");
    }

    @Test
    public void elElvisFalseStringTest() throws Exception {
        this.testElvisOperator("false", "default");
    }

    @Test
    public void elElvisTrueStringTest() throws Exception {
        String testVal = "true";
        this.testElvisOperator(testVal, testVal);
    }

    @Test
    public void elElvisLongTest() throws Exception {
        assertThrows(ELException.class, () -> {
            this.testElvisOperator(Long.valueOf(0), "ignored");
        });
    }

    // ---------------------------------------------------------- private methods

    // Test Evlis.
    private void testElvisOperator(Object testVal, Object expectedResult) throws Exception {

        boolean pass = false;

        NameValuePair value[] = NameValuePair.buildNameValuePair(testVal, "default");

        try {
            for (boolean tf : deferred) {
                String expr = ExprEval.buildElExpr(tf, "elvis");
                Object result = ExprEval.evaluateValueExpression(expr, value, Object.class);

                logger.log(Logger.Level.TRACE, "result is " + result.toString());

                pass = (ExprEval.compareClass(result, expectedResult.getClass()) && ExprEval.compareValue(result, expectedResult));

                if (!pass) {
                    throw new Exception("TEST FAILED: pass = false");
                }
            }
        } catch (ELException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
