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

import java.lang.reflect.Method;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestELProcessor {

    @BeforeAll
    public static void setup() {
        /*
         * The API has no Jakarta Expression Language implementation available when running these tests.
         *
         * Note: This system property is JVM-global so it applies to every test that runs in the same JVM after this
         * one, not just the tests in this class. That is acceptable for the limited API tests that currently exist but
         * if a test ever needs a different ExpressionFactory, the tests will need to run in a fresh JVM per test.
         */
        System.setProperty(ExpressionFactory.class.getName(), TesterExpressionFactory.class.getName());
    }

    @Test
    public void testDefineFunctionSignatureNoParameters() throws Exception {
        ELProcessor elp = new ELProcessor();
        elp.defineFunction("fn", "zero", TestELProcessor.class.getName(), "int zero()");

        Assertions.assertEquals(TestELProcessor.class.getMethod("zero"), resolve(elp, "fn", "zero"));
    }

    @Test
    public void testDefineFunctionSignatureWithSpaces() throws Exception {
        ELProcessor elp = new ELProcessor();
        elp.defineFunction("fn", "sum", TestELProcessor.class.getName(), "int sum(int, int)");

        Assertions.assertEquals(TestELProcessor.class.getMethod("sum", int.class, int.class),
                resolve(elp, "fn", "sum"));
    }

    @Test
    public void testDefineFunctionSignatureEmptyFunctionName() throws Exception {
        ELProcessor elp = new ELProcessor();
        elp.defineFunction("fn", "", TestELProcessor.class.getName(), "int sum(int,int)");

        Assertions.assertEquals(TestELProcessor.class.getMethod("sum", int.class, int.class),
                resolve(elp, "fn", "sum"));
    }

    @Test
    public void testDefineFunctionSignatureEmptyLeadingParameter() {
        ELProcessor elp = new ELProcessor();
        Assertions.assertThrows(NoSuchMethodException.class,
                () -> elp.defineFunction("fn", "sum", TestELProcessor.class.getName(), "int sum(,int)"));
    }

    @Test
    public void testDefineFunctionSignatureEmptyTrailingParameter() {
        ELProcessor elp = new ELProcessor();
        Assertions.assertThrows(NoSuchMethodException.class,
                () -> elp.defineFunction("fn", "sum", TestELProcessor.class.getName(), "int sum(int,)"));
    }

    @Test
    public void testDefineFunctionSignatureEmptyMiddleParameter() {
        ELProcessor elp = new ELProcessor();
        Assertions.assertThrows(NoSuchMethodException.class,
                () -> elp.defineFunction("fn", "sum", TestELProcessor.class.getName(), "int sum(int, ,int)"));
    }

    @Test
    public void testDefineFunctionSignatureEmptyTrailingNonsense() {
        ELProcessor elp = new ELProcessor();
        Assertions.assertThrows(NoSuchMethodException.class,
                () -> elp.defineFunction("fn", "sum", TestELProcessor.class.getName(), "int sum(int,int) nonsense"));
    }

    private static Method resolve(ELProcessor elp, String prefix, String function) {
        return elp.getELManager().getELContext().getFunctionMapper().resolveFunction(prefix, function);
    }

    public static int zero() {
        return 0;
    }

    public static int sum(int a, int b) {
        return a + b;
    }
}
