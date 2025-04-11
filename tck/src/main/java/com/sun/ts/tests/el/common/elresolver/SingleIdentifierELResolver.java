/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
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
package com.sun.ts.tests.el.common.elresolver;

import java.util.Set;

import jakarta.el.ELContext;
import jakarta.el.ELResolver;

/*
 * This ELResolver resolves the identifiers "single" and "notSingle".
 *
 * For "single", the StandaloneIdentifierMarker MUST be present and MUST be true to resolve the identifier to PASS.
 * Otherwise it resolves to FAIL.
 *
 * For "notSingle", the StandaloneIdentifierMarker MAY be present and if present MUST be false to resolve the identifier
 * to PASS. Otherwise it resolves to FAIL.
 */
public class SingleIdentifierELResolver extends ELResolver {

    public static final String SINGLE = "single";
    public static final String NOT_SINGLE = "notSingle";

    private static final Set<String> IDENTIFIERS = Set.of(SINGLE, NOT_SINGLE);

    public static final String FAIL = "NOT_OK";
    public static final String PASS = "OK";

    @Override
    public Object getValue(ELContext context, Object base, Object property) {
        if (!willResolve(context, base, property)) {
            return null;
        }

        Object marker = context.getContext(ELResolver.StandaloneIdentifierMarker.class);

        if (marker == null) {
            if (NOT_SINGLE.equals(property)) {
                return PASS;
            }
            return FAIL;
        }

        if (!(marker instanceof Boolean b)) {
            return FAIL;
        }


        if (SINGLE.equals(property)) {
            if (b.booleanValue()) {
                return PASS;
            }
            return FAIL;
        }

        if (NOT_SINGLE.equals(property)) {
            if (b.booleanValue()) {
                return FAIL;
            }
            return PASS;
        }

        // Shouldn't reach here but fail if we do
        return FAIL;
    }

    @Override
    public Class<?> getType(ELContext context, Object base, Object property) {
        if (!willResolve(context, base, property)) {
            return null;
        }
        return String.class;
    }

    @Override
    public void setValue(ELContext context, Object base, Object property, Object value) {
        //  NO-OP
    }

    @Override
    public boolean isReadOnly(ELContext context, Object base, Object property) {
        if (!willResolve(context, base, property)) {
            return false;
        }
        return true;
    }

    @Override
    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        return String.class;
    }

    private boolean willResolve(ELContext context, Object base, Object property) {
        if (base == null && IDENTIFIERS.contains(property)) {
            context.setPropertyResolved(true);
            return true;
        }
        return false;
    }
}
