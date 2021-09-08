/*
 * Copyright (c) 2019 Contributors to the Eclipse Foundation
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
package jakarta.el;

import java.lang.annotation.Annotation;

/**
 * Provides information about the method to which a method expression resolves.
 */
public class MethodReference {

    private final Object base;
    private final MethodInfo methodInfo;
    private final Annotation[] annotations;
    private final Object[] evaluatedParameters;


    public MethodReference(Object base, MethodInfo methodInfo, Annotation[] annotations, Object[] evaluatedParameters) {
        this.base = base;
        this.methodInfo = methodInfo;
        this.annotations = annotations;
        this.evaluatedParameters = evaluatedParameters;
    }


    /**
     * Obtain the base object on which the method will be invoked.
     *
     * @return The base object on which the method will be invoked or
     *         {@code null} for literal method expressions.
     */
    public Object getBase() {
        return base;
    }


    /**
     * Obtain the {@link MethodInfo} for the {@link MethodExpression} for which
     * this {@link MethodReference} has been generated.
     *
     * @return The {@link MethodInfo} for the {@link MethodExpression} for which
     *         this {@link MethodReference} has been generated.
     */
    public MethodInfo getMethodInfo() {
        return this.methodInfo;
    }


    /**
     * Obtain the annotations on the method to which the associated expression
     * resolves.
     *
     * @return The annotations on the method to which the associated expression
     *         resolves. If the are no annotations, then an empty array is
     *         returned.
     */
    public Annotation[] getAnnotations() {
        return annotations;
    }


    /**
     * Obtain the evaluated parameter values that will be passed to the method
     * to which the associated expression resolves.
     *
     *  @return The evaluated parameters.
     */
    public Object[] getEvaluatedParameters() {
        return evaluatedParameters;
    }
}
