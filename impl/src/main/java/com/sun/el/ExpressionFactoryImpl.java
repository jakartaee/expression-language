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

package com.sun.el;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;

import com.sun.el.lang.ELSupport;
import com.sun.el.lang.ExpressionBuilder;
import com.sun.el.stream.StreamELResolver;
import com.sun.el.util.MessageFactory;

/**
 * @see javax.el.ExpressionFactory
 *
 * @author Jacob Hookom [jacob@hookom.net]
 * @author Kin-man Chung
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: kchung $
 */
public class ExpressionFactoryImpl extends ExpressionFactory {

    /**
     *
     */
    public ExpressionFactoryImpl() {
        super();
    }

    public ExpressionFactoryImpl(Properties properties) {
        super();
        this.properties = properties;
        this.isBackwardCompatible22 = "true".equals(getProperty("javax.el.bc2.2"));
    }

    @Override
    public Object coerceToType(Object obj, Class type) {
        Object ret;
        try {
            ret = ELSupport.coerceToType(obj, type, isBackwardCompatible22);
        } catch (IllegalArgumentException ex) {
            throw new ELException(ex);
        }
        return ret;
    }

    @Override
    public MethodExpression createMethodExpression(ELContext context, String expression, Class expectedReturnType, Class[] expectedParamTypes) {
        ExpressionBuilder builder = new ExpressionBuilder(expression, context);
        MethodExpression me = builder.createMethodExpression(expectedReturnType, expectedParamTypes);
        if (expectedParamTypes == null && !me.isParametersProvided()) {
            throw new NullPointerException(MessageFactory.get("error.method.nullParms"));
        }
        return me;
    }

    @Override
    public ValueExpression createValueExpression(ELContext context, String expression, Class expectedType) {
        if (expectedType == null) {
            throw new NullPointerException(MessageFactory.get("error.value.expectedType"));
        }
        ExpressionBuilder builder = new ExpressionBuilder(expression, context);
        return builder.createValueExpression(expectedType);
    }

    @Override
    public ValueExpression createValueExpression(Object instance, Class expectedType) {
        if (expectedType == null) {
            throw new NullPointerException(MessageFactory.get("error.value.expectedType"));
        }
        return new ValueExpressionLiteral(instance, expectedType);
    }

    public String getProperty(String key) {
        if (properties == null) {
            return null;
        }
        return properties.getProperty(key);
    }

    @Override
    public ELResolver getStreamELResolver() {
        return new StreamELResolver();
    }

    @Override
    public Map<String, Method> getInitFunctionMap() {
        Map<String, Method> funcs = new HashMap<String, Method>();
        return funcs;
    }

    private Properties properties;
    private boolean isBackwardCompatible22;
}
