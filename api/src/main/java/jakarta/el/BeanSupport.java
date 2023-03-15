/*
 * Copyright (c) 2023 Contributors to the Eclipse Foundation
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

import jakarta.el.BeanELResolver.BeanProperties;

/*
 * Provides an abstraction so the BeanELResolver can obtain JavaBeans specification support via different
 * implementations.
 */
abstract class BeanSupport {

    private static final BeanSupport beanSupport;

    static {
        // Only intended for unit tests. Not intended to be part of public API.
        boolean doNotCacheInstance = Boolean.getBoolean("jakarta.el.BeanSupport.doNotCacheInstance");
        if (doNotCacheInstance) {
            beanSupport = null;
        } else {
            beanSupport = createInstance();
        }
    }

    private static BeanSupport createInstance() {
        // Only intended for unit tests. Not intended to be part of public API.
        boolean useFull = !Boolean.getBoolean("jakarta.el.BeanSupport.useStandalone");

        if (useFull) {
            // If not explicitly configured to use standalone, use the full implementation unless it is not available.
            try {
                Class.forName("java.beans.BeanInfo");
            } catch (Exception e) {
                // Ignore: Expected if using modules and java.desktop module is not present
                useFull = false;
            }
        }
        if (useFull) {
            // The full implementation provided by the java.beans package
            return new BeanSupportFull();
        } else {
            // The cut-down local implementation that does not depend on the java.beans package
            return new BeanSupportStandalone();
        }
    }

    static BeanSupport getInstance() {
        if (beanSupport == null) {
            return createInstance();
        }
        return beanSupport;
    }

    abstract BeanProperties getBeanProperties(Class<?> type);
}
