/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.el.parser;

import javax.el.ELException;

import com.sun.el.lang.EvaluationContext;

/**
 * @author Kin-man Chung
 */
public class AstMethodArguments extends SimpleNode {
    public AstMethodArguments(int id) {
        super(id);
    }

    Class<?>[] getParamTypes() {
        return null;
    }

    public Object[] getParameters(EvaluationContext ctx) throws ELException {

        if (this.children == null) {
            return new Object[] {};
        }

        Object[] obj = new Object[this.children.length];
        for (int i = 0; i < obj.length; i++) {
            obj[i] = this.children[i].getValue(ctx);
        }
        return obj;
    }

    public int getParameterCount() {
        return this.children == null ? 0 : this.children.length;
    }

    @Override
    public boolean isParametersProvided() {
        return true;
    }

}
