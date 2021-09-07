/*
 * Copyright (c) 2021 Contributors to the Eclipse Foundation.
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

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the behaviour of the BeanELResolver with default methods.
 */
public class DefaultMethodTest {

    public interface MyInterface {
        default public String getValueB() {
            return "defaultB";
        }
        default public String getValueC() {
            return "defaultC";
        }
    }
    
    public class MyBean implements MyInterface {
        public String getValueA() {
            return "valueA";
        }

        @Override
        public String getValueC() {
            return "valueC";
        }
        
    }
    
    @Test
    public void testGetValue() {
        
        MyBean bean = new MyBean();
        
        BeanELResolver resolver = new BeanELResolver();
        ELContext context = new StandardELContext(ELManager.getExpressionFactory());

        Object result = resolver.getValue(context, bean, "valueA");
        Assert.assertEquals(bean.getValueA(), result);
    }

    @Test
    public void testGetDefaultValue() {
        
        MyBean bean = new MyBean();
        
        BeanELResolver resolver = new BeanELResolver();
        ELContext context = new StandardELContext(ELManager.getExpressionFactory());

        Object result = resolver.getValue(context, bean, "valueB");
        Assert.assertEquals(bean.getValueB(), result);
    }

    @Test
    public void testGetDefaultOverriddenValue() {
        
        MyBean bean = new MyBean();
        
        BeanELResolver resolver = new BeanELResolver();
        ELContext context = new StandardELContext(ELManager.getExpressionFactory());

        Object result = resolver.getValue(context, bean, "valueC");
        Assert.assertEquals(bean.getValueC(), result);
    }
}
