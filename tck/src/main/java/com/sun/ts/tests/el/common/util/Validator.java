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

package com.sun.ts.tests.el.common.util;

import java.math.BigDecimal;
import java.math.BigInteger;


import jakarta.el.ELProcessor;

import java.lang.System.Logger;

public class Validator {

  private static final Logger logger = System.getLogger(Validator.class.getName());

  protected Validator() {
    // Exists only to defeat instantiation.
  }

  /**
   * This method is used to validate an expression that have at least one
   * BigDecimal in it. We pass in one of the operands(testVal), the other
   * operand is automatically picked up from the NumberList.
   *
   * @param testVal
   *          - One of the operands used in the expression.
   * @param expectedVal
   *          - The expected value returned from the Expression evaluation.
   * @param operator
   *          - The operator in which the operands are compared. (i.e. "+", "-",
   *          etc...)
   */
  public static void testBigDecimal(BigDecimal testVal, Object expectedVal,
      String operator) throws Exception {
    boolean pass = false;
    Class<?> returnType;

    for (int i = 0; TestNum.getNumberList().size() > i; i++) {
      logger.log(Logger.Level.INFO,
          "*** Start " + "\"" + "BigDecimal" + "\"" + " Test Sequence ***");

      Object testNum = TestNum.getNumberList().get(i);

      NameValuePair values[] = NameValuePair.buildNameValuePair(testVal,
          testNum);

      try {
        String expr = ExprEval.buildElExpr(true, operator);
        logger.log(Logger.Level.INFO, "expression to be evaluated is " + expr);
        logger.log(Logger.Level.INFO, "types are BigDecimal and " + testNum.getClass().getName());

        Object result = ExprEval.evaluateValueExpression(expr, values,
            Object.class);

        logger.log(Logger.Level.INFO, "result is " + result.toString());

        /*
         * If operator is "+=" (concatenation) then coerce both operands to
         * String and concatenate them. (NEW to EL 3.0)
         */
        if ("+=".equals(operator)) {

          pass = Validator.runConcatenationTest(testVal, result, testNum);

          // If the Operator is "%" then the return type is Double.
        } else if ("%".equals(operator)) {

          returnType = Double.class;
          logger.log(Logger.Level.INFO, "Setting Expected Type: " + returnType.getName());

          pass = (ExprEval.compareClass(result, returnType)
              && ExprEval.compareValue(result,
                  Double.valueOf(((BigDecimal) expectedVal).doubleValue())));
        } else {
          returnType = BigDecimal.class;
          logger.log(Logger.Level.INFO, "Setting Expected Type: " + returnType.getName());

          pass = (ExprEval.compareClass(result, returnType) && ExprEval
              .compareValue((BigDecimal) result, (BigDecimal) expectedVal, 5));
        }

      } catch (RuntimeException re) {
        ELTestUtil.printStackTrace(re);
        throw new Exception(re);

      } catch (Exception e) {
        ELTestUtil.printStackTrace(e);
        throw new Exception(e);

      } finally {
        ExprEval.cleanup();
        logger.log(Logger.Level.INFO,
            "*** End " + "\"" + "BigDecimal" + "\"" + " Test Sequence ***");
      }

      if (!pass)
        throw new Exception("TEST FAILED: pass = false");

    }
  }

  /**
   * This method is used to validate an expression that have at least one Float
   * in it. We pass in one of the operands(testVal), the other operand is
   * automatically picked up from the NumberList.
   *
   * @param testVal
   *          - One of the operands used in the expression.
   * @param expectedVal
   *          - The expected value returned from the Expression evaluation.
   * @param operator
   *          - The operator in which the operands are compared. (i.e. "+", "-",
   *          etc...)
   */
  public static void testFloat(Float testVal, Object expectedVal,
      String operator) throws Exception {

    boolean pass = false;
    Class<?> returnType;

    // For each NumberType in this list.
    for (int i = 0; TestNum.getNumberList().size() > i; i++) {
      logger.log(Logger.Level.INFO, "*** Start " + "\"" + "Float" + "\"" + " Test Sequence ***");

      Object testNum = TestNum.getNumberList().get(i);

      NameValuePair values[] = NameValuePair.buildNameValuePair(testVal,
          testNum);

      // If Test value from numberList is BigDecimal skip it.
      if (testNum instanceof BigDecimal) {
        logger.log(Logger.Level.INFO, "Skip " + testNum.getClass().getSimpleName()
            + " for Float tests we already tested for this in the"
            + " BigDecimal tests.");
        continue;
      }

      try {
        String expr = ExprEval.buildElExpr(true, operator);
        logger.log(Logger.Level.INFO, "expression to be evaluated is " + expr);
        logger.log(Logger.Level.INFO, "types are Float and " + testNum.getClass().getName());

        Object result = ExprEval.evaluateValueExpression(expr, values,
            Object.class);

        logger.log(Logger.Level.INFO, "result is " + result.toString());

        /*
         * If operator is "+="concatenation then coerce both operands to String
         * and concatenate them. (NEW to EL 3.0)
         */
        if ("+=".equals(operator)) {
          pass = Validator.runConcatenationTest(testVal, result, testNum);

          // If the Operator is "%" then the return type is Double.
        } else if ("%".equals(operator)) {
          returnType = Double.class;
          logger.log(Logger.Level.INFO,
              "Setting Expected Type: " + returnType.getCanonicalName());

          pass = (ExprEval.compareClass(result, returnType)
              && ExprEval.compareValue(result,
                  Double.valueOf(((Float) expectedVal).doubleValue())));
        } else {
          if (testNum instanceof BigInteger) {
            returnType = BigDecimal.class;
            logger.log(Logger.Level.INFO,
                "Setting Expected Type: " + returnType.getCanonicalName());

            pass = (ExprEval.compareClass(result, returnType)
                && ExprEval.compareValue(Float.valueOf(((BigDecimal) result).floatValue()),
                    (Float) expectedVal, 3));
          } else {
            returnType = Double.class;
            logger.log(Logger.Level.INFO,
                "Setting Expected Type: " + returnType.getCanonicalName());

            pass = (ExprEval.compareClass(result, returnType) && ExprEval
                .compareValue((Double) result, (Float) expectedVal, 3));
          }
        }

      } catch (RuntimeException re) {
        ELTestUtil.printStackTrace(re);
        throw new Exception(re);
      } catch (Exception e) {
        ELTestUtil.printStackTrace(e);
        throw new Exception(e);

      } finally {
        ExprEval.cleanup();
        logger.log(Logger.Level.INFO, "*** End " + "\"" + "Float" + "\"" + " Test Sequence ***");
      }

      if (!pass)
        throw new Exception("TEST FAILED: pass = false");

    }
  }

  /**
   * This method is used to validate an expression that have at least one Double
   * in it. We pass in one of the operands(testVal), the other operand is
   * automatically picked up from the NumberList.
   *
   * @param testVal
   *          - One of the operands used in the expression.
   * @param expectedVal
   *          - The expected value returned from the Expression evaluation.
   * @param operator
   *          - The operator in which the operands are compared. (i.e. "+", "-",
   *          etc...)
   */
  public static void testDouble(Double testVal, Object expectedVal,
      String operator) throws Exception {

    boolean pass = false;
    Class<?> returnType;

    // For each NumberType in this list.
    for (int i = 0; TestNum.getNumberList().size() > i; i++) {
      logger.log(Logger.Level.INFO,
          "*** Start " + "\"" + "Double" + "\"" + "Test " + "Sequence ***");

      Object testNum = TestNum.getNumberList().get(i);

      NameValuePair values[] = NameValuePair.buildNameValuePair(testVal,
          testNum);

      // If Test value from numberList is BigDecimal, Float skip it.
      if ((testNum instanceof BigDecimal) || (testNum instanceof Float)) {
        String skipType = testNum.getClass().getSimpleName();
        logger.log(Logger.Level.INFO, "Skip " + skipType + " Data type already "
            + "tested for this in the " + skipType + " tests.");
        continue;
      }

      try {
        String expr = ExprEval.buildElExpr(true, operator);
        logger.log(Logger.Level.INFO, "expression to be evaluated is " + expr);
        logger.log(Logger.Level.INFO, "types are Double and " + testNum.getClass().getName());

        Object result = ExprEval.evaluateValueExpression(expr, values,
            Object.class);

        logger.log(Logger.Level.INFO, "result is " + result.toString());

        /*
         * If operator is "+=" (concatenation) then coerce both operands to
         * String and concatenate them. (NEW to EL 3.0)
         */
        if ("+=".equals(operator)) {
          pass = Validator.runConcatenationTest(testVal, result, testNum);

          // If the Operator is "%" then the return type is Double.
        } else if ("%".equals(operator)) {
          returnType = Double.class;
          logger.log(Logger.Level.INFO,
              "Setting Expected Type: " + returnType.getCanonicalName());

          pass = (ExprEval.compareClass(result, returnType)
              && ExprEval.compareValue(result, expectedVal));
        } else {
          if (testNum instanceof BigInteger) {
            returnType = BigDecimal.class;
            logger.log(Logger.Level.INFO,
                "Setting Expected Type: " + returnType.getCanonicalName());

            pass = (ExprEval.compareClass(result, returnType)
                && ExprEval.compareValue(Double.valueOf(((BigDecimal) result).doubleValue()),
                    expectedVal));
          } else {
            returnType = Double.class;
            logger.log(Logger.Level.INFO,
                "Setting Expected Type: " + returnType.getCanonicalName());

            pass = (ExprEval.compareClass(result, returnType)
                && ExprEval.compareValue(result, expectedVal));
          }
        }

      } catch (RuntimeException re) {
        ELTestUtil.printStackTrace(re);
        throw new Exception(re);

      } catch (Exception e) {
        ELTestUtil.printStackTrace(e);
        throw new Exception(e);

      } finally {
        ExprEval.cleanup();
        logger.log(Logger.Level.INFO, "*** End " + "\"" + "Double" + "\"" + " Test Sequence ***");
      }

      if (!pass)
        throw new Exception("TEST FAILED: pass = false");

    }
  }

  /**
   * This method is used to validate an expression that have at least one
   * NumericString in it (numeric String containing ".", "e", or "E". We pass in
   * one of the operands(testVal), the other operand is automatically picked up
   * from the NumberList.
   *
   * @param testVal
   *          - One of the operands used in the expression.
   * @param expectedVal
   *          - The expected value returned from the Expression evaluation.
   * @param operator
   *          - The operator in which the operands are compared. (i.e. "+", "-",
   *          etc...)
   */
  public static void testNumericString(String testVal, Double expectedVal,
      String operator) throws Exception {

    boolean pass = false;
    Class<?> returnType;

    // For each NumberType in this list.
    for (int i = 0; TestNum.getNumberList().size() > i; i++) {
      logger.log(Logger.Level.INFO, "*** Start " + "\"" + "NumericString" + "\"" + "Test "
          + "Sequence ***");

      Object testNum = TestNum.getNumberList().get(i);

      NameValuePair values[] = NameValuePair.buildNameValuePair(testVal,
          testNum);

      // If Test value from numberList is BigDecimal, Float, Double skip
      // it.
      if ((testNum instanceof BigDecimal) || (testNum instanceof Float)
          || (testNum instanceof Double)) {
        String skipType = testNum.getClass().getSimpleName();
        logger.log(Logger.Level.INFO, "Skip " + skipType + " Data type already "
            + "tested for this in the " + skipType + " tests.");
        continue;
      }

      try {
        String expr = ExprEval.buildElExpr(true, operator);
        logger.log(Logger.Level.INFO, "expression to be evaluated is " + expr);
        logger.log(Logger.Level.INFO, "types are String and " + testNum.getClass().getName());

        Object result = ExprEval.evaluateValueExpression(expr, values,
            Object.class);

        logger.log(Logger.Level.INFO, "result is " + result.toString());

        if ("%".equals(operator)) {
          returnType = Double.class;
          logger.log(Logger.Level.INFO,
              "Setting Expected Type: " + returnType.getCanonicalName());

          pass = (ExprEval.compareClass(result, returnType)
              && ExprEval.compareValue(result, expectedVal));
        } else {
          if (testNum instanceof BigInteger) {
            returnType = BigDecimal.class;
            logger.log(Logger.Level.INFO,
                "Setting Expected Type: " + returnType.getCanonicalName());

            pass = (ExprEval.compareClass(result, returnType)
                && ExprEval.compareValue(Double.valueOf(((BigDecimal) result).doubleValue()),
                    expectedVal));
          } else {
            returnType = Double.class;
            logger.log(Logger.Level.INFO,
                "Setting Expected Type: " + returnType.getCanonicalName());

            pass = (ExprEval.compareClass(result, returnType)
                && ExprEval.compareValue(result, expectedVal));
          }
        }

      } catch (RuntimeException re) {
        ELTestUtil.printStackTrace(re);
        throw new Exception(re);

      } catch (Exception e) {
        ELTestUtil.printStackTrace(e);
        throw new Exception(e);

      } finally {
        ExprEval.cleanup();
        logger.log(Logger.Level.INFO,
            "*** End " + "\"" + "NumericString" + "\"" + " Test Sequence ***");
      }

      if (!pass)
        throw new Exception("TEST FAILED: pass = false");

    }
  }

  /**
   * This method is used to validate an expression that have at least one
   * BigInteger in it. We pass in one of the operands(testVal), the other
   * operand is automatically picked up from the numberList.
   *
   * @param testVal
   *          - One of the operands used in the expression.
   * @param expectedVal
   *          - The expected value returned from the Expression evaluation.
   * @param operator
   *          - The operator in which the operands are compared. (i.e. "+", "-",
   *          etc...)
   */
  public static void testBigInteger(BigInteger testVal, Object expectedVal,
      String operator) throws Exception {

    boolean pass = false;
    Class<?> returnType;

    // For each NumberType in this list.
    for (int i = 0; TestNum.getNumberList().size() > i; i++) {
      logger.log(Logger.Level.INFO,
          "*** Start " + "\"" + "BigInteger" + "\"" + " Test Sequence ***");

      Object testNum = TestNum.getNumberList().get(i);

      NameValuePair values[] = NameValuePair.buildNameValuePair(testVal,
          testNum);

      // If Test value from numberList BigDecimal, Float, Double, or
      // String skip it.
      if ((testNum instanceof BigDecimal) || (testNum instanceof Float)
          || (testNum instanceof Double) || (testNum instanceof String)) {
        String skipType = testNum.getClass().getSimpleName();
        logger.log(Logger.Level.INFO, "Skip " + skipType + " Data type already "
            + "tested for this in the " + skipType + " tests.");
        continue;
      }

      try {
        String expr = ExprEval.buildElExpr(true, operator);
        logger.log(Logger.Level.INFO, "expression to be evaluated is " + expr);
        logger.log(Logger.Level.INFO, "types are BigInteger and " + testNum.getClass().getName());

        Object result = ExprEval.evaluateValueExpression(expr, values,
            Object.class);

        logger.log(Logger.Level.INFO, "result is " + result.toString());

        /*
         * If operator is "+=" then coerce both operands to String and
         * concatenate them. (NEW to EL 3.0)
         */
        if ("+=".equals(operator)) {
          pass = Validator.runConcatenationTest(testVal, result, testNum);

          // If the Operator is "/" then the return type is
          // BigDecimal.
        } else if ("/".equals(operator)) {
          returnType = BigDecimal.class;
          logger.log(Logger.Level.INFO,
              "Setting Expected Type: " + returnType.getCanonicalName());

          pass = (ExprEval.compareClass(result, returnType)
              && ExprEval.compareValue((BigDecimal) result,
                  BigDecimal.valueOf(((BigInteger) expectedVal).doubleValue()),
                  0));
        } else {
          returnType = BigInteger.class;
          logger.log(Logger.Level.INFO,
              "Setting Expected Type: " + returnType.getCanonicalName());

          pass = (ExprEval.compareClass(result, returnType) && ExprEval
              .compareValue((BigInteger) result, (BigInteger) expectedVal, 0));
        }

      } catch (RuntimeException re) {
        ELTestUtil.printStackTrace(re);
        throw new Exception(re);

      } catch (Exception e) {
        ELTestUtil.printStackTrace(e);
        throw new Exception(e);

      } finally {
        ExprEval.cleanup();
        logger.log(Logger.Level.INFO,
            "*** End " + "\"" + "BigInteger" + "\"" + " Test Sequence ***");
      }

      if (!pass)
        throw new Exception("TEST FAILED: pass = false");

    }
  }

  /**
   * This method is used to validate an expression that have at least one Long
   * in it. We pass in one of the operands(testVal), the other operand is
   * automatically picked up from the numberList.
   *
   * @param testVal
   *          - One of the operands used in the expression.
   * @param expectedVal
   *          - The expected value returned from the Expression evaluation.
   * @param operator
   *          - The operator in which the operands are compared. (i.e. "+", "-",
   *          etc...)
   */
  public static void testLong(Long testVal, Object expectedVal, String operator)
      throws Exception {

    boolean pass = false;
    Class<?> returnType;

    // For each NumberType in this list.
    for (int i = 0; TestNum.getNumberList().size() > i; i++) {
      logger.log(Logger.Level.INFO, "*** Start " + "\"" + "Long" + "\"" + " Test Sequence ***");

      Object testNum = TestNum.getNumberList().get(i);

      NameValuePair values[] = NameValuePair.buildNameValuePair(testVal,
          testNum);

      // If Test value from numberList BigDecimal, Float, Double,
      // String or BigInteger skip it.
      if ((testNum instanceof BigDecimal) || (testNum instanceof Float)
          || (testNum instanceof Double) || (testNum instanceof String)
          || (testNum instanceof BigInteger)) {
        String skipType = testNum.getClass().getSimpleName();
        logger.log(Logger.Level.INFO, "Skip " + skipType + " Data type already "
            + "tested for this in the " + skipType + " tests.");
        continue;
      }

      try {
        String expr = ExprEval.buildElExpr(true, operator);
        logger.log(Logger.Level.INFO, "expression to be evaluated is " + expr);
        logger.log(Logger.Level.INFO, "types are  Long and " + testNum.getClass().getName());

        Object result = ExprEval.evaluateValueExpression(expr, values,
            Object.class);

        logger.log(Logger.Level.INFO, "result is " + result.toString());

        /*
         * If operator is "+=" then coerce both operands to String and
         * concatenate them. (NEW to EL 3.0)
         */
        if ("+=".equals(operator)) {
          pass = Validator.runConcatenationTest(testVal, result, testNum);

          // If the Operator is "/" then the return type is Double.
        } else if ("/".equals(operator)) {
          returnType = Double.class;
          logger.log(Logger.Level.INFO,
              "Setting Expected Type: " + returnType.getCanonicalName());

          pass = (ExprEval.compareClass(result, returnType)
              && ExprEval.compareValue(result,
                  Double.valueOf(((Long) expectedVal).doubleValue())));
        } else {
          returnType = Long.class;
          logger.log(Logger.Level.INFO,
              "Setting Expected Type: " + returnType.getCanonicalName());

          pass = (ExprEval.compareClass(result, returnType)
              && ExprEval.compareValue((Long) result, (Long) expectedVal, 0));
        }

      } catch (RuntimeException re) {
        ELTestUtil.printStackTrace(re);
        throw new Exception(re);

      } catch (Exception e) {
        ELTestUtil.printStackTrace(e);
        throw new Exception(e);

      } finally {
        ExprEval.cleanup();
        logger.log(Logger.Level.INFO, "*** End " + "\"" + "Long" + "\"" + " Test Sequence ***");
      }

      if (!pass)
        throw new Exception("TEST FAILED: pass = false");

    }
  }

  /**
   * This method is used to validate an expression that have at least one
   * Integer in it. We pass in one of the operands(testVal), the other operand
   * is automatically picked up from the numberList.
   *
   * @param testVal
   *          - One of the operands used in the expression.
   * @param expectedVal
   *          - The expected value returned from the Expression evaluation.
   * @param operator
   *          - The operator in which the operands are compared. (i.e. "+", "-",
   *          etc...)
   */
  public static void testInteger(Integer testVal, Object expectedVal,
      String operator) throws Exception {

    boolean pass = false;
    Class<?> returnType;

    // For each NumberType in this list.
    for (int i = 0; TestNum.getNumberList().size() > i; i++) {
      logger.log(Logger.Level.INFO,
          "*** Start " + "\"" + "Integer" + "\"" + " Test Sequence ***");

      Object testNum = TestNum.getNumberList().get(i);

      NameValuePair values[] = NameValuePair.buildNameValuePair(testVal,
          testNum);

      // If Test value from numberList BigDecimal, Float, Double, String,
      // Long, or BigInteger skip it.
      if ((testNum instanceof BigDecimal) || (testNum instanceof Float)
          || (testNum instanceof Double) || (testNum instanceof String)
          || (testNum instanceof Long) || (testNum instanceof BigInteger)) {
        String skipType = testNum.getClass().getSimpleName();
        logger.log(Logger.Level.INFO, "Skip " + skipType + " Data type already "
            + "tested for this in the " + skipType + " tests.");
        continue;
      }

      try {
        String expr = ExprEval.buildElExpr(true, operator);
        logger.log(Logger.Level.INFO, "expression to be evaluated is " + expr);
        logger.log(Logger.Level.INFO, "types are  Integer and " + testNum.getClass().getName());

        Object result = ExprEval.evaluateValueExpression(expr, values,
            Object.class);

        logger.log(Logger.Level.INFO, "result is " + result.toString());

        /*
         * If operator is "+=" then coerce both operands to String and
         * concatenate them. (NEW to EL 3.0)
         */
        if ("+=".equals(operator)) {
          pass = Validator.runConcatenationTest(testVal, result, testNum);

          // If the Operator is "/" then the return type is Double.
        } else if ("/".equals(operator)) {
          returnType = Double.class;
          logger.log(Logger.Level.INFO,
              "Setting Expected Type: " + returnType.getCanonicalName());

          pass = (ExprEval.compareClass(result, returnType)
              && ExprEval.compareValue(result,
                  Double.valueOf(((Integer) expectedVal).doubleValue())));
        } else {
          returnType = Long.class;
          logger.log(Logger.Level.INFO,
              "Setting Expected Type: " + returnType.getCanonicalName());

          pass = (ExprEval.compareClass(result, returnType)
              && ExprEval.compareValue(result,
                  Long.valueOf(((Integer) expectedVal).longValue())));
        }

      } catch (RuntimeException re) {
        ELTestUtil.printStackTrace(re);
        throw new Exception(re);

      } catch (Exception e) {
        ELTestUtil.printStackTrace(e);
        throw new Exception(e);

      } finally {
        ExprEval.cleanup();
        logger.log(Logger.Level.INFO,
            "*** End " + "\"" + "Integer" + "\"" + " Test Sequence ***");
      }

      if (!pass)
        throw new Exception("TEST FAILED: pass = false");

    }
  }

  /**
   * This method is used to validate an expression that have at least one Short
   * in it. We pass in one of the operands(testVal), the other operand is
   * automatically picked up from the numberList.
   *
   * @param testVal
   *          - One of the operands used in the expression.
   * @param expectedVal
   *          - The expected value returned from the Expression evaluation.
   * @param operator
   *          - The operator in which the operands are compared. (i.e. "+", "-",
   *          etc...)
   */
  public static void testShort(Short testVal, Object expectedVal,
      String operator) throws Exception {

    boolean pass = false;
    Class<?> returnType;

    // For each NumberType in this list.
    for (int i = 0; TestNum.getNumberList().size() > i; i++) {
      logger.log(Logger.Level.INFO,"*** Start " + "\"" + "Short" + "\"" + " Test Sequence ***");

      Object testNum = TestNum.getNumberList().get(i);

      NameValuePair values[] = NameValuePair.buildNameValuePair(testVal,
          testNum);

      // If Test value from numberList BigDecimal, Float, Double, String,
      // Long, BigInteger, Integer skip it.
      if (!(testNum instanceof Short || testNum instanceof Byte)) {
        String skipType = testNum.getClass().getSimpleName();
        logger.log(Logger.Level.INFO, "Skip " + skipType + " Data type already "
            + "tested for this in the " + skipType + " tests.");
        continue;
      }

      try {
        String expr = ExprEval.buildElExpr(true, operator);
        logger.log(Logger.Level.INFO, "expression to be evaluated is " + expr);
        logger.log(Logger.Level.INFO, "types are  Short and " + testNum.getClass().getName());

        Object result = ExprEval.evaluateValueExpression(expr, values,
            Object.class);

        logger.log(Logger.Level.INFO, "result is " + result.toString());

        /*
         * If operator is "+=" then coerce both operands to String and
         * concatenate them. (NEW to EL 3.0)
         */
        if ("+=".equals(operator)) {
          pass = Validator.runConcatenationTest(testVal, result, testNum);

          // If the Operator is "/" then the return type is Double.
        } else if ("/".equals(operator)) {
          returnType = Double.class;
          logger.log(Logger.Level.INFO,
              "Setting Expected Type: " + returnType.getCanonicalName());

          pass = (ExprEval.compareClass(result, returnType)
              && ExprEval.compareValue(result,
                  Double.valueOf(((Short) expectedVal).doubleValue())));
        } else {
          returnType = Long.class;
          logger.log(Logger.Level.INFO,
              "Setting Expected Type: " + returnType.getCanonicalName());

          pass = (ExprEval.compareClass(result, returnType) && ExprEval
              .compareValue(result, Long.valueOf(((Short) expectedVal).longValue())));
        }

      } catch (RuntimeException re) {
        ELTestUtil.printStackTrace(re);
        throw new Exception(re);

      } catch (Exception e) {
        ELTestUtil.printStackTrace(e);
        throw new Exception(e);

      } finally {
        ExprEval.cleanup();
        logger.log(Logger.Level.INFO, "*** End " + "\"" + "Short" + "\"" + " Test Sequence ***");
      }

      if (!pass)
        throw new Exception("TEST FAILED: pass = false");

    }
  }

  /**
   * This method is used to validate an expression that have at least one Byte
   * in it. We pass in one of the operands(testVal), the other operand is
   * automatically picked up from the numberList.
   *
   * @param testVal
   *          - One of the operands used in the expression.
   * @param expectedVal
   *          - The expected value returned from the Expression evaluation.
   * @param operator
   *          - The operator in which the operands are compared. (i.e. "+", "-",
   *          etc...)
   */
  public static void testByte(Byte testVal, Object expectedVal, String operator)
      throws Exception {

    boolean pass = false;
    Class<?> returnType;

    // For each NumberType in this list.
    for (int i = 0; TestNum.getNumberList().size() > i; i++) {
      logger.log(Logger.Level.INFO, "*** Start " + "\"" + "Byte" + "\"" + " Test Sequence ***");

      Object testNum = TestNum.getNumberList().get(i);

      NameValuePair values[] = NameValuePair.buildNameValuePair(testVal,
          testNum);

      // If Test value from numberList BigDecimal, Float, Double, String,
      // Long, BigInteger, Integer, Short skip it.
      if (!(testNum instanceof Byte)) {
        String skipType = testNum.getClass().getSimpleName();
        logger.log(Logger.Level.INFO, "Skip " + skipType + " Data type already "
            + "tested for this in the " + skipType + " tests.");
        continue;
      }

      try {
        String expr = ExprEval.buildElExpr(true, operator);
        logger.log(Logger.Level.INFO, "expression to be evaluated is " + expr);
        logger.log(Logger.Level.INFO, "types are  Byte and " + testNum.getClass().getName());

        Object result = ExprEval.evaluateValueExpression(expr, values,
            Object.class);

        logger.log(Logger.Level.INFO, "result is " + result.toString());

        /*
         * If operator is "+=" then coerce both operands to String and
         * concatenate them. (NEW to EL 3.0)
         */
        if ("+=".equals(operator)) {
          pass = Validator.runConcatenationTest(testVal, result, testNum);

          // If the Operator is "/" then the return type is Double.
        } else if ("/".equals(operator)) {
          returnType = Double.class;
          logger.log(Logger.Level.INFO,
              "Setting Expected Type: " + returnType.getCanonicalName());

          pass = (ExprEval.compareClass(result, returnType)
              && ExprEval.compareValue(result,
                  Double.valueOf(((Byte) expectedVal).doubleValue())));
        } else {
          returnType = Long.class;
          logger.log(Logger.Level.INFO,
              "Setting Expected Type: " + returnType.getCanonicalName());

          pass = (ExprEval.compareClass(result, returnType) && ExprEval
              .compareValue(result, Long.valueOf(((Byte) expectedVal).longValue())));
        }

      } catch (RuntimeException re) {
        ELTestUtil.printStackTrace(re);
        throw new Exception(re);

      } catch (Exception e) {
        ELTestUtil.printStackTrace(e);
        throw new Exception(e);

      } finally {
        ExprEval.cleanup();
        logger.log(Logger.Level.INFO,"*** End " + "\"" + "Byte" + "\"" + " Test Sequence ***");
      }

      if (!pass)
        throw new Exception("TEST FAILED: pass = false");

    }
  }

  /**
   * This method is used to validate an expression that has at least one boolean
   * in it.
   *
   * @param testValOne
   *          - The boolean operand.
   * @param testValTwo
   *          - The second operand that will be coerced to a boolean.
   * @param expectedVal
   *          - The expected value returned from the Expression evaluation.
   * @param operator
   *          - The operator in which the operands are compared. (i.e. "+", "-",
   *          etc...)
   */
  public static void testBoolean(boolean testValOne, Object testValTwo,
      Object expectedVal, String operator) throws Exception {

    boolean pass = false;

    NameValuePair values[] = NameValuePair.buildNameValuePair(Boolean.valueOf(testValOne), testValTwo);

    try {
      logger.log(Logger.Level.INFO,
          "*** Start " + "\"" + "Boolean" + "\"" + " Test Sequence ***");
      String expr = ExprEval.buildElExpr(true, operator);
      logger.log(Logger.Level.INFO, "expression to be evaluated is " + expr);
      logger.log(Logger.Level.INFO, "types are  Boolean and " + testValTwo.getClass().getName());

      Object result = ExprEval.evaluateValueExpression(expr, values,
          Object.class);

      /*
       * If operator is "+=" then coerce both operands to String and concatenate
       * them. (NEW to EL 3.0)
       */
      if ("+=".equals(operator)) {
        pass = Validator.runConcatenationTest(Boolean.valueOf(testValOne), result, testValTwo);

      } else {
        logger.log(Logger.Level.INFO, "result is " + result.toString());
        pass = (ExprEval.compareClass(result, Boolean.class)
            && ExprEval.compareValue(result, expectedVal));
      }

    } catch (RuntimeException re) {
      ELTestUtil.printStackTrace(re);
      throw new Exception(re);

    } catch (Exception e) {
      ELTestUtil.printStackTrace(e);
      throw new Exception(e);

    } finally {
      ExprEval.cleanup();
      logger.log(Logger.Level.INFO, "*** End " + "\"" + "Boolean" + "\"" + " Test Sequence ***");
    }

    if (!pass)
      throw new Exception("TEST FAILED: pass = false");

  }

  public static void testExpression(ELProcessor elp, String expr,
      Object expected, String testName) throws Exception {
    boolean pass = false;

    try {
      logger.log(Logger.Level.INFO, "*** Start " + testName + " Test Sequence ***");
      logger.log(Logger.Level.INFO, "expression to be evaluated is " + expr);
      Object result = elp.eval(expr);

      pass = ExprEval.compareClass(result, expected.getClass())
          && ExprEval.compareValue(result, expected);

      if (!pass)
        throw new Exception("TEST FAILED: pass = false");

    } catch (RuntimeException re) {
      ELTestUtil.printStackTrace(re);
      throw new Exception(re);

    } catch (Exception e) {
      ELTestUtil.printStackTrace(e);
      throw new Exception(e);

    } finally {
      ExprEval.cleanup();
      logger.log(Logger.Level.INFO, "*** End " + testName + " Test Sequence ***");
    }
  }

  // ------------------------- private methods

  private static boolean runConcatenationTest(Object testVal, Object result,
      Object testNum) {

    Class<String> returnType = String.class;
    String expectedResult = testVal.toString() + testNum.toString();

    logger.log(Logger.Level.INFO, "Setting Expected Type: " + returnType.getName());

    return (ExprEval.compareClass(result, returnType)
        && ExprEval.compareValue(result, expectedResult));
  }
}
