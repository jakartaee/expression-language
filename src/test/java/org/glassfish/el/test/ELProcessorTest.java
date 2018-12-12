/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
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

import org.junit.Test;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.el.ELProcessor;
import javax.el.ELManager;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ELContext;
import java.lang.reflect.Method;

public class ELProcessorTest {

    static ELProcessor elp;
    static ELManager elm;
    static ExpressionFactory factory;
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        elp = new ELProcessor();
        elm = elp.getELManager();
        factory = elm.getExpressionFactory();
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void testMethExpr() {
        MethodExpression meth = null;
        ELContext ctxt = elm.getELContext();
        try {
            meth = factory.createMethodExpression(
                ctxt, "#{str.length}", Object.class, null);
        } catch (NullPointerException ex){
            // Do nothing
        }
        assertTrue(meth == null);
        meth = factory.createMethodExpression(
                ctxt, "#{'abc'.length()}", Object.class, null);
        Object result = meth.invoke(ctxt, new Object[] {"abcde"});
        System.out.println("'abc'.length() called, equals " + result);
        assertEquals(result, new Integer(3));
    }

    @Test
    public void testGetValue() {
        Object result = elp.eval("10 + 1");
        assertEquals(result.toString(), "11");
        result = elp.getValue("10 + 2", String.class);
        assertEquals(result, "12");
    }

    @Test
    public void testSetVariable () {
        elp.setVariable("xx", "100");
        Object result = elp.getValue("xx + 11", String.class);
        assertEquals(result, "111");
        elp.setVariable("xx", null);
        assertEquals(elp.eval("xx"), null);
        elp.setVariable("yy", "abc");
        assertEquals(elp.eval("yy = 123; abc"), 123L);
        assertEquals(elp.eval("abc = 456; yy"), 456L);
    }

    @Test
    public void testConcat() {
        Object result = elp.eval("'10' + 1");
        assertEquals(result, 11L);
        result = elp.eval("10 += '1'");
        assertEquals(result.toString(), "101");
    }
    
    @Test
    public void testParenthesis() {
        elp.setVariable("xx", "1");
        Object result = elp.eval("((xx)) + 1");
        assertEquals(result, 2L);
    }
    
    @Test
    public void defineFuncTest() {
        Class c = MyBean.class;
        Method meth = null;
        Method meth2 = null;
        try {
            meth = c.getMethod("getBar", new Class<?>[] {});
            meth2 = c.getMethod("getFoo", new Class<?>[] {});
        } catch (Exception e) {
            System.out.printf("Exception: ", e);
        }
        try {
            elp.defineFunction("xx", "", meth);
            Object ret = elp.eval("xx:getBar() == 64");
            assertTrue((Boolean)ret);
        } catch (NoSuchMethodException ex) {
            
        }
        
        boolean caught = false;
        try {
            elp.defineFunction("", "", meth2);
            Object ret = elp.eval("getFoo() == 100");
            assertTrue((Boolean)ret);
        } catch (NoSuchMethodException ex) {
            caught = true;
        }
        assertTrue(caught);
        
        try {
            elp.defineFunction("yy", "", "org.glassfish.el.test.ELProcessorTest$MyBean", "getBar");
            Object ret = elp.eval("yy:getBar() == 64");
            assertTrue((Boolean)ret);
        } catch (ClassNotFoundException | NoSuchMethodException ex) {
            
        }
        
        caught = false;
        try {
            elp.defineFunction("yy", "", "org.glassfish.el.test.ELProcessorTest$MyBean", "getFooBar");
            Object ret = elp.eval("yy:getBar() == 100");
            assertTrue((Boolean)ret);
        } catch (ClassNotFoundException | NoSuchMethodException ex) {
            caught = true;
        }
        assertTrue(caught);
        
        caught = false;
        try {
            elp.defineFunction("yy", "", "testBean", "getFoo");
            Object ret = elp.eval("yy:getBar() == 100");
            assertTrue((Boolean)ret);
        } catch (ClassNotFoundException | NoSuchMethodException ex) {
            caught = true;
        }
        assertTrue(caught);
    }
/*
    @Test
    public void testBean() {
        elp.defineBean("xyz", new MyBean());
        Object result = elp.eval("xyz.foo");
        assertEquals(result.toString(), "100");
    }
*/

    @Test
    public void testImport() {
        elm.importClass("org.glassfish.el.test.ELProcessorTest$MyBean");
        assertTrue((Boolean)elp.eval("ELProcessorTest$MyBean.aaaa == 101"));
        assertTrue((Boolean)elp.eval("ELProcessorTest$MyBean.getBar() == 64"));
        elm.importStatic("org.glassfish.el.test.ELProcessorTest$MyBean.aaaa");
        assertEquals(new Integer(101), elp.eval("aaaa"));
        elm.importStatic("org.glassfish.el.test.ELProcessorTest$MyBean.getBar");
        assertEquals(new Integer(64), elp.eval("getBar()"));
 /*
        elm.importStatic("a.b.NonExisting.foobar");
        elp.eval("foobar");
        elp.eval("ELProcessorTest$MyBean.getFoo()");
        */
    }

    static public class MyBean {
        public static int aaaa = 101;
        public int getFoo() {
            return 100;
        }
        public int getFoo(int i) {
            return 200;
        }
        public static int getBar() {
            return 64;
        }
    }
}

