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

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Base class for tests that (indirectly) use BeanSupport and want to test both implementations.
 */
public abstract class ELBaseTest {

    public static Stream<String> data() {
        return Stream.of("true", "false");
    }

    @BeforeAll
    public static void setup() {
        // Disable caching so we can switch implementations within a JVM instance.
        System.setProperty("jakarta.el.BeanSupport.doNotCacheInstance", "true");
    }

    /*
     * Double check test has been configured as expected
     */
    @ParameterizedTest
    @MethodSource("data")
    public void testImplementation(boolean useStandalone) {
        configureBeanSupport(useStandalone);
        if (useStandalone) {
            Assertions.assertEquals(BeanSupportStandalone.class, BeanSupport.getInstance().getClass());
        } else {
            Assertions.assertEquals(BeanSupportFull.class, BeanSupport.getInstance().getClass());
        }
    }
     
    
    protected final void configureBeanSupport(boolean useStandalone) {
        // Set up the implementation for this test run
        System.setProperty("jakarta.el.BeanSupport.useStandalone", Boolean.toString(useStandalone));
    }
}
