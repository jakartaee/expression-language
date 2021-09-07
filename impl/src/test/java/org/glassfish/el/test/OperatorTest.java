/*
 * Copyright (c) 2018, 2021 Oracle and/or its affiliates and others.
 * All rights reserved.
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

package org.glassfish.el.test;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import jakarta.el.ELProcessor;
import jakarta.el.ELManager;
import jakarta.el.ValueExpression;

/**
 *
 * @author Kin-man
 */
public class OperatorTest {
    
    static ELProcessor elp;

    public OperatorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        elp = new ELProcessor();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    void testExpr(String testname, String expr, Object expected) {
        System.out.println("=== Test " + testname + " ===");
        System.out.println(" ** " + expr);
        Object result = elp.eval(expr);
        System.out.println("    returns " + result);
        assertEquals(expected, result);
    }

    @Test
    public void testConcat() {
        testExpr("concat", "a = null; b = null; a + b", Long.valueOf(0));
        testExpr("add", "10 + 11", Long.valueOf(21));
        testExpr("add 2", "((1)) + 1", Long.valueOf(2));
        testExpr("concat", "'10' + 11", Long.valueOf(21));
        testExpr("concat 2", "11 + '10'", Long.valueOf(21));
        testExpr("concat 3", "100 += 10 ", "10010");
        testExpr("concat 4", "'100' += 10", "10010");
        testExpr("concat 5", "'100' + 10 + 1", Long.valueOf(111));
        testExpr("concat 6", "'100' += 10 + 1", "10011");
    }
    
    @Test
    public void testAssign() {
        elp.eval("vv = 10");
        testExpr("assign", "vv+1", Long.valueOf(11));
        elp.eval("vv = 100");
        testExpr("assign 2", "vv", Long.valueOf(100));
        testExpr("assign 3", "x = vv = vv+1; x + vv", Long.valueOf(202));
        elp.eval("map = {'one':100, 'two':200}");
        testExpr("assign 4", "map.two = 201; map.two", Long.valueOf(201));
        testExpr("assign string", "x='string'; x += 1", "string1");
    }
    
    @Test
    public void testSemi() {
        testExpr("semi", "10; 20", Long.valueOf(20));
        testExpr("semi0", "10; 20; 30", Long.valueOf(30));
        elp.eval("x = 10; 20");
        testExpr("semi 2", "x", Long.valueOf(10));
        testExpr("semi 3", "(x = 10; 20) + (x ; x+1)", Long.valueOf(31));
        testExpr("semi 4", "(x = 10; y) = 11; x + y", Long.valueOf(21));
    }
    @Test
    public void testMisc() {
        testExpr("quote", "\"'\"", "'");
        testExpr("quote", "'\"'", "\"");
        ELManager elm = elp.getELManager();
        ValueExpression v = ELManager.getExpressionFactory().createValueExpression(
                elm.getELContext(), "#${1+1}", Object.class);
        Object ret = v.getValue(elm.getELContext());
        assertEquals(ret, "#2");
        
        elp.setVariable("debug", "true");
        ret = elp.eval("debug == true");
//        elp.eval("[1,2][true]"); // throws IllegalArgumentExpression
/*        
        elp.defineBean("date", new Date(2013, 1,2));
        elp.eval("date.getYear()");
        
        elp.defineBean("href", null);
        testExpr("space", "(empty href)?'#':href", "#");
        MethodExpression m = elm.getExpressionFactory().createMethodExpression(
                elm.getELContext(), "${name}", Object.class, new Class[] {});
        m.invoke(elm.getELContext(), null);
        */
    }

    @Test
    public void testEquals() {
        testExpr("string", "'xx' == 'xx'", Boolean.TRUE);
        testExpr("number", "'a'.length() == 1", Boolean.TRUE);
        testExpr("coerce '01'", "'01' == 1", Boolean.TRUE);
        testExpr("coerce '01'", "1 == '01'", Boolean.TRUE);
        testExpr("coerce '01.10'", "'01.10' == 1.10", Boolean.TRUE);
    }
}
