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

package javax.el;

/**
 * The listener interface for receiving notification when an EL expression is evaluated.
 *
 * @since EL 3.0
 */
public abstract class EvaluationListener {

    /**
     * Receives notification before an EL expression is evaluated
     * 
     * @param context The ELContext
     * @param expression The EL expression string to be evaluated
     */
    public void beforeEvaluation(ELContext context, String expression) {
    }

    /**
     * Receives notification after an EL expression is evaluated
     * 
     * @param context The ELContext
     * @param expression The EL expression string to be evaluated
     */
    public void afterEvaluation(ELContext context, String expression) {
    }

    /**
     * Receives notification when the (base, property) pair is resolved
     * 
     * @param context The ELContext
     * @param base The base object
     * @param property The property object
     */
    public void propertyResolved(ELContext context, Object base, Object property) {
    }

}
