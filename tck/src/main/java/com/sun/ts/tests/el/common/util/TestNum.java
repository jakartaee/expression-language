/*
 * Copyright (c) 2006, 2025 Oracle and/or its affiliates and others.
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
package com.sun.ts.tests.el.common.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;

/**
 * Used to store lists that will be utilized across the board for a common
 * point of reference when testing.
 */
public final class TestNum {

    private static final String NUMBER_REFERENCE = "1";

    public static final Instant DATE_REFERENCE_BEFORE = Instant.parse("2025-04-03T02:00:59.00Z");
    public static final Instant DATE_REFERENCE = Instant.parse("2025-04-03T02:01:00.00Z");
    public static final Instant DATE_REFERENCE_AFTER = Instant.parse("2025-04-03T02:01:01.00Z");

    /**
     * Private as this class will only have static methods and members.
     */
    private TestNum() {
    }

    /**
     * Used for a common list of Float values when testing.
     *
     * @return - A set list of common Floats that we use as test values.
     */
    public static ArrayList<Float> getFloatList() {

        ArrayList<Float> floatList = new ArrayList<>();

        floatList.add(Float.valueOf("1.00005f"));
        floatList.add(Float.valueOf("1.5E-4d"));
        floatList.add(Float.valueOf("1.5E+4"));
        floatList.add(Float.valueOf("1.5e+4"));

        return floatList;

    }

    /**
     * Used a common reference point for Number types and a common value is
     * assigned (1).
     *
     * @return - A common List of Number types with a constant value.
     */
    public static ArrayList<Object> getNumberList() {

        ArrayList<Object> numberList = new ArrayList<>();

        numberList.add(BigDecimal.valueOf(Long.parseLong(NUMBER_REFERENCE)));
        numberList.add(Double.valueOf(NUMBER_REFERENCE));
        numberList.add(Float.valueOf(NUMBER_REFERENCE));
        numberList.add(NUMBER_REFERENCE + ".0");
        numberList.add(NUMBER_REFERENCE + "e0");
        numberList.add(NUMBER_REFERENCE + "E0");
        numberList.add(BigInteger.valueOf(Long.parseLong(NUMBER_REFERENCE)));
        numberList.add(Long.valueOf(NUMBER_REFERENCE));
        numberList.add(Integer.valueOf(NUMBER_REFERENCE));
        numberList.add(Short.valueOf(NUMBER_REFERENCE));
        numberList.add(Byte.valueOf(NUMBER_REFERENCE));

        return numberList;

    }


    /**
     * Used to provide a list of date and times using different implementations
     * that all refer to 2025-04-03 02:01 UTC.
     */
    public static ArrayList<Object> getDateTimeList() {

        ArrayList<Object> dateTimeList = new ArrayList<>();

        dateTimeList.add(Date.from(DATE_REFERENCE));
        dateTimeList.add(Clock.fixed(DATE_REFERENCE, ZoneId.of("Z")));
        dateTimeList.add(DATE_REFERENCE);
        dateTimeList.add(ZonedDateTime.ofInstant(DATE_REFERENCE, ZoneId.of("+12:00")));

        return dateTimeList;
    }
}
