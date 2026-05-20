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
package jakarta.el;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestELContext {

    private static final String NAME = "x";
    private static final String VALUE_NON_NULL = "x";
    private static final String VALUE_NULL = null;


    @Test
    public void testLambdaScopeWithNullParameter() {
        ELContext elContext = new SimpleELContextImpl();

        // Set up parameters for outer scope
        Map<String,Object> outerParams = new HashMap<>();
        outerParams.put(NAME, VALUE_NON_NULL);

        // Set up parameters for innerscope
        Map<String,Object> innerParams = new HashMap<>();
        innerParams.put(NAME, VALUE_NULL);

        // Enter outer scope
        elContext.enterLambdaScope(outerParams);

        // Should see outer, non-null value
        Assertions.assertEquals(VALUE_NON_NULL, elContext.getLambdaArgument(NAME));

        // Enter inner scope
        elContext.enterLambdaScope(innerParams);

        // Should see inner, null value
        Assertions.assertNull(elContext.getLambdaArgument(NAME));
    }


    private static class SimpleELContextImpl extends ELContext {

        @Override
        public ELResolver getELResolver() {
            return null;
        }

        @Override
        public FunctionMapper getFunctionMapper() {
            return null;
        }

        @Override
        public VariableMapper getVariableMapper() {
            return null;
        }
    }
}
