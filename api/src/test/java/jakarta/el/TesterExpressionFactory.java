/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
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

/**
 * Minimal {@link ExpressionFactory} so API tests can create an {@link ELProcessor} without an implementation.
 */
public class TesterExpressionFactory extends ExpressionFactory {

    @Override
    public ValueExpression createValueExpression(ELContext context, String expression, Class<?> expectedType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ValueExpression createValueExpression(Object instance, Class<?> expectedType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MethodExpression createMethodExpression(ELContext context, String expression, Class<?> expectedReturnType,
            Class<?>[] expectedParamTypes) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T coerceToType(Object obj, Class<T> targetType) {
        throw new UnsupportedOperationException();
    }
}
