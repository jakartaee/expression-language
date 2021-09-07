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

import jakarta.el.ELProcessor;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kichung
 */
public class StaticRefTest {

    ELProcessor elp;

    public StaticRefTest() {
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

    @Test
    public void testStaticRef() {
        // Pre imported java.lang classes
        Boolean ret = elp.eval("Boolean.TRUE");
        assertTrue(ret.booleanValue());
        ret = elp.eval("Boolean.TRUE");
        assertTrue(ret.booleanValue());  // test caching Boolean
    }

/*
    @Test
    public void testClass() {
        assertEquals(String.class, elp.eval("String.class"));
    }
*/

    @Test
    public void testConstructor() {
        assertEquals(Integer.valueOf(1001), elp.eval("Integer(1001)"));
    }

    @Test
    public void testStaticMethod() {
        assertEquals(Integer.valueOf(4), elp.eval("Integer.numberOfTrailingZeros(16)"));
    }
}
