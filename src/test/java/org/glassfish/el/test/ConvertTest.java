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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.el.*;

/**
 *
 * @author kichung
 */
public class ConvertTest {
    ELProcessor elp;

    public ConvertTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        elp = new ELProcessor();
    }

    @After
    public void tearDown() {
    }

    static public class MyBean {
        String name;
        int pint;
        Integer integer;

        MyBean() {

        }
        MyBean(String name) {
            this.name = name;
        }
        public String getName() {
            return this.name;
        }
        public void setPint(int v) {
            this.pint = v;
        }
        public int getPint() {
            return this.pint;
        }

        public void setInteger(Integer i){
            this.integer = i;
        }

        public Integer getInteger() {
            return this.integer;
        }
    }
    @Test
    public void testVoid() {
        MyBean bean = new MyBean();
        elp.defineBean("bean", bean);
        // Assig null to int is 0;
        Object obj = elp.eval("bean.pint = null");
        assertEquals(obj, null);
        assertEquals(bean.getPint(), 0);

        // Assig null to Integer is null
        elp.setValue("bean.integer", null);
        assertEquals(bean.getInteger(), null);
    }

    @Test
    public void testCustom() {
        elp.getELManager().addELResolver(new TypeConverter() {
            @Override
            public Object convertToType(ELContext context, Object obj, Class<?> type) {
                if (obj instanceof String && type == MyBean.class) {
                    context.setPropertyResolved(true);
                    return new MyBean((String) obj);
                }
                return null;
            }
        });
        
        Object val = elp.getValue("'John Doe'", MyBean.class);
        assertTrue(val instanceof MyBean);
        assertEquals(((MyBean)val).getName(), "John Doe");
    }
}
