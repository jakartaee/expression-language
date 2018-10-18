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

package com.sun.el.stream;

import javax.el.LambdaExpression;

public class Optional {

    private final static Optional EMPTY = new Optional();
    private final Object value;

    Optional(Object value) {
        if (value == null) {
            throw new NullPointerException();
        }
        this.value = value;
    }

    Optional() {
        this.value = null;
    }

    public boolean isPresent() {
        return value != null;
    }

    public void ifPresent(LambdaExpression lambda) {
        if (value != null) {
            lambda.invoke(value);
        }
    }

    public Object get() {
        if (value == null) {
            throw new java.util.NoSuchElementException("No value present");
        }
        return value;
    }

    public Object orElse(Object other) {
        return value != null ? value : other;
    }

    public Object orElseGet(LambdaExpression other) {
        return value != null ? value : other.invoke();
    }
}
