/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
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
 * @author Jacob Hookom [jacob@hookom.net]
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: kchung $
 */
public final class AstCompositeExpression extends SimpleNode {

    public AstCompositeExpression(int id) {
        super(id);
    }

    @Override
    public Class getType(EvaluationContext ctx) throws ELException {
        return String.class;
    }

    @Override
    public Object getValue(EvaluationContext ctx) throws ELException {
        StringBuffer sb = new StringBuffer(16);
        Object obj = null;
        if (this.children != null) {
            for (int i = 0; i < this.children.length; i++) {
                obj = this.children[i].getValue(ctx);
                if (obj != null) {
                    sb.append(obj);
                }
            }
        }
        return sb.toString();
    }
}
