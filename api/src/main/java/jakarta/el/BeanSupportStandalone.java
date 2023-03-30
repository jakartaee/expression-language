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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.el.BeanELResolver.BeanProperties;
import jakarta.el.BeanELResolver.BeanProperty;

/*
 * Implements those parts of the JavaBeans Specification that can be implemented without reference to the java.beans
 * package.
 */
class BeanSupportStandalone extends BeanSupport {

    /*
     * The full JavaBeans implementation has a much more detailed definition of method order that applies to an entire
     * class. When ordering write methods for a single property, a much simpler comparator can be used because it is
     * known that the method names are the same, the return parameters are both void and the methods only have a single
     * parameter.
     */
    private static final Comparator<Method> WRITE_METHOD_COMPARATOR =
            Comparator.comparing(m -> m.getParameterTypes()[0].getName());

    @Override
    BeanProperties getBeanProperties(Class<?> type) {
        return new BeanPropertiesStandalone(type);
    }


    private static PropertyDescriptor[] getPropertyDescriptors(Class<?> baseClass) {
        Map<String, PropertyDescriptor> pds = new HashMap<>();
        Method[] methods = baseClass.getMethods();
        for (Method method : methods) {
            if (!Modifier.isStatic(method.getModifiers())) {
                String methodName = method.getName();
                if (methodName.startsWith("is")) {
                    if (method.getParameterCount() == 0 && method.getReturnType() == boolean.class) {
                        String propertyName = getPropertyName(methodName.substring(2));
                        PropertyDescriptor pd = pds.computeIfAbsent(propertyName, k -> new PropertyDescriptor());
                        pd.setName(propertyName);
                        pd.setReadMethodIs(method);
                    }
                } else if (methodName.startsWith("get")) {
                    if (method.getParameterCount() == 0) {
                        String propertyName = getPropertyName(methodName.substring(3));
                        PropertyDescriptor pd = pds.computeIfAbsent(propertyName, k -> new PropertyDescriptor());
                        pd.setName(propertyName);
                        pd.setReadMethod(method);
                    }
                } else if (methodName.startsWith("set")) {
                    if (method.getParameterCount() == 1 && method.getReturnType() == void.class) {
                        String propertyName = getPropertyName(methodName.substring(3));
                        PropertyDescriptor pd = pds.computeIfAbsent(propertyName, k -> new PropertyDescriptor());
                        pd.setName(propertyName);
                        pd.addWriteMethod(method);
                    }

                }
            }
        }
        return pds.values().toArray(new PropertyDescriptor[0]);
    }


    private static String getPropertyName(String input) {
        if (input.length() == 0) {
            return null;
        }
        if (!Character.isUpperCase(input.charAt(0))) {
            return null;
        }
        if (input.length() > 1 && Character.isUpperCase(input.charAt(1))) {
            return input;
        }
        char[] chars = input.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }


    private static class PropertyDescriptor {
        private String name;
        private boolean usesIs;
        private Method readMethod;
        private Method writeMethod;
        private List<Method> writeMethods = new ArrayList<>();

        String getName() {
            return name;
        }

        void setName(String name) {
            this.name = name;
        }

        Class<?> getType() {
            if (readMethod == null) {
                return getWriteMethod().getParameterTypes()[0];
            }
            return readMethod.getReturnType();
        }

        Method getReadMethod() {
            return readMethod;
        }

        void setReadMethod(Method readMethod) {
            if (usesIs) {
                return;
            }
            this.readMethod = readMethod;
        }

        void setReadMethodIs(Method readMethod) {
            this.readMethod = readMethod;
            this.usesIs = true;
        }

        Method getWriteMethod() {
            if (writeMethod == null) {
                Class<?> type;
                if (readMethod != null) {
                    type = readMethod.getReturnType();
                } else {
                    if (writeMethods.size() > 1) {
                        writeMethods.sort(WRITE_METHOD_COMPARATOR);
                    }
                    type = writeMethods.get(0).getParameterTypes()[0];
                }
                for (Method candidate : writeMethods) {
                    if (type.isAssignableFrom(candidate.getParameterTypes()[0])) {
                        type = candidate.getParameterTypes()[0];
                        this.writeMethod = candidate;
                    }
                }
            }
            return writeMethod;
        }

        void addWriteMethod(Method writeMethod) {
            this.writeMethods.add(writeMethod);
        }
    }


    static final class BeanPropertiesStandalone extends BeanProperties {

        BeanPropertiesStandalone(Class<?> baseClass) throws ELException {
            super(baseClass);
            PropertyDescriptor[] pds = getPropertyDescriptors(this.baseClass);
            for (PropertyDescriptor pd : pds) {
                this.propertyMap.put(pd.getName(), new BeanPropertyStandalone(baseClass, pd));
            }
            /*
             * Populating from any interfaces causes default methods to be included.
             */
            populateFromInterfaces(baseClass);
        }

        private void populateFromInterfaces(Class<?> aClass) {
            Class<?> interfaces[] = aClass.getInterfaces();
            if (interfaces.length > 0) {
                for (Class<?> ifs : interfaces) {
                    PropertyDescriptor[] pds = getPropertyDescriptors(baseClass);
                    for (PropertyDescriptor pd : pds) {
                        if (!this.propertyMap.containsKey(pd.getName())) {
                            this.propertyMap.put(pd.getName(), new BeanPropertyStandalone(this.baseClass, pd));
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


    static final class BeanPropertyStandalone extends BeanProperty {

        private final Method readMethod;
        private final Method writeMethod;

        BeanPropertyStandalone(Class<?> owner, PropertyDescriptor pd) {
            super(owner, pd.getType());
            readMethod = pd.getReadMethod();
            writeMethod = pd.getWriteMethod();
        }

        @Override
        Method getReadMethod() {
            return readMethod;
        }

        @Override
        Method getWriteMethod() {
            return writeMethod;
        }
    }
}
