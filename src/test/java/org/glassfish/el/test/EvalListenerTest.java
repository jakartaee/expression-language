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

import java.util.ArrayList;
import javax.el.ELManager;
import javax.el.ELContext;
import javax.el.ELProcessor;
import javax.el.EvaluationListener;
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
public class EvalListenerTest {

    public EvalListenerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testEvalListener() {
        ELProcessor elp = new ELProcessor();
        ELManager elm = elp.getELManager();
        final ArrayList<String> msgs = new ArrayList<String>();
        elm.addEvaluationListener(new EvaluationListener() {
            @Override
            public void beforeEvaluation(ELContext ctxt, String expr) {
                System.out.println("Before: " + expr);
                msgs.add("Before: " + expr);
            }
            @Override
            public void afterEvaluation(ELContext ctxt, String expr) {
                System.out.println("After: " + expr);
                msgs.add("After: " + expr);
            }
        });
        elp.eval("100 + 10");
        elp.eval("x = 5; x*101");
        String[] expected = {"Before: ${100 + 10}",
            "After: ${100 + 10}",
            "Before: ${x = 5; x*101}",
            "After: ${x = 5; x*101}" };
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], msgs.get(i));
        }
    }

    @Test
    public void testResListener() {
        ELProcessor elp = new ELProcessor();
        ELManager elm = elp.getELManager();
        final ArrayList<String> msgs = new ArrayList<String>();
        elm.addEvaluationListener(new EvaluationListener() {
            @Override
            public void propertyResolved(ELContext ctxt, Object b, Object p) {
                System.out.println("Resolved: " + b + " " + p);
                msgs.add("Resolved: " + b + " " + p);
            }
        });
        elp.eval("x = 10");
        elp.eval("[1,2,3][2]");
        elp.eval("'abcd'.length()");
        elp.eval("'xyz'.class");
        String[] expected = {
            "Resolved: null x",
            "Resolved: [1, 2, 3] 2",
            "Resolved: abcd length",
            "Resolved: xyz class"
        };
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], msgs.get(i));
        }
    }
}
