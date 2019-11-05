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

package com.sun.el.parser;

import jakarta.el.ELException;
import jakarta.el.MethodInfo;
import jakarta.el.ValueReference;

import com.sun.el.lang.EvaluationContext;

/* All AST nodes must implement this interface.  It provides basic
   machinery for constructing the parent and child relationships
   between nodes. */

/**
 * @author Jacob Hookom [jacob@hookom.net]
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: kchung $
 */
public interface Node {

    /**
     * This method is called after the node has been made the current node. It indicates that child nodes can now be added
     * to it.
     */
    void jjtOpen();

    /**
     * This method is called after all the child nodes have been added.
     */
    void jjtClose();

    /**
     * This pair of methods are used to inform the node of its parent.
     */
    void jjtSetParent(Node n);

    Node jjtGetParent();

    /**
     * This method tells the node to add its argument to the node's list of children.
     */
    void jjtAddChild(Node n, int i);

    /**
     * This method returns a child node. The children are numbered from zero, left to right.
     */
    Node jjtGetChild(int i);

    /** Return the number of children the node has. */
    int jjtGetNumChildren();

    String getImage();

    Object getValue(EvaluationContext ctx) throws ELException;

    void setValue(EvaluationContext ctx, Object value) throws ELException;

    Class getType(EvaluationContext ctx) throws ELException;

    ValueReference getValueReference(EvaluationContext ctx) throws ELException;

    boolean isReadOnly(EvaluationContext ctx) throws ELException;

    void accept(NodeVisitor visitor) throws ELException;

    MethodInfo getMethodInfo(EvaluationContext ctx, Class[] paramTypes) throws ELException;

    Object invoke(EvaluationContext ctx, Class[] paramTypes, Object[] paramValues) throws ELException;

    @Override
    boolean equals(Object n);

    @Override
    int hashCode();

    boolean isParametersProvided();
}
