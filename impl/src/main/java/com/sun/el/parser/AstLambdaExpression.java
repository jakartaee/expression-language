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

import java.util.List;

import jakarta.el.ELException;
import jakarta.el.LambdaExpression;
import jakarta.el.ValueExpression;

import com.sun.el.ValueExpressionImpl;
import com.sun.el.lang.EvaluationContext;
import com.sun.el.util.MessageFactory;

/**
 * @author Kin-man Chung
 */
public class AstLambdaExpression extends SimpleNode {

    public AstLambdaExpression(int id) {
        super(id);
    }

    @Override
    public Object getValue(EvaluationContext ctx) throws ELException {
        // Create a lambda expression
        ValueExpression expr = new ValueExpressionImpl("#{Lambda Expression}", this.children[1], ctx.getFunctionMapper(), ctx.getVariableMapper(), null);
        List<String> parameters = ((AstLambdaParameters) this.children[0]).getParameters();
        LambdaExpression lambda = new LambdaExpression(parameters, expr);
        if (this.children.length <= 2) {
            return lambda;
        }

        // There are arguments following the lambda exprn, invoke it now.
        Object ret = null;
        for (int i = 2; i < this.children.length; i++) {
            if (ret != null) {
                if (!(ret instanceof LambdaExpression)) {
                    throw new ELException(MessageFactory.get("error.lambda.call"));
                }
                lambda = (LambdaExpression) ret;
            }
            AstMethodArguments args = (AstMethodArguments) this.children[i];
            ret = lambda.invoke(ctx, args.getParameters(ctx));
        }
        return ret;
    }
}
