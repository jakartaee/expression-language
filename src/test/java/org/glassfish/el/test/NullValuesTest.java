/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://oss.oracle.com/licenses/CDDL+GPL-1.1
 * or LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package org.glassfish.el.test;

import jakarta.el.ELContext;
import jakarta.el.ELProcessor;
import jakarta.el.ExpressionFactory;
import jakarta.el.MethodExpression;

import org.junit.Assert;
import org.junit.Test;

public class NullValuesTest {

    /*
     * This method executes AstValue.invoke as can be seen in the next stack trace:
     * java.lang.Exception: Stack trace
     *     at java.lang.Thread.dumpStack(Thread.java:1365)
     *     at org.glassfish.el.test.NullValuesTest$TestBean.testCase(NullValuesTest.java)
     *     at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
     *     at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)
     *     at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
     *     at java.lang.reflect.Method.invoke(Method.java:606)
     *     at com.sun.el.util.ReflectionUtil.invokeMethod(ReflectionUtil.java:181)
     *     at com.sun.el.parser.AstValue.invoke(AstValue.java:289)
     *     at com.sun.el.MethodExpressionImpl.invoke(MethodExpressionImpl.java:304)
     */
    @Test
    public void nullString() {
        ELProcessor elp = new ELProcessor();
        elp.setValue("testBean", new TestBean());
        ExpressionFactory ef = elp.getELManager().getExpressionFactory();
        ELContext elContext = elp.getELManager().getELContext();
        MethodExpression me = ef.createMethodExpression(elContext, "#{testBean.testCase}", String.class, new Class[] {String.class});
        String output = (String) me.invoke(elContext, new Object[] {null});
        // Null string was not changed to an empty string
        Assert.assertEquals(null, output);
    }

    public static class TestBean {

        /*
         * Returns the input, so the test can expect the returned value must
         * be equals to input and it was not converted into empty string.
         */
        public String testCase(String input) {
            return input;
        }
    }

}
