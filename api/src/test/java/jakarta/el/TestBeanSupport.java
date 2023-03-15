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
import jakarta.el.BeanELResolver.BeanProperty;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class TestBeanSupport extends ELBaseTest {

    @ParameterizedTest
    @MethodSource("data")
    public void testSimpleBean(boolean useStandalone) {
        doTest(useStandalone, SimpleBean.class, "value", TypeA.class, TypeA.class, TypeA.class);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testInvalidIs01Bean(boolean useStandalone) {
        doTest(useStandalone, InvalidIs01Bean.class, "value", TypeA.class, TypeA.class, TypeA.class);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testInvalidIs02Bean(boolean useStandalone) {
        doTest(useStandalone, InvalidIs02Bean.class, "value", TypeA.class, null, TypeA.class);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testInvalidIs03Bean(boolean useStandalone) {
        doTest(useStandalone, InvalidIs03Bean.class, "value", TypeA.class, null, TypeA.class);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testReadOnlyBean(boolean useStandalone) {
        doTest(useStandalone, ReadOnlyBean.class, "value", TypeA.class, TypeA.class, null);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testWriteOnlyBean(boolean useStandalone) {
        doTest(useStandalone, WriteOnlyBean.class, "value", TypeA.class, null, TypeA.class);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testOverLoadedWithGetABean(boolean useStandalone) {
        doTest(useStandalone, OverLoadedWithGetABean.class, "value", TypeA.class, TypeA.class, TypeAAA.class);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testOverLoadedWithGetAABean(boolean useStandalone) {
        doTest(useStandalone, OverLoadedWithGetAABean.class, "value", TypeAA.class, TypeAA.class, TypeAAA.class);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testOverLoadedWithGetAAABean(boolean useStandalone) {
        doTest(useStandalone, OverLoadedWithGetAAABean.class, "value", TypeAAA.class, TypeAAA.class, TypeAAA.class);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testMismatchBean(boolean useStandalone) {
        doTest(useStandalone, MismatchBean.class, "value", TypeA.class, TypeA.class, null);
    }

    /*
     * The first setter found "wins".
     */
    @ParameterizedTest
    @MethodSource("data")
    public void testAmbiguousBean(boolean useStandalone) {
        doTest(useStandalone, AmbiguousBean.class, "value", TypeA.class, null, TypeA.class);
    }


    private void doTest(boolean useStandalone, Class<?> clazz, String propertyName, Class<?> type, Class<?> typeGet,
            Class<?> typeSet) {
        configureBeanSupport(useStandalone);
        BeanProperties beanProperties = BeanSupport.getInstance().getBeanProperties(clazz);
        BeanProperty beanProperty = beanProperties.propertyMap.get(propertyName);

        Assertions.assertNotNull(beanProperty);
        Assertions.assertEquals(type, beanProperty.getPropertyType());

        if (typeGet == null) {
            Assertions.assertNull(beanProperty.getReadMethod());
        } else {
            Assertions.assertEquals(0, beanProperty.getReadMethod().getParameterCount());
            Assertions.assertEquals(typeGet, beanProperty.getReadMethod().getReturnType());
        }

        if (typeSet == null) {
            Assertions.assertNull(beanProperty.getWriteMethod());
        } else {
            Assertions.assertEquals(void.class, beanProperty.getWriteMethod().getReturnType());
            Assertions.assertEquals(1, beanProperty.getWriteMethod().getParameterCount());
            Assertions.assertEquals(typeSet, beanProperty.getWriteMethod().getParameterTypes()[0]);
        }
    }


    public static class SimpleBean {
        public TypeA getValue() {
            return null;
        }

        public void setValue(@SuppressWarnings("unused") TypeA value) {
        }
    }


    public static class InvalidIs01Bean {
        public TypeA isValue() {
            return null;
        }

        public TypeA getValue() {
            return null;
        }

        public void setValue(@SuppressWarnings("unused") TypeA value) {
        }
    }


    public static class InvalidIs02Bean {
        public TypeA isValue() {
            return null;
        }

        public void setValue(@SuppressWarnings("unused") TypeA value) {
        }
    }


    public static class InvalidIs03Bean {
        public Boolean isValue() {
            return null;
        }

        public void setValue(@SuppressWarnings("unused") TypeA value) {
        }
    }


    public static class ReadOnlyBean {
        public TypeA getValue() {
            return null;
        }
    }


    public static class WriteOnlyBean {
        public void setValue(@SuppressWarnings("unused") TypeA value) {
        }
    }


    public static class OverLoadedWithGetABean {
        public TypeA getValue() {
            return null;
        }

        public void setValue(@SuppressWarnings("unused") TypeA value) {
        }

        public void setValue(@SuppressWarnings("unused") TypeAA value) {
        }

        public void setValue(@SuppressWarnings("unused") TypeAAA value) {
        }
    }


    public static class OverLoadedWithGetAABean {
        public TypeAA getValue() {
            return null;
        }

        public void setValue(@SuppressWarnings("unused") TypeA value) {
        }

        public void setValue(@SuppressWarnings("unused") TypeAA value) {
        }

        public void setValue(@SuppressWarnings("unused") TypeAAA value) {
        }
    }


    public static class OverLoadedWithGetAAABean {
        public TypeAAA getValue() {
            return null;
        }

        public void setValue(@SuppressWarnings("unused") TypeA value) {
        }

        public void setValue(@SuppressWarnings("unused") TypeAA value) {
        }

        public void setValue(@SuppressWarnings("unused") TypeAAA value) {
        }
    }


    public static class MismatchBean {
        public TypeA getValue() {
            return null;
        }

        public void setValue(@SuppressWarnings("unused") String value) {
        }
    }


    public static class AmbiguousBean {
        public void setValue(@SuppressWarnings("unused") TypeA value) {
        }

        public void setValue(@SuppressWarnings("unused") String value) {
        }
    }


    public static class TypeA {
    }


    public static class TypeAA extends TypeA {
    }


    public static class TypeAAA extends TypeAA {
    }
}
