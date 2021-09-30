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
import java.util.Arrays;

/**
 * Provides information about the method to which a method expression resolves.
 * 
 * Two MethodReference instances are considered equal if the reference the same
 * method on the same base object.
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


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(annotations);
        result = prime * result + ((base == null) ? 0 : base.hashCode());
        result = prime * result + Arrays.deepHashCode(evaluatedParameters);
        result = prime * result + ((methodInfo == null) ? 0 : methodInfo.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MethodReference other = (MethodReference) obj;
        if (!Arrays.equals(annotations, other.annotations)) {
            return false;
        }
        if (base == null) {
            if (other.base != null) {
                return false;
            }
        } else if (!base.equals(other.base)) {
            return false;
        }
        if (!Arrays.deepEquals(evaluatedParameters, other.evaluatedParameters)) {
            return false;
        }
        if (methodInfo == null) {
            if (other.methodInfo != null) {
                return false;
            }
        } else if (!methodInfo.equals(other.methodInfo)) {
            return false;
        }
        return true;
    }
}
