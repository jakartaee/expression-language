/*
 * Copyright (c) 2009, 2025 Oracle and/or its affiliates and others.
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

/*
 * $Id$
 */

package com.sun.ts.tests.el.common.elresolver;

import com.sun.ts.tests.el.common.functionmapper.TCKFunctionMapper;

import jakarta.el.ELContext;
import jakarta.el.ELException;
import jakarta.el.ELResolver;

/* This ELResolver resolves only functions mapped by the TCKFunctionMapper.
 * It does not allow variables to be set.
*/

public class FunctionELResolver extends ELResolver {

  @Override
  public Object getValue(ELContext context, Object base, Object property)
      throws ELException {
    if (context == null)
      throw new NullPointerException();

    Object result = null;
    if (base == null) {
      context.setPropertyResolved(true);
      String[] function = property.toString().split(":", 2);

      // Strip off parentheses
      String localname = function[1].substring(0, function[1].length() - 3);
      result = new TCKFunctionMapper().resolveFunction(function[0], localname);
    }

    return result;
  }

  @Override
  public Class<?> getType(ELContext context, Object base, Object property)
      throws ELException {
    if (context == null)
      throw new NullPointerException();

    if (base == null)
      context.setPropertyResolved(true);

    return null;
  }

  @Override
  public void setValue(ELContext context, Object base, Object property,
      Object value) {
    if (context == null)
      throw new NullPointerException();

    if (base == null)
      context.setPropertyResolved(true);
  }

  @Override
  public boolean isReadOnly(ELContext context, Object base, Object property) {
    if (context == null)
      throw new NullPointerException();

    if (base == null)
      context.setPropertyResolved(true);
    return false;
  }

  @Override
  public Class<?> getCommonPropertyType(ELContext context, Object base) {
    if (base == null)
      return Object.class;
    return null;
  }

}
