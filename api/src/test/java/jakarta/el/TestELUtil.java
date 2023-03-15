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
import java.util.TimeZone;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class TestELUtil extends ELBaseTest {

    /*
     * https://github.com/jakartaee/expression-language/issues/188
     */
    @ParameterizedTest
    @MethodSource("data")
    public void testAccessibleMethod(boolean useStandalone) throws Exception {
        configureBeanSupport(useStandalone);
        TimeZone tz = TimeZone.getDefault();
        Method m = ELUtil.findMethod(tz.getClass(), tz, "getRawOffset", null, null);
        m.invoke(tz);
    }
}
