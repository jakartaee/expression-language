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
package ee.jakarta.tck.el.api.evaluationlistener;

import java.lang.System.Logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import jakarta.el.ELContext;
import jakarta.el.ELManager;
import jakarta.el.EvaluationListener;
import jakarta.el.ExpressionFactory;
import jakarta.el.ValueExpression;

public class ELClientIT {

    private static final Logger logger = System.getLogger(ELClientIT.class.getName());

    @BeforeEach
    void logStartTest(TestInfo testInfo) {
        logger.log(Logger.Level.INFO, "STARTING TEST : " + testInfo.getDisplayName());
    }

    @AfterEach
    void logFinishTest(TestInfo testInfo) {
        logger.log(Logger.Level.INFO, "FINISHED TEST : " + testInfo.getDisplayName());
    }

    @Test
    public void testValidExpression() throws Exception {
        doTestExpression("${'a' += 'b'}", "ab", true);
    }

    @Test
    public void testInvalidExpression() throws Exception {
        doTestExpression("${'a' + 'b'}", null, false);
    }

    private void doTestExpression(String expr, Object expectedResult, boolean valid) throws Exception {
        ELManager elm = new ELManager();
        ELContext elc = elm.getELContext();
        ExpressionFactory factory = ELManager.getExpressionFactory();

        TesterEvaluationListener listener = new TesterEvaluationListener();
        elc.addEvaluationListener(listener);

        ValueExpression ve = factory.createValueExpression(elc, expr, String.class);

        Object result = null;

        try {
            result = ve.getValue(elc);
        } catch (Exception e) {
            // Ignore
        }

        // Not strictly the purpose of this test but make sure the expected result is obtained
        if (valid) {
            if (result == null) {
                throw new Exception("Unexpected EL result of null");
            }
            if (!expectedResult.getClass().equals(result.getClass())) {
                throw new Exception("Unexpected EL result type of " + result.getClass());
            }
            if (!expectedResult.equals(result)) {
                throw new Exception("Unexpected EL result value of " + result);
            }
        }
        if (!listener.isBeforeEvaluationCalled()) {
            throw new Exception("EvaluationListener.beforeEvaluation() not called");
        }
        if (valid && !listener.isAfterEvaluationCalled()) {
            throw new Exception("EvaluationListener.afterEvaluation() not called after valid expression");
        }
        if (!valid && listener.isAfterEvaluationCalled()) {
            throw new Exception("EvaluationListener.afterEvaluation() called after invalid expression");
        }
    }

    private class TesterEvaluationListener extends EvaluationListener {

        private volatile boolean beforeEvaluationCalled = false;
        private volatile boolean afterEvaluationCalled = false;

        @Override
        public void beforeEvaluation(ELContext context, String expression) {
            beforeEvaluationCalled = true;
        }

        @Override
        public void afterEvaluation(ELContext context, String expression) {
            afterEvaluationCalled = true;
        }

        public boolean isBeforeEvaluationCalled() {
            return beforeEvaluationCalled;
        }

        public boolean isAfterEvaluationCalled() {
            return afterEvaluationCalled;
        }
    }
}
