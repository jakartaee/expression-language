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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import jakarta.el.BeanELResolver.BeanProperties;
import jakarta.el.BeanELResolver.BeanProperty;

class BeanSupportFull extends BeanSupport {

    @Override
    BeanProperties getBeanProperties(Class<?> type) {
        return new BeanPropertiesFull(type);
    }

    static final class BeanPropertiesFull extends BeanProperties {

        BeanPropertiesFull(Class<?> baseClass) throws ELException {
            super(baseClass);
            try {
                BeanInfo info = Introspector.getBeanInfo(this.baseClass);
                PropertyDescriptor[] pds = info.getPropertyDescriptors();
                for (PropertyDescriptor pd : pds) {
                    this.propertyMap.put(pd.getName(), new BeanPropertyFull(baseClass, pd));
                }
                /*
                 * Populating from any interfaces causes default methods to be included.
                 */
                populateFromInterfaces(baseClass);
            } catch (IntrospectionException ie) {
                throw new ELException(ie);
            }
        }

        private void populateFromInterfaces(Class<?> aClass) throws IntrospectionException {
            Class<?> interfaces[] = aClass.getInterfaces();
            if (interfaces.length > 0) {
                for (Class<?> ifs : interfaces) {
                    BeanInfo info = Introspector.getBeanInfo(ifs);
                    PropertyDescriptor[] pds = info.getPropertyDescriptors();
                    for (PropertyDescriptor pd : pds) {
                        if (!this.propertyMap.containsKey(pd.getName())) {
                            this.propertyMap.put(pd.getName(), new BeanPropertyFull(this.baseClass, pd));
                        }
                    }
                    populateFromInterfaces(ifs);
                }
            }
            Class<?> superclass = aClass.getSuperclass();
            if (superclass != null) {
                populateFromInterfaces(superclass);
            }
        }
    }

    static final class BeanPropertyFull extends BeanProperty {

        private final PropertyDescriptor descriptor;

        BeanPropertyFull(Class<?> owner, PropertyDescriptor descriptor) {
            super(owner, descriptor.getPropertyType());
            this.descriptor = descriptor;
        }

        @Override
        Method getWriteMethod() {
            return descriptor.getWriteMethod();
        }

        @Override
        Method getReadMethod() {
            return descriptor.getReadMethod();
        }
    }
}
