/*
 * Copyright (c) 2021 Oracle and/or its affiliates. All rights reserved.
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

import jakarta.el.ELManager;
import jakarta.el.ELProcessor;
import jakarta.el.ValueExpression;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EscapingTest {

    static ELProcessor elp;
    static ELManager elm;

    public EscapingTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        elp = new ELProcessor();
        elm = elp.getELManager();
    }

    @Test
    public void testEscape01() {
        assertEquals("$2", evaluateExpression("$${1+1}"));
        assertEquals("$${1+1}", evaluateExpression("$\\${1+1}"));
    }

    @Test
    public void testEscape02() {
        assertEquals("$2", evaluateExpression("$#{1+1}"));
        assertEquals("$#{1+1}", evaluateExpression("$\\#{1+1}"));
    }

    @Test
    public void testEscape03() {
        assertEquals("#2", evaluateExpression("##{1+1}"));
        assertEquals("##{1+1}", evaluateExpression("#\\#{1+1}"));
    }

    @Test
    public void testEscape04() {
        assertEquals("#2", evaluateExpression("#${1+1}"));
        assertEquals("#${1+1}", evaluateExpression("#\\${1+1}"));
    }

    private String evaluateExpression(String expr) {
        ValueExpression v = ELManager.getExpressionFactory().createValueExpression(
                elm.getELContext(), expr, String.class);
        return (String) v.getValue(elm.getELContext());
    }
}
