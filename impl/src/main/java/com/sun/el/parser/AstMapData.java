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

import java.util.HashMap;
import java.util.HashSet;

import javax.el.ELException;

import com.sun.el.lang.EvaluationContext;

/**
 * @author Kin-man Chung
 */
public class AstMapData extends SimpleNode {
    public AstMapData(int id) {
        super(id);
    }

    @Override
    public Object getValue(EvaluationContext ctx) {
        HashSet<Object> set = new HashSet<Object>();
        HashMap<Object, Object> map = new HashMap<Object, Object>();

        int paramCount = this.jjtGetNumChildren();
        for (int i = 0; i < paramCount; i++) {
            Node entry = this.children[i];
            Object v1 = entry.jjtGetChild(0).getValue(ctx);
            if (entry.jjtGetNumChildren() > 1) {
                // expr: expr
                map.put(v1, entry.jjtGetChild(1).getValue(ctx));
            } else {
                set.add(v1);
            }
        }
        // It is error to have mixed set/map entries
        if (set.size() > 0 && map.size() > 0) {
            throw new ELException("Cannot mix set entry with map entry.");
        }
        if (map.size() > 0) {
            return map;
        }
        return set;
    }
}
