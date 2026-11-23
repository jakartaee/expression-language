/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
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
package ee.jakarta.tck.el.spec.dotandindexoperator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import ee.jakarta.tck.el.common.util.ExprEval;
import ee.jakarta.tck.el.common.util.NameValuePair;
import jakarta.el.PropertyNotFoundException;

import java.lang.System.Logger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ELClientIT {

    private static final Logger logger = System.getLogger(ELClientIT.class.getName());

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
    public void testLiteralNullWithProperty01() throws Exception {
        doTestNullExpected("null.a");
    }


    @Test
    public void testLiteralNullWithProperty02() throws Exception {
        doTestNullExpected("null.a.b");
    }


    @Test
    public void testLiteralNullWithProperty03() throws Exception {
        doTestNullExpected("null['a']");
    }


    @Test
    public void testLiteralNullWithProperty04() throws Exception {
        doTestNullExpected("null['a']['b']");
    }


    @Test
    public void testUnresolvedIntermediateProperty() throws Exception {
        doTestPropertyNotFoundExceptionExpected("'x'.unknown.next");
    }


    @Test
    public void testUnresolvedIntermediateMethod() throws Exception {
        doTestPropertyNotFoundExceptionExpected("'x'.unknown().next");
    }


    @Test
    public void testLiteralNullWithMethod01() throws Exception {
        doTestNullExpected("null.method()");
    }


    @Test
    public void testLiteralNullWithMethod02() throws Exception {
        doTestNullExpected("null.method().a");
    }


    @Test
    public void testLiteralNullWithMethod03() throws Exception {
        doTestNullExpected("null.a.method()");
    }


    @Test
    public void testMapLookupNullWhenNullPresent() throws Exception {
        Map<String,String> testMap = new HashMap<>();
        testMap.put(null, "data");
        NameValuePair nvp = new NameValuePair("map", testMap);
        doTestNullExpected("map[null]", new NameValuePair[] { nvp });
    }


    @Test
    public void testMapLookupNullWhenNullNotPresent() throws Exception {
        Map<String,String> testMap = new HashMap<>();
        NameValuePair nvp = new NameValuePair("map", testMap);
        doTestNullExpected("map[null]", new NameValuePair[] { nvp });
    }


    @Test
    public void testMapLookupNullWhenNullNotPresentAndNotPermitted() throws Exception {
        Map<String,String> testMap = new ConcurrentHashMap<>();
        NameValuePair nvp = new NameValuePair("map", testMap);
        doTestNullExpected("map[null]", new NameValuePair[] { nvp });
    }


    private void doTestNullExpected(String expression) throws Exception {
        doTestNullExpected(expression, null);
    }


    private void doTestNullExpected(String expression, NameValuePair[] nvps) throws Exception {

        boolean pass = false;

        String[] symbols = { "$", "#" };

        try {
            for (String prefix : symbols) {
                String expr = prefix + "{" + expression + "}";
                Object result = ExprEval.evaluateValueExpression(expr, nvps, Object.class);

                if (result == null) {
                    logger.log(Logger.Level.TRACE, "result is null");
                } else {
                    logger.log(Logger.Level.TRACE, "result is " + result.toString());
                }

                pass = result == null;

                if (!pass) {
                    throw new Exception("TEST FAILED: pass = false");
                }
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }


    private void doTestPropertyNotFoundExceptionExpected(String expression) throws Exception {

        boolean pass = true;

        String[] symbols = { "$", "#" };

        try {
            for (String prefix : symbols) {
                String expr = prefix + "{" + expression + "}";
                try {
                    ExprEval.evaluateValueExpression(expr, null, Object.class);
                    pass = false;
                    logger.log(Logger.Level.TRACE, "PropertyNotFoundException not thrown");
                } catch (PropertyNotFoundException pnfe) {
                    // Expected
                } catch (Throwable t) {
                    pass = false;
                    logger.log(Logger.Level.TRACE, t.getClass().getName() + " thrown");
                }

                if (!pass) {
                    throw new Exception("TEST FAILED: pass = false");
                }
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
