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

package com.sun.el.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * @author Jacob Hookom [jacob@hookom.net]
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: kchung $
 */
public final class MessageFactory {

    protected final static ResourceBundle bundle = ResourceBundle
            .getBundle("com.sun.el.Messages");
    /**
     * 
     */
    public MessageFactory() {
        super();
    }
    
    public static String get(final String key) {
        return bundle.getString(key);
    }

    public static String get(final String key, final Object obj0) {
        return getArray(key, new Object[] { obj0 });
    }

    public static String get(final String key, final Object obj0,
            final Object obj1) {
        return getArray(key, new Object[] { obj0, obj1 });
    }

    public static String get(final String key, final Object obj0,
            final Object obj1, final Object obj2) {
        return getArray(key, new Object[] { obj0, obj1, obj2 });
    }

    public static String get(final String key, final Object obj0,
            final Object obj1, final Object obj2, final Object obj3) {
        return getArray(key, new Object[] { obj0, obj1, obj2, obj3 });
    }

    public static String get(final String key, final Object obj0,
            final Object obj1, final Object obj2, final Object obj3,
            final Object obj4) {
        return getArray(key, new Object[] { obj0, obj1, obj2, obj3, obj4 });
    }

    public static String getArray(final String key, final Object[] objA) {
        return MessageFormat.format(bundle.getString(key), objA);
    }

}
