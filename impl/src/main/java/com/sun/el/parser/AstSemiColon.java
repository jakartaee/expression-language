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

import jakarta.el.ELException;

import com.sun.el.lang.EvaluationContext;

/**
 * @author Kin-man Chung
 */
public class AstSemiColon extends SimpleNode {
    public AstSemiColon(int id) {
        super(id);
    }

    @Override
    public Object getValue(EvaluationContext ctx) throws ELException {
        this.children[0].getValue(ctx);
        return this.children[1].getValue(ctx);
    }

    @Override
    public void setValue(EvaluationContext ctx, Object value) throws ELException {
        this.children[0].getValue(ctx);
        this.children[1].setValue(ctx, value);
    }
}
