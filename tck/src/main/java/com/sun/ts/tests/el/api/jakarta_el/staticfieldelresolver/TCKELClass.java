/*
 * Copyright (c) 2012, 2025 Oracle and/or its affiliates and others.
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

package com.sun.ts.tests.el.api.jakarta_el.staticfieldelresolver;

public class TCKELClass {
  public static String firstName = "Ender";

  public static String lastName = "Wiggins";

  // Not writable from StaticFieldELResolver
  public String notStatic = "notStatic";

  private static String privStatic = "privStatic";

  /**
   * @param name
   *          - Name to test against firstName
   *
   * @return true if firstName matches the provided name
   */
  public static boolean isName(String name) {
    boolean result = false;

    if (name.equals(firstName)) {
      result = true;
    }

    return result;
  }
}
