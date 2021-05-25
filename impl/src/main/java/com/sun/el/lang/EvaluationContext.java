/*
 * Copyright (c) 1997, 2021 Oracle and/or its affiliates and others.
 * All rights reserved.
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

package com.sun.el.lang;

import java.util.List;
import java.util.Map;

import jakarta.el.ELContext;
import jakarta.el.ELResolver;
import jakarta.el.EvaluationListener;
import jakarta.el.FunctionMapper;
import jakarta.el.ImportHandler;
import jakarta.el.VariableMapper;

/**
 * The context for Jakarta Expression expression evaluation. This wrapper ELContext captures the function mapper and the
 * variable mapper at the point when the expression is parsed, and only for those functions and variable used in the
 * expression.
 */
public final class EvaluationContext extends ELContext {

    private final ELContext elContext;
    private final FunctionMapper fnMapper;
    private final VariableMapper varMapper;

    public EvaluationContext(ELContext elContext, FunctionMapper fnMapper, VariableMapper varMapper) {
        this.elContext = elContext;
        this.fnMapper = fnMapper;
        this.varMapper = varMapper;
    }

    public ELContext getELContext() {
        return elContext;
    }

    @Override
    public FunctionMapper getFunctionMapper() {
        return fnMapper;
    }

    @Override
    public VariableMapper getVariableMapper() {
        return varMapper;
    }

    @Override
    public Object getContext(Class key) {
        return elContext.getContext(key);
    }

    @Override
    public ELResolver getELResolver() {
        return elContext.getELResolver();
    }

    @Override
    public boolean isPropertyResolved() {
        return elContext.isPropertyResolved();
    }

    @Override
    public void putContext(Class key, Object contextObject) {
        elContext.putContext(key, contextObject);
    }

    @Override
    public void setPropertyResolved(boolean resolved) {
        elContext.setPropertyResolved(resolved);
    }

    @Override
    public void setPropertyResolved(Object base, Object property) {
        elContext.setPropertyResolved(base, property);
    }

    @Override
    public void addEvaluationListener(EvaluationListener listener) {
        elContext.addEvaluationListener(listener);
    }

    @Override
    public List<EvaluationListener> getEvaluationListeners() {
        return elContext.getEvaluationListeners();
    }

    @Override
    public void notifyBeforeEvaluation(String expr) {
        elContext.notifyBeforeEvaluation(expr);
    }

    @Override
    public void notifyAfterEvaluation(String expr) {
        elContext.notifyAfterEvaluation(expr);
    }

    @Override
    public void notifyPropertyResolved(Object base, Object property) {
        elContext.notifyPropertyResolved(base, property);
    }

    @Override
    public boolean isLambdaArgument(String arg) {
        return elContext.isLambdaArgument(arg);
    }

    @Override
    public Object getLambdaArgument(String arg) {
        return elContext.getLambdaArgument(arg);
    }

    @Override
    public void enterLambdaScope(Map<String, Object> args) {
        elContext.enterLambdaScope(args);
    }

    @Override
    public void exitLambdaScope() {
        elContext.exitLambdaScope();
    }

    @Override
    public <T> T convertToType(Object obj, Class<T> targetType) {
        return elContext.convertToType(obj, targetType);
    }

    @Override
    public ImportHandler getImportHandler() {
        return elContext.getImportHandler();
    }
}
