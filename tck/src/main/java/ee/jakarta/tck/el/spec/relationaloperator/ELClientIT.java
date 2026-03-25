/*
 * Copyright (c) 2009, 2025 Oracle and/or its affiliates and others.
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
package ee.jakarta.tck.el.spec.relationaloperator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import ee.jakarta.tck.el.common.util.ExprEval;
import ee.jakarta.tck.el.common.util.NameValuePair;
import ee.jakarta.tck.el.common.util.TestNum;

import java.lang.System.Logger;

public class ELClientIT {

  private static final Logger logger = System.getLogger(ELClientIT.class.getName());

  private List<Object> numberList;

  private enum TestEnum {
    APPLE, PEAR
  }

  // Data Type to test String Coercion.
  private static final DougType DT = new DougType();

  private static final NickType NT = new NickType();

  public ELClientIT(){
    numberList = TestNum.getNumberList();
  }

  @AfterEach
  public void cleanup() throws Exception {
    logger.log(Logger.Level.INFO, "Cleanup method called");
  }

  @BeforeEach
  void logStartTest(TestInfo testInfo) {
    logger.log(Logger.Level.INFO, "STARTING TEST : "+testInfo.getDisplayName());
  }

  @AfterEach
  void logFinishTest(TestInfo testInfo) {
    logger.log(Logger.Level.INFO, "FINISHED TEST : "+testInfo.getDisplayName());
  }


  /**
   * @testName  elEqualOperandLessThanOrEqualTest
   * @assertion_ids  EL:SPEC:21.1
   * @test_Strategy  Validate that if the operands in an EL &lt;= or le operation
   *                 are equal, the result is true.
   */
  @Test
  public void elEqualOperandLessThanOrEqualTest() throws Exception {

    boolean pass = false;

    try {
      // Expression One
      String expr1 = ExprEval.buildElExpr(false, "<=");
      logger.log(Logger.Level.TRACE, "first expression to be evaluated is " + expr1);

      NameValuePair values1[] = NameValuePair
          .buildNameValuePair(Float.valueOf(-1.0f), Float.valueOf((float) -1.0));

      Object result1 = ExprEval.evaluateValueExpression(expr1, values1,
          Boolean.class);
      logger.log(Logger.Level.TRACE, "first result is " + result1.toString());

      // Expression Two
      String expr2 = ExprEval.buildElExpr(true, "le");
      logger.log(Logger.Level.TRACE, "second expression to be evaluated is " + expr2);

      NameValuePair values2[] = NameValuePair
          .buildNameValuePair(new BigDecimal("1.0"), BigDecimal.ONE);
      Object result2 = ExprEval.evaluateValueExpression(expr2, values2,
          Boolean.class);

      logger.log(Logger.Level.TRACE, "second result is " + result2.toString());

      pass = (ExprEval.compareClass(result1, Boolean.class)
          && ExprEval.compareValue((Boolean) result1, Boolean.TRUE)
          && ExprEval.compareClass(result2, Boolean.class)
          && ExprEval.compareValue((Boolean) result2, Boolean.TRUE));

    } catch (Exception e) {
      throw new Exception(e);
    } finally {
      ExprEval.cleanup();
    }

    if (!pass)
      throw new Exception("TEST FAILED: pass = false");
  }

  /**
   * @testName  elEqualOperandGreaterThanOrEqualTest
   * @assertion_ids  EL:SPEC:21.1
   * @test_Strategy  Validate that if the operands in an EL >= or ge operation
   *                 are equal, the result is true.
   */
  @Test
  public void elEqualOperandGreaterThanOrEqualTest() throws Exception {

    boolean pass = false;

    try {
      // Expression One
      String expr1 = ExprEval.buildElExpr(false, ">=");
      logger.log(Logger.Level.TRACE, "first expression to be evaluated is " + expr1);

      NameValuePair values1[] = NameValuePair
          .buildNameValuePair(Float.valueOf(-1.0f), Float.valueOf((float)-1.0));

      Object result1 = ExprEval.evaluateValueExpression(expr1, values1,
          Boolean.class);
      logger.log(Logger.Level.TRACE, "first result is " + result1.toString());

      // Expression Two
      String expr2 = ExprEval.buildElExpr(true, "ge");
      logger.log(Logger.Level.TRACE, "second expression to be evaluated is " + expr2);

      NameValuePair values2[] = NameValuePair
          .buildNameValuePair(new BigInteger("1010"), BigInteger.TEN);

      Object result2 = ExprEval.evaluateValueExpression(expr2, values2,
          Boolean.class);
      logger.log(Logger.Level.TRACE, "second result is " + result2.toString());

      pass = (ExprEval.compareClass(result1, Boolean.class)
          && ExprEval.compareValue((Boolean) result1, Boolean.TRUE)
          && ExprEval.compareClass(result2, Boolean.class)
          && ExprEval.compareValue((Boolean) result2, Boolean.TRUE));

    } catch (Exception e) {
      throw new Exception(e);
    } finally {
      ExprEval.cleanup();
    }

    if (!pass)
      throw new Exception("TEST FAILED: pass = false");
  }

  /**
   * @testName  elNullOperandLessThanOrEqualTest
   * @assertion_ids  EL:SPEC:21.2
   * @test_Strategy  Validate that if one of the operands in an EL &lt;= or le
   *                 operation is null, the result is false.
   */
  @Test
  public void elNullOperandLessThanOrEqualTest() throws Exception {

    boolean pass = false;

    try {

      Object result1 = ExprEval.evaluateValueExpression("${1 <= nullValue}",
          null, Object.class);
      logger.log(Logger.Level.TRACE, "first result is " + result1.toString());

      Object result2 = ExprEval.evaluateValueExpression("#{2 le nullValue}",
          null, Object.class);
      logger.log(Logger.Level.TRACE, "second result is " + result2.toString());

      pass = (ExprEval.compareClass(result1, Boolean.class)
          && ExprEval.compareValue((Boolean) result1, Boolean.FALSE)
          && ExprEval.compareClass(result2, Boolean.class)
          && ExprEval.compareValue((Boolean) result2, Boolean.FALSE));

    } catch (Exception e) {
      throw new Exception(e);
    }

    if (!pass)
      throw new Exception("TEST FAILED: pass = false");
  }

  /**
   * @testName  elNullOperandGreaterThanOrEqualTest
   * @assertion_ids  EL:SPEC:21.2
   * @test_Strategy  Validate that if one of the operands in an EL >= or ge
   *                 operation is null, the result is false.
   */
  @Test
  public void elNullOperandGreaterThanOrEqualTest() throws Exception {

    boolean pass = false;

    try {

      Object result1 = ExprEval.evaluateValueExpression("${1 >= nullValue}",
          null, Object.class);
      logger.log(Logger.Level.TRACE, "first result is " + result1.toString());

      Object result2 = ExprEval.evaluateValueExpression("#{2 ge nullValue}",
          null, Object.class);
      logger.log(Logger.Level.TRACE, "second result is " + result2.toString());

      pass = (ExprEval.compareClass(result1, Boolean.class)
          && ExprEval.compareValue((Boolean) result1, Boolean.FALSE)
          && ExprEval.compareClass(result2, Boolean.class)
          && ExprEval.compareValue((Boolean) result2, Boolean.FALSE));

    } catch (Exception e) {
      throw new Exception(e);
    }

    if (!pass)
      throw new Exception("TEST FAILED: pass = false");
  }

  /**
   * @testName  elNullOperandNotEqualTest
   * @assertion_ids  EL:SPEC:22.2
   * @test_Strategy  Validate that if one of the operands is null in an EL !=,
   *                 ne operation return true.
   */
  @Test
  public void elNullOperandNotEqualTest() throws Exception {

    boolean pass = false;

    try {

      Object result1 = ExprEval.evaluateValueExpression("${1 != nullValue}",
          null, Object.class);
      logger.log(Logger.Level.TRACE, "first result is " + result1.toString());

      Object result2 = ExprEval.evaluateValueExpression("#{2 ne nullValue}",
          null, Object.class);
      logger.log(Logger.Level.TRACE, "second result is " + result2.toString());

      pass = (ExprEval.compareClass(result1, Boolean.class)
          && ExprEval.compareValue((Boolean) result1, Boolean.TRUE)
          && ExprEval.compareClass(result2, Boolean.class)
          && ExprEval.compareValue((Boolean) result2, Boolean.TRUE));

    } catch (Exception e) {
      throw new Exception(e);
    }

    if (!pass)
      throw new Exception("TEST FAILED: pass = false");

  } // End elNullOperandNotEqualTest

  /**
   * @testName  elNullOperandEqualTest
   * @assertion_ids  EL:SPEC:22.2
   * @test_Strategy  Validate that if one of the operands is null in an EL =, eq
   *                 operation return false.
   */
  @Test
  public void elNullOperandEqualTest() throws Exception {

    boolean pass = false;

    try {

      Object result1 = ExprEval.evaluateValueExpression("${1 == nullValue}",
          null, Object.class);
      logger.log(Logger.Level.TRACE, "first result is " + result1.toString());

      Object result2 = ExprEval.evaluateValueExpression("#{2 eq nullValue}",
          null, Object.class);
      logger.log(Logger.Level.TRACE, "second result is " + result2.toString());

      pass = (ExprEval.compareClass(result1, Boolean.class)
          && ExprEval.compareValue((Boolean) result1, Boolean.FALSE)
          && ExprEval.compareClass(result2, Boolean.class)
          && ExprEval.compareValue((Boolean) result2, Boolean.FALSE));

    } catch (Exception e) {
      throw new Exception(e);
    }

    if (!pass)
      throw new Exception("TEST FAILED: pass = false");

  } // End elNullOperandEqualTest

  /**
   * @testName  elBigDecimalLessThanTest
   * @assertion_ids  EL:SPEC:21.3
   * @test_Strategy  Validate that if one of the operands in an EL "&lt;" or "lt"
   *                 operation is a BigDecimal, the result is coerced to
   *                 BigDecimal and the correct boolean value is returned.
   *
   *                 Equations tested: BigDecimal "&lt;" &amp; "lt" BigDecimal
   *                 BigDecimal "&lt;" &amp; "lt" Double BigDecimal "&lt;" &amp; "lt" Float
   *                 BigDecimal "&lt;" &amp; "lt" BigInteger BigDecimal "&lt;" &amp; "lt"
   *                 Integer BigDecimal "&lt;" &amp; "lt" Long BigDecimal "&lt;" &amp; "lt"
   *                 Short BigDecimal "&lt;" &amp; "lt" Byte
   */
  @Test
  public void elBigDecimalLessThanTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(BigDecimal.valueOf(10.531), Boolean.FALSE, "<");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(BigDecimal.valueOf(1.0000), Boolean.FALSE, "lt");

    // value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(BigDecimal.valueOf(0.531), Boolean.TRUE, "lt");

  }

  /**
   * @testName  elBigDecimalLessThanEqualTest
   * @assertion_ids  EL:SPEC:21.1; EL:SPEC:21.3
   * @test_Strategy  Validate that if one of the operands in an EL "&lt;=" or "le"
   *                 operation is a BigDecimal, the result is coerced to
   *                 BigDecimal and the correct boolean value is returned.
   *
   *                 Equations tested: BigDecimal "&lt;=" &amp; "le" BigDecimal
   *                 BigDecimal "&lt;=" &amp; "le" Double BigDecimal "&lt;=" &amp; "le" Float
   *                 BigDecimal "&lt;=" &amp; "le" BigInteger BigDecimal "&lt;=" &amp; "le"
   *                 Integer BigDecimal "&lt;=" &amp; "le" Long BigDecimal "&lt;=" &amp; "le"
   *                 Short BigDecimal "&lt;=" &amp; "le" Byte
   */
  @Test
  public void elBigDecimalLessThanEqualTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(BigDecimal.valueOf(10.531), Boolean.FALSE, "<=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(BigDecimal.valueOf(1.0000), Boolean.TRUE, "le");

    // Value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(BigDecimal.valueOf(-10.531), Boolean.TRUE, "<=");
  }

  /**
   * @testName  elBigDecimalGreaterThanTest
   * @assertion_ids  EL:SPEC:21.3
   * @test_Strategy  Validate that if one of the operands in an EL ">" or "gt"
   *                 operation is a BigDecimal, the result is coerced to
   *                 BigDecimal and the correct boolean value is returned.
   *
   *                 Equations tested: BigDecimal ">" &amp; "gt" BigDecimal
   *                 BigDecimal ">" &amp; "gt" Double BigDecimal ">" &amp; "gt" Float
   *                 BigDecimal ">" &amp; "gt" BigInteger BigDecimal ">" &amp; "gt"
   *                 Integer BigDecimal ">" &amp; "gt" Long BigDecimal ">" &amp; "gt"
   *                 Short BigDecimal ">" &amp; "gt" Byte
   */
  @Test
  public void elBigDecimalGreaterThanTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(BigDecimal.valueOf(10.531), Boolean.TRUE, ">");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(BigDecimal.valueOf(1.0000), Boolean.FALSE, ">");

    // value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(BigDecimal.valueOf(0.531), Boolean.FALSE, "gt");

  }

  /**
   * @testName  elBigDecimalGreaterThanEqualTest
   * @assertion_ids  EL:SPEC:21.1; EL:SPEC:21.3
   * @test_Strategy  Validate that if one of the operands in an EL ">=" or "ge"
   *                 operation is a BigDecimal, the result is coerced to
   *                 BigDecimal and the correct boolean value is returned.
   *
   *                 Equations tested: BigDecimal ">=" &amp; "ge" BigDecimal
   *                 BigDecimal ">=" &amp; "ge" Double BigDecimal ">=" &amp; "ge" Float
   *                 BigDecimal ">=" &amp; "ge" BigInteger BigDecimal ">=" &amp; "ge"
   *                 Integer BigDecimal ">=" &amp; "ge" Long BigDecimal ">=" &amp; "ge"
   *                 Short BigDecimal ">=" &amp; "ge" Byte
   */
  @Test
  public void elBigDecimalGreaterThanEqualTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(BigDecimal.valueOf(10.531), Boolean.TRUE, ">=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(BigDecimal.valueOf(1.0000), Boolean.TRUE, "ge");

    // value passed in is smaller than the COMPARATOR.
    this.testOperatorBoolean(BigDecimal.valueOf(-1.0000), Boolean.FALSE, "ge");

  }

  /**
   * @testName  elBigDecimalEqualToTest
   * @assertion_ids  EL:SPEC:22.1; EL:SPEC:22.3.1
   * @test_Strategy  Validate that if one of the operands in an EL "==" or "eq"
   *                 operation is a BigDecimal, the result is coerced to
   *                 BigDecimal and the correct boolean value is returned.
   *
   *                 Equations tested: BigDecimal "==" &amp; "eq" BigDecimal
   *                 BigDecimal "==" &amp; "eq" Double BigDecimal "==" &amp; "eq" Float
   *                 BigDecimal "==" &amp; "eq" BigInteger BigDecimal "==" &amp; "eq"
   *                 Integer BigDecimal "==" &amp; "eq" Long BigDecimal "==" &amp; "eq"
   *                 Short BigDecimal "==" &amp; "eq" Byte
   */
  @Test
  public void elBigDecimalEqualToTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(BigDecimal.valueOf(10.531), Boolean.FALSE, "==");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(BigDecimal.valueOf(1), Boolean.TRUE, "eq");

  }

  /**
   * @testName  elBigDecimalNotEqualToTest
   * @assertion_ids  EL:SPEC:22.3.2
   * @test_Strategy  Validate that if one of the operands in an EL "!=" or "ne"
   *                 operation is a BigDecimal, the result is coerced to
   *                 BigDecimal and the correct boolean value is returned.
   *
   *                 Equations tested: BigDecimal "!=" &amp; "ne" BigDecimal
   *                 BigDecimal "!=" &amp; "ne" Double BigDecimal "!=" &amp; "ne" Float
   *                 BigDecimal "!=" &amp; "ne" BigInteger BigDecimal "!=" &amp; "ne"
   *                 Integer BigDecimal "!=" &amp; "ne" Long BigDecimal "!=" &amp; "ne"
   *                 Short BigDecimal "!=" &amp; "ne" Byte
   */
  @Test
  public void elBigDecimalNotEqualToTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(BigDecimal.valueOf(10.531), Boolean.TRUE, "!=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(BigDecimal.valueOf(1), Boolean.FALSE, "ne");

  }

  /**
   * @testName  elFloatLessThanTest
   * @assertion_ids  EL:SPEC:21.4
   * @test_Strategy  Validate that if one of the operands in an EL "&lt;" or "lt"
   *                 operation is a Float, the result is coerced to Double and
   *                 the correct boolean value is returned.
   *
   *                 Equations tested: Float "&lt;" &amp; "lt" Double Float "&lt;" &amp; "lt"
   *                 Float Float "&lt;" &amp; "lt" BigInteger Float "&lt;" &amp; "lt" Integer
   *                 Float "&lt;" &amp; "lt" Long Float "&lt;" &amp; "lt" Short Float "&lt;" &amp;
   *                 "lt" Byte
   */
  @Test
  public void elFloatLessThanTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Float.valueOf(10f), Boolean.FALSE, "<");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Float.valueOf(1f), Boolean.FALSE, "<");

    // value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(Float.valueOf(-10f), Boolean.TRUE, "lt");

  }

  /**
   * @testName  elFloatLessThanEqualTest
   * @assertion_ids  EL:SPEC:21.1; EL:SPEC:21.4
   * @test_Strategy  Validate that if one of the operands in an EL "&lt;=" or "le"
   *                 operation is a Float, the result is coerced to Double and
   *                 the correct boolean value is returned.
   *
   *                 Equations tested: Float "&lt;=" &amp; "le" Double Float "&lt;=" &amp;
   *                 "le" Float Float "&lt;=" &amp; "le" BigInteger Float "&lt;=" &amp; "le"
   *                 Integer Float "&lt;=" &amp; "le" Long Float "&lt;=" &amp; "le" Short
   *                 Float "&lt;=" &amp; "le" Byte
   */
  @Test
  public void elFloatLessThanEqualTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Float.valueOf(10f), Boolean.FALSE, "<=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Float.valueOf(1f), Boolean.TRUE, "le");

    // Value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(Float.valueOf(-10f), Boolean.TRUE, "<=");
  }

  /**
   * @testName  elFloatGreaterThanTest
   * @assertion_ids  EL:SPEC:21.4
   * @test_Strategy  Validate that if one of the operands in an EL ">" or "gt"
   *                 operation is a Float, the result is coerced to Double and
   *                 the correct boolean value is returned.
   *
   *                 Equations tested: Float ">" &amp; "gt" Double Float ">" &amp; "gt"
   *                 Float Float ">" &amp; "gt" BigInteger Float ">" &amp; "gt" Integer
   *                 Float ">" &amp; "gt" Long Float ">" &amp; "gt" Short Float ">" &amp;
   *                 "gt" Byte
   */
  @Test
  public void elFloatGreaterThanTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Float.valueOf(10531f), Boolean.TRUE, ">");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Float.valueOf(1f), Boolean.FALSE, ">");

    // value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(Float.valueOf(-531f), Boolean.FALSE, "gt");

  }

  /**
   * @testName  elFloatGreaterThanEqualTest
   * @assertion_ids  EL:SPEC:21.1; EL:SPEC:21.4
   * @test_Strategy  Validate that if one of the operands in an EL ">=" or "ge"
   *                 operation is a Float, the result is coerced to Double and
   *                 the correct boolean value is returned.
   *
   *                 Equations tested: Float ">=" &amp; "ge" Double Float ">=" &amp;
   *                 "ge" Float Float ">=" &amp; "ge" BigInteger Float ">=" &amp; "ge"
   *                 Integer Float ">=" &amp; "ge" Long Float ">=" &amp; "ge" Short
   *                 Float ">=" &amp; "ge" Byte
   */
  @Test
  public void elFloatGreaterThanEqualTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Float.valueOf(10531f), Boolean.TRUE, ">=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Float.valueOf(1f), Boolean.TRUE, "ge");

    // value passed in is smaller than the COMPARATOR.
    this.testOperatorBoolean(Float.valueOf(-1f), Boolean.FALSE, "ge");

  }

  /**
   * @testName  elFloatEqualToTest
   * @assertion_ids  EL:SPEC:22.1; EL:SPEC:22.4
   * @test_Strategy  Validate that if one of the operands in an EL "==" or "eq"
   *                 operation is a Float, the result is coerced to Double and
   *                 the correct boolean value is returned.
   *
   *                 Equations tested: Float "==" &amp; "eq" Double Float "==" &amp;
   *                 "eq" Float Float "==" &amp; "eq" BigInteger Float "==" &amp; "eq"
   *                 Integer Float "==" &amp; "eq" Long Float "==" &amp; "eq" Short
   *                 Float "==" &amp; "eq" Byte
   */
  @Test
  public void elFloatEqualToTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Float.valueOf(10531), Boolean.FALSE, "==");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Float.valueOf(1), Boolean.TRUE, "eq");

  }

  /**
   * @testName  elFloatNotEqualToTest
   * @assertion_ids  EL:SPEC:22.4
   * @test_Strategy  Validate that if one of the operands in an EL "!=" or "ne"
   *                 operation is a Float, the result is coerced to Double and
   *                 the correct boolean value is returned.
   *
   *                 Equations tested: Float "!=" &amp; "ne" Double Float "!=" &amp;
   *                 "ne" Float Float "!=" &amp; "ne" BigInteger Float "!=" &amp; "ne"
   *                 Integer Float "!=" &amp; "ne" Long Float "!=" &amp; "ne" Short
   *                 Float "!=" &amp; "ne" Byte
   */
  @Test
  public void elFloatNotEqualToTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Float.valueOf(10531), Boolean.TRUE, "!=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Float.valueOf(1), Boolean.FALSE, "ne");

  }

  /**
   * @testName  elDoubleLessThanTest
   * @assertion_ids  EL:SPEC:21.4
   * @test_Strategy  Validate that if one of the operands in an EL "&lt;" or "lt"
   *                 operation is a Double, the result is coerced to Double and
   *                 the correct boolean value is returned.
   *
   *                 Equations tested: Double "&lt;" &amp; "lt" Double Double "&lt;" &amp;
   *                 "lt" BigInteger Double "&lt;" &amp; "lt" Integer Double "&lt;" &amp; "lt"
   *                 Long Double "&lt;" &amp; "lt" Short Double "&lt;" &amp; "lt" Byte
   */
  @Test
  public void elDoubleLessThanTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Double.valueOf(2.5), Boolean.FALSE, "<");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Double.valueOf(1.0), Boolean.FALSE, "lt");

    // value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(Double.valueOf(-2.5), Boolean.TRUE, "lt");

  }

  /**
   * @testName  elDoubleLessThanEqualTest
   * @assertion_ids  EL:SPEC:21.1; EL:SPEC:21.4
   * @test_Strategy  Validate that if one of the operands in an EL "&lt;=" or "le"
   *                 operation is a Double, the result is coerced to Double and
   *                 the correct boolean value is returned.
   *
   *                 Equations tested: Double "&lt;=" &amp; "le" Double Double "&lt;=" &amp;
   *                 "le" BigInteger Double "&lt;=" &amp; "le" Integer Double "&lt;=" &amp;
   *                 "le" Long Double "&lt;=" &amp; "le" Short Double "&lt;=" &amp; "le" Byte
   */
  @Test
  public void elDoubleLessThanEqualTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Double.valueOf(2.5), Boolean.FALSE, "<=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Double.valueOf(1.0), Boolean.TRUE, "le");

    // Value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(Double.valueOf(-1.5), Boolean.TRUE, "<=");
  }

  /**
   * @testName  elDoubleGreaterThanTest
   * @assertion_ids  EL:SPEC:21.4
   * @test_Strategy  Validate that if one of the operands in an EL ">" or "gt"
   *                 operation is a Double, the result is coerced to Double and
   *                 the correct boolean value is returned.
   *
   *                 Equations tested: Double ">" &amp; "gt" Double Double ">" &amp;
   *                 "gt" BigInteger Double ">" &amp; "gt" Integer Double ">" &amp; "gt"
   *                 Long Double ">" &amp; "gt" Short Double ">" &amp; "gt" Byte
   */
  @Test
  public void elDoubleGreaterThanTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Double.valueOf(10.5), Boolean.TRUE, ">");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Double.valueOf(1.0), Boolean.FALSE, "gt");

    // value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(Double.valueOf(-10.5), Boolean.FALSE, "gt");

  }

  /**
   * @testName  elDoubleGreaterThanEqualTest
   * @assertion_ids  EL:SPEC:21.1; EL:SPEC:21.4
   * @test_Strategy  Validate that if one of the operands in an EL ">=" or "ge"
   *                 operation is a Double, the result is coerced to Double and
   *                 the correct boolean value is returned.
   *
   *                 Equations tested: Double ">=" &amp; "ge" Double Double ">=" &amp;
   *                 "ge" BigInteger Double ">=" &amp; "ge" Integer Double ">=" &amp;
   *                 "ge" Long Double ">=" &amp; "ge" Short Double ">=" &amp; "ge" Byte
   */
  @Test
  public void elDoubleGreaterThanEqualTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Double.valueOf(10.0), Boolean.TRUE, ">=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Double.valueOf(1.0), Boolean.TRUE, "ge");

    // value passed in is smaller than the COMPARATOR.
    this.testOperatorBoolean(Double.valueOf(-10.0), Boolean.FALSE, "ge");

  }

  /**
   * @testName  elDoubleEqualToTest
   * @assertion_ids  EL:SPEC:22.1; EL:SPEC:22.4
   * @test_Strategy  Validate that if one of the operands in an EL "==" or "eq"
   *                 operation is a Double, the result is coerced to Double and
   *                 the correct boolean value is returned.
   *
   *                 Equations tested: Double "==" &amp; "eq" Double Double "==" &amp;
   *                 "eq" BigInteger Double "==" &amp; "eq" Integer Double "==" &amp;
   *                 "eq" Long Double "==" &amp; "eq" Short Double "==" &amp; "eq" Byte
   */
  @Test
  public void elDoubleEqualToTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Double.valueOf(10531), Boolean.FALSE, "==");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Double.valueOf(1.00), Boolean.TRUE, "eq");

  }

  /**
   * @testName  elDoubleNotEqualToTest
   * @assertion_ids  EL:SPEC:22.4
   * @test_Strategy  Validate that if one of the operands in an EL "!=" or "ne"
   *                 operation is a Double, the result is coerced to Double and
   *                 the correct boolean value is returned.
   *
   *                 Equations tested: Double "!=" &amp; "ne" Double Double "!=" &amp;
   *                 "ne" BigInteger Double "!=" &amp; "ne" Integer Double "!=" &amp;
   *                 "ne" Long Double "!=" &amp; "ne" Short Double "!=" &amp; "ne" Byte
   */
  @Test
  public void elDoubleNotEqualToTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Double.valueOf(10531), Boolean.TRUE, "!=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Double.valueOf(1), Boolean.FALSE, "ne");

  }

  /**
   * @testName  elBigIntegerLessThanTest
   * @assertion_ids  EL:SPEC:21.5
   * @test_Strategy  Validate that if one of the operands in an EL "&lt;" or "lt"
   *                 operation is a BigInteger, the result is coerced to
   *                 BigInteger and the correct boolean value is returned.
   *
   *                 Equations tested: BigInteger "&lt;" &amp; "lt" BigInteger
   *                 BigInteger "&lt;" &amp; "lt" Integer BigInteger "&lt;" &amp; "lt" Long
   *                 BigInteger "&lt;" &amp; "lt" Short BigInteger "&lt;" &amp; "lt" Byte
   */
  @Test
  public void elBigIntegerLessThanTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(BigInteger.valueOf(10531), Boolean.FALSE, "<");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(BigInteger.valueOf(1), Boolean.FALSE, "lt");

    // value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(BigInteger.valueOf(-10531), Boolean.TRUE, "lt");

  }

  /**
   * @testName  elBigIntegerLessThanEqualTest
   * @assertion_ids  EL:SPEC:21.1; EL:SPEC:21.5
   * @test_Strategy  Validate that if one of the operands in an EL "&lt;=" or "le"
   *                 operation is a BigInteger, the result is coerced to
   *                 BigInteger and the correct boolean value is returned.
   *
   *                 Equations tested: BigInteger "&lt;=" &amp; "le" BigInteger
   *                 BigInteger "&lt;=" &amp; "le" Integer BigInteger "&lt;=" &amp; "le" Long
   *                 BigInteger "&lt;=" &amp; "le" Short BigInteger "&lt;=" &amp; "le" Byte
   */
  @Test
  public void elBigIntegerLessThanEqualTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(BigInteger.valueOf(10531), Boolean.FALSE, "<=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(BigInteger.valueOf(1), Boolean.TRUE, "le");

    // Value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(BigInteger.valueOf(-10531), Boolean.TRUE, "<=");
  }

  /**
   * @testName  elBigIntegerGreaterThanTest
   * @assertion_ids  EL:SPEC:21.5
   * @test_Strategy  Validate that if one of the operands in an EL ">" or "gt"
   *                 operation is a BigInteger, the result is coerced to
   *                 BigInteger and the correct boolean value is returned.
   *
   *                 Equations tested: BigInteger ">" &amp; "gt" BigInteger
   *                 BigInteger ">" &amp; "gt" Integer BigInteger ">" &amp; "gt" Long
   *                 BigInteger ">" &amp; "gt" Short BigInteger ">" &amp; "gt" Byte
   */
  @Test
  public void elBigIntegerGreaterThanTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(BigInteger.valueOf(10531), Boolean.TRUE, ">");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(BigInteger.valueOf(1), Boolean.FALSE, "gt");

    // value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(BigInteger.valueOf(-10531), Boolean.FALSE, "gt");

  }

  /**
   * @testName  elBigIntegerGreaterThanEqualTest
   * @assertion_ids  EL:SPEC:21.1; EL:SPEC:21.5
   * @test_Strategy  Validate that if one of the operands in an EL ">=" or "ge"
   *                 operation is a BigInteger, the result is coerced to
   *                 BigInteger and the correct boolean value is returned.
   *
   *                 Equations tested: BigInteger ">=" &amp; "ge" BigInteger
   *                 BigInteger ">=" &amp; "ge" Integer BigInteger ">=" &amp; "ge" Long
   *                 BigInteger ">=" &amp; "ge" Short BigInteger ">=" &amp; "ge" Byte
   */
  @Test
  public void elBigIntegerGreaterThanEqualTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(BigInteger.valueOf(10531), Boolean.TRUE, ">=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(BigInteger.valueOf(1), Boolean.TRUE, "ge");

    // value passed in is smaller than the COMPARATOR.
    this.testOperatorBoolean(BigInteger.valueOf(-10531), Boolean.FALSE, "ge");

  }

  /**
   * @testName  elBigIntegerEqualToTest
   * @assertion_ids  EL:SPEC:22.1; EL:SPEC:22.5.1
   * @test_Strategy  Validate that if one of the operands in an EL "==" or "eq"
   *                 operation is a BigInteger, the result is coerced to
   *                 BigInteger and the correct boolean value is returned.
   *
   *                 Equations tested: BigInteger "==" &amp; "eq" BigInteger
   *                 BigInteger "==" &amp; "eq" Integer BigInteger "==" &amp; "eq" Long
   *                 BigInteger "==" &amp; "eq" Short BigInteger "==" &amp; "eq" Byte
   */
  @Test
  public void elBigIntegerEqualToTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(BigInteger.valueOf(10531), Boolean.FALSE, "==");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(BigInteger.valueOf(1), Boolean.TRUE, "eq");

  }

  /**
   * @testName  elBigIntegerNotEqualToTest
   * @assertion_ids  EL:SPEC:22.5.2
   * @test_Strategy  Validate that if one of the operands in an EL "!=" or "ne"
   *                 operation is a BigInteger, the result is coerced to
   *                 BigInteger and the correct boolean value is returned.
   *
   *                 Equations tested: BigInteger "!=" &amp; "ne" BigInteger
   *                 BigInteger "!=" &amp; "ne" Integer BigInteger "!=" &amp; "ne" Long
   *                 BigInteger "!=" &amp; "ne" Short BigInteger "!=" &amp; "ne" Byte
   */
  @Test
  public void elBigIntegerNotEqualToTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(BigInteger.valueOf(10531), Boolean.TRUE, "!=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(BigInteger.valueOf(1), Boolean.FALSE, "ne");

  }

  /**
   * @testName  elLongLessThanTest
   * @assertion_ids  EL:SPEC:21.6
   * @test_Strategy  Validate that if one of the operands in an EL "&lt;" or "lt"
   *                 operation is a Long, the result is coerced to Long and the
   *                 correct boolean value is returned.
   *
   *                 Equations tested: Long "&lt;" &amp; "lt" Integer Long "&lt;" &amp; "lt"
   *                 Long Long "&lt;" &amp; "lt" Short Long "&lt;" &amp; "lt" Byte
   */
  @Test
  public void elLongLessThanTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Long.valueOf(25000), Boolean.FALSE, "<");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Long.valueOf(1), Boolean.FALSE, "<");

    // value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(Long.valueOf(-25000), Boolean.TRUE, "lt");

  }

  /**
   * @testName  elLongLessThanEqualTest
   * @assertion_ids  EL:SPEC:21.1; EL:SPEC:21.6
   * @test_Strategy  Validate that if one of the operands in an EL "&lt;=" or "le"
   *                 operation is a Long, the result is coerced to Long and the
   *                 correct boolean value is returned.
   *
   *                 Equations tested: Long "&lt;=" &amp; "le" Integer Long "&lt;=" &amp; "le"
   *                 Long Long "&lt;=" &amp; "le" Short Long "&lt;=" &amp; "le" Byte
   */
  @Test
  public void elLongLessThanEqualTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Long.valueOf(25000), Boolean.FALSE, "<=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Long.valueOf(1), Boolean.TRUE, "le");

    // Value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(Long.valueOf(-25000), Boolean.TRUE, "<=");
  }

  /**
   * @testName  elLongGreaterThanTest
   * @assertion_ids  EL:SPEC:21.6
   * @test_Strategy  Validate that if one of the operands in an EL ">" or "gt"
   *                 operation is a Long, the result is coerced to Long and the
   *                 correct boolean value is returned.
   *
   *                 Equations tested: Long ">" &amp; "gt" Integer Long ">" &amp; "gt"
   *                 Long Long ">" &amp; "gt" Short Long ">" &amp; "gt" Byte
   */
  @Test
  public void elLongGreaterThanTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Long.valueOf(10531), Boolean.TRUE, ">");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Long.valueOf(1), Boolean.FALSE, ">");

    // value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(Long.valueOf(-10531), Boolean.FALSE, "gt");

  }

  /**
   * @testName  elLongGreaterThanEqualTest
   * @assertion_ids  EL:SPEC:21.1; EL:SPEC:21.6
   * @test_Strategy  Validate that if one of the operands in an EL ">=" or "ge"
   *                 operation is a Long, the result is coerced to Long and the
   *                 correct boolean value is returned.
   *
   *                 Equations tested: Long ">=" &amp; "ge" Integer Long ">=" &amp; "ge"
   *                 Long Long ">=" &amp; "ge" Short Long ">=" &amp; "ge" Byte
   */
  @Test
  public void elLongGreaterThanEqualTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Long.valueOf(25000), Boolean.TRUE, ">=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Long.valueOf(1), Boolean.TRUE, "ge");

    // value passed in is smaller than the COMPARATOR.
    this.testOperatorBoolean(Long.valueOf(-25000), Boolean.FALSE, "ge");

  }

  /**
   * @testName  elLongEqualToTest
   * @assertion_ids  EL:SPEC:22.1; EL:SPEC:22.6
   * @test_Strategy  Validate that if one of the operands in an EL "==" or "eq"
   *                 operation is a Long, the result is coerced to Long and the
   *                 correct boolean value is returned.
   *
   *                 Equations tested: Long "==" &amp; "eq" Integer Long "==" &amp; "eq"
   *                 Long Long "==" &amp; "eq" Short Long "==" &amp; "eq" Byte
   */
  @Test
  public void elLongEqualToTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Long.valueOf(25000), Boolean.FALSE, "==");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Long.valueOf(1), Boolean.TRUE, "eq");

  }

  /**
   * @testName  elLongNotEqualToTest
   * @assertion_ids  EL:SPEC:22.6
   * @test_Strategy  Validate that if one of the operands in an EL "!=" or "ne"
   *                 operation is a Long, the result is coerced to Long and the
   *                 correct boolean value is returned.
   *
   *                 Equations tested: Long "!=" &amp; "ne" Integer Long "!=" &amp; "ne"
   *                 Long Long "!=" &amp; "ne" Short Long "!=" &amp; "ne" Byte
   */
  @Test
  public void elLongNotEqualToTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Long.valueOf(25000), Boolean.TRUE, "!=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Long.valueOf(1), Boolean.FALSE, "ne");

  }

  /**
   * @testName  elIntegerLessThanTest
   * @assertion_ids  EL:SPEC:21.6
   * @test_Strategy  Validate that if one of the operands in an EL "&lt;" or "lt"
   *                 operation is an Integer, the result is coerced to Long and
   *                 the correct boolean value is returned.
   *
   *                 Equations tested: Integer "&lt;" &amp; "lt" Integer Integer "&lt;" &amp;
   *                 "lt" Short Integer "&lt;" &amp; "lt" Byte
   */
  @Test
  public void elIntegerLessThanTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Integer.valueOf(25), Boolean.FALSE, "<");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Integer.valueOf(1), Boolean.FALSE, "lt");

    // value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(Integer.valueOf(-25), Boolean.TRUE, "lt");

  }

  /**
   * @testName  elIntegerLessThanEqualTest
   * @assertion_ids  EL:SPEC:21.1; EL:SPEC:21.6
   * @test_Strategy  Validate that if one of the operands in an EL "&lt;=" or "le"
   *                 operation is an Integer, the result is coerced to Long and
   *                 the correct boolean value is returned.
   *
   *                 Equations tested: Integer "&lt;=" &amp; "le" Integer Integer "&lt;="
   *                 &amp; "le" Short Integer "&lt;=" &amp; "le" Byte
   */
  @Test
  public void elIntegerLessThanEqualTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Integer.valueOf(25), Boolean.FALSE, "<=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Integer.valueOf(1), Boolean.TRUE, "le");

    // Value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(Integer.valueOf(-25), Boolean.TRUE, "<=");
  }

  /**
   * @testName  elIntegerGreaterThanTest
   * @assertion_ids  EL:SPEC:21.6
   * @test_Strategy  Validate that if one of the operands in an EL ">" or "gt"
   *                 operation is an Integer, the result is coerced to Long and
   *                 the correct boolean value is returned.
   *
   *                 Equations tested: Integer ">" &amp; "gt" Integer Integer ">" &amp;
   *                 "gt" Short Integer ">" &amp; "gt" Byte
   */
  @Test
  public void elIntegerGreaterThanTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Integer.valueOf(105), Boolean.TRUE, ">");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Integer.valueOf(1), Boolean.FALSE, "gt");

    // value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(Integer.valueOf(-105), Boolean.FALSE, "gt");

  }

  /**
   * @testName  elIntegerGreaterThanEqualTest
   * @assertion_ids  EL:SPEC:21.1; EL:SPEC:21.6
   * @test_Strategy  Validate that if one of the operands in an EL ">=" or "ge"
   *                 operation is an Integer, the result is coerced to Long and
   *                 the correct boolean value is returned.
   *
   *                 Equations tested: Integer ">=" &amp; "ge" Integer Integer ">="
   *                 &amp; "ge" Short Integer ">=" &amp; "ge" Byte
   */
  @Test
  public void elIntegerGreaterThanEqualTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Integer.valueOf(250), Boolean.TRUE, ">=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Integer.valueOf(1), Boolean.TRUE, "ge");

    // value passed in is smaller than the COMPARATOR.
    this.testOperatorBoolean(Integer.valueOf(-250), Boolean.FALSE, "ge");

  }

  /**
   * @testName  elIntegerEqualToTest
   * @assertion_ids  EL:SPEC:22.1; EL:SPEC:22.6
   * @test_Strategy  Validate that if one of the operands in an EL "==" or "eq"
   *                 operation is an Integer, the result is coerced to Long and
   *                 the correct boolean value is returned.
   *
   *                 Equations tested: Integer "==" &amp; "eq" Integer Integer "=="
   *                 &amp; "eq" Short Integer "==" &amp; "eq" Byte
   */
  @Test
  public void elIntegerEqualToTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Integer.valueOf(25), Boolean.FALSE, "==");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Integer.valueOf(1), Boolean.TRUE, "eq");

  }

  /**
   * @testName  elIntegerNotEqualToTest
   * @assertion_ids  EL:SPEC:22.6
   * @test_Strategy  Validate that if one of the operands in an EL "!=" or "ne"
   *                 operation is an Integer, the result is coerced to Long and
   *                 the correct boolean value is returned.
   *
   *                 Equations tested: Integer "!=" &amp; "ne" Integer Integer "!="
   *                 &amp; "ne" Short Integer "!=" &amp; "ne" Byte
   */
  @Test
  public void elIntegerNotEqualToTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Integer.valueOf(25), Boolean.TRUE, "!=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Integer.valueOf(1), Boolean.FALSE, "ne");

  }

  /**
   * @testName  elShortLessThanTest
   * @assertion_ids  EL:SPEC:21.6
   * @test_Strategy  Validate that if one of the operands in an EL "&lt;" or "lt"
   *                 operation is an Short, the result is coerced to Long and
   *                 the correct boolean value is returned.
   *
   *                 Equations tested: Short "&lt;" &amp; "lt" Short Short "&lt;" &amp; "lt"
   *                 Byte
   */
  @Test
  public void elShortLessThanTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Short.valueOf("2"), Boolean.FALSE, "<");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Short.valueOf("1"), Boolean.FALSE, "<");

    // value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(Short.valueOf("-2"), Boolean.TRUE, "lt");

  }

  /**
   * @testName  elShortLessThanEqualTest
   * @assertion_ids  EL:SPEC:21.1; EL:SPEC:21.6
   * @test_Strategy  Validate that if one of the operands in an EL "&lt;=" or "le"
   *                 operation is an Short, the result is coerced to Long and
   *                 the correct boolean value is returned.
   *
   *                 Equations tested: Short "&lt;=" &amp; "le" Short Short "&lt;=" &amp; "le"
   *                 Byte
   */
  @Test
  public void elShortLessThanEqualTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Short.valueOf("2"), Boolean.FALSE, "<=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Short.valueOf("1"), Boolean.TRUE, "le");

    // Value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(Short.valueOf("-2"), Boolean.TRUE, "<=");
  }

  /**
   * @testName  elShortGreaterThanTest
   * @assertion_ids  EL:SPEC:21.6
   * @test_Strategy  Validate that if one of the operands in an EL ">" or "gt"
   *                 operation is an Short, the result is coerced to Long and
   *                 the correct boolean value is returned.
   *
   *                 Equations tested: Short ">" &amp; "gt" Short Short ">" &amp; "gt"
   *                 Byte
   */
  @Test
  public void elShortGreaterThanTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Short.valueOf("2"), Boolean.TRUE, ">");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Short.valueOf("1"), Boolean.FALSE, ">");

    // value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(Short.valueOf("-2"), Boolean.FALSE, "gt");

  }

  /**
   * @testName  elShortGreaterThanEqualTest
   * @assertion_ids  EL:SPEC:21.1; EL:SPEC:21.6
   * @test_Strategy  Validate that if one of the operands in an EL ">=" or "ge"
   *                 operation is an Short, the result is coerced to Long and
   *                 the correct boolean value is returned.
   *
   *                 Equations tested: Short ">=" &amp; "ge" Short Short ">=" &amp; "ge"
   *                 Byte
   */
  @Test
  public void elShortGreaterThanEqualTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Short.valueOf("2"), Boolean.TRUE, ">=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Short.valueOf("1"), Boolean.TRUE, "ge");

    // value passed in is smaller than the COMPARATOR.
    this.testOperatorBoolean(Short.valueOf("-2"), Boolean.FALSE, "ge");

  }

  /**
   * @testName  elShortEqualToTest
   * @assertion_ids  EL:SPEC:22.1; EL:SPEC:22.6
   * @test_Strategy  Validate that if one of the operands in an EL "==" or "eq"
   *                 operation is an Short, the result is coerced to Long and
   *                 the correct boolean value is returned.
   *
   *                 Equations tested: Short "==" &amp; "eq" Short Short "==" &amp; "eq"
   *                 Byte
   */
  @Test
  public void elShortEqualToTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Short.valueOf("2"), Boolean.FALSE, "==");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Short.valueOf("1"), Boolean.TRUE, "eq");

  }

  /**
   * @testName  elShortNotEqualToTest
   * @assertion_ids  EL:SPEC:22.6
   * @test_Strategy  Validate that if one of the operands in an EL "!=" or "ne"
   *                 operation is an Short, the result is coerced to Long and
   *                 the correct boolean value is returned.
   *
   *                 Equations tested: Short "!=" &amp; "ne" Short Short "!=" &amp; "ne"
   *                 Byte
   */
  @Test
  public void elShortNotEqualToTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Short.valueOf("2"), Boolean.TRUE, "!=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Short.valueOf("1"), Boolean.FALSE, "ne");

  }

  /**
   * @testName  elByteLessThanTest
   * @assertion_ids  EL:SPEC:21.6
   * @test_Strategy  Validate that if one of the operands in an EL "&lt;" or "lt"
   *                 operation is an Byte, the result is coerced to Long and the
   *                 correct boolean value is returned.
   *
   *                 Equations tested: Byte "&lt;" &amp; "lt" Byte
   */
  @Test
  public void elByteLessThanTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Byte.valueOf("2"), Boolean.FALSE, "<");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Byte.valueOf("1"), Boolean.FALSE, "lt");

    // value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(Byte.valueOf("-2"), Boolean.TRUE, "lt");

  }

  /**
   * @testName  elByteLessThanEqualTest
   * @assertion_ids  EL:SPEC:21.1; EL:SPEC:21.6
   * @test_Strategy  Validate that if one of the operands in an EL "&lt;=" or "le"
   *                 operation is an Byte, the result is coerced to Long and the
   *                 correct boolean value is returned.
   *
   *                 Equations tested: Byte "&lt;=" &amp; "le" Byte
   */
  @Test
  public void elByteLessThanEqualTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Byte.valueOf("2"), Boolean.FALSE, "<=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Byte.valueOf("1"), Boolean.TRUE, "le");

    // Value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(Byte.valueOf("-2"), Boolean.TRUE, "<=");
  }

  /**
   * @testName  elByteGreaterThanTest
   * @assertion_ids  EL:SPEC:21.1
   * @test_Strategy  Validate that if one of the operands in an EL ">" or "gt"
   *                 operation is an Byte, the result is coerced to Long and the
   *                 correct boolean value is returned.
   *
   *                 Equations tested: Byte ">" &amp; "gt" Byte
   */
  @Test
  public void elByteGreaterThanTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Byte.valueOf("2"), Boolean.TRUE, ">");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Byte.valueOf("1"), Boolean.FALSE, "gt");

    // value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(Byte.valueOf("-2"), Boolean.FALSE, "gt");

  }

  /**
   * @testName  elByteGreaterThanEqualTest
   * @assertion_ids  EL:SPEC:21.1; EL:SPEC:21.6
   * @test_Strategy  Validate that if one of the operands in an EL ">=" or "ge"
   *                 operation is an Byte, the result is coerced to Long and the
   *                 correct boolean value is returned.
   *
   *                 Equations tested: Byte ">=" &amp; "ge" Byte
   */
  @Test
  public void elByteGreaterThanEqualTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Byte.valueOf("2"), Boolean.TRUE, ">=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Byte.valueOf("1"), Boolean.TRUE, "ge");

    // value passed in is smaller than the COMPARATOR.
    this.testOperatorBoolean(Byte.valueOf("-2"), Boolean.FALSE, "ge");

  }

  /**
   * @testName  elByteEqualToTest
   * @assertion_ids  EL:SPEC:22.1; EL:SPEC:22.6
   * @test_Strategy  Validate that if one of the operands in an EL "==" or "eq"
   *                 operation is an Byte, the result is coerced to Long and the
   *                 correct boolean value is returned.
   *
   *                 Equations tested: Byte "==" &amp; "eq" Byte
   */
  @Test
  public void elByteEqualToTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Byte.valueOf("2"), Boolean.FALSE, "==");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Byte.valueOf("1"), Boolean.TRUE, "eq");

  }

  /**
   * @testName  elByteNotEqualToTest
   * @assertion_ids  EL:SPEC:22.6
   * @test_Strategy  Validate that if one of the operands in an EL "!=" or "ne"
   *                 operation is an Byte, the result is coerced to Long and the
   *                 correct boolean value is returned.
   *
   *                 Equations tested: Byte "!=" &amp; "ne" Byte
   */
  @Test
  public void elByteNotEqualToTest() throws Exception {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Byte.valueOf("2"), Boolean.TRUE, "!=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Byte.valueOf("1"), Boolean.FALSE, "ne");

  }

  /**
   * @testName  elStringLessThanTest
   * @assertion_ids  EL:SPEC:21.7
   * @test_Strategy  Validate that if one of the operands in an EL "&lt;" or "lt"
   *                 operation is an String, that both operands are coerced to
   *                 type String and the correct boolean value is returned.
   *
   *                 Equations tested: String "&lt;" &amp; "lt" String
   */
  @Test
  public void elStringLessThanTest() throws Exception {

    // Value A is less than value B. (true)
    this.testOperatorBoolean("Alpha", DT, Boolean.TRUE, "<");

    // Value A is less than value B. (false)
    this.testOperatorBoolean("Beta", DT, Boolean.FALSE, "lt");

    // Value A is less than value B. (false)
    this.testOperatorBoolean("Gamma", DT, Boolean.FALSE, "lt");

  }

  /**
   * @testName  elStringLessThanEqualTest
   * @assertion_ids  EL:SPEC:21.7
   * @test_Strategy  Validate that if one of the operands in an EL "&lt;=" or "le"
   *                 operation is a String, that both operands are coerced to
   *                 type String and the correct boolean value is returned.
   *
   *                 Equations tested: String "&lt;=" &amp; "le" String
   */
  @Test
  public void elStringLessThanEqualTest() throws Exception {

    // Value A is less than or equal to value B. (false)
    this.testOperatorBoolean("Gamma", DT, Boolean.FALSE, "<=");

    // Value A is less than or equal to value B. (true)
    this.testOperatorBoolean("Beta", DT, Boolean.TRUE, "le");

    // Value A is less than or equal to value B. (true)
    this.testOperatorBoolean("Alpha", DT, Boolean.TRUE, "<=");
  }

  /**
   * @testName  elStringGreaterThanTest
   * @assertion_ids  EL:SPEC:21.7
   * @test_Strategy  Validate that if one of the operands in an EL ">" or "gt"
   *                 operation is an String, that both operands are coerced to
   *                 type String and the correct boolean value is returned.
   *
   *                 Equations tested: String ">" &amp; "gt" String
   */
  @Test
  public void elStringGreaterThanTest() throws Exception {

    // Value A is greater than value B. (true)
    this.testOperatorBoolean("Gamma", DT, Boolean.TRUE, ">");

    // Value A greater than value B. (false)
    this.testOperatorBoolean("Beta", DT, Boolean.FALSE, "gt");

    // Value A is greater than value B. (false)
    this.testOperatorBoolean("Alpha", DT, Boolean.FALSE, "gt");

  }

  /**
   * @testName  elStringGreaterThanEqualTest
   * @assertion_ids  EL:SPEC:21.7
   * @test_Strategy  Validate that if one of the operands in an EL ">=" or "ge"
   *                 operation is an String, that both operands are coerced to
   *                 type String and the correct boolean value is returned.
   *
   *                 Equations tested: String ">=" &amp; "ge" String
   */
  @Test
  public void elStringGreaterThanEqualTest() throws Exception {

    // Value A is greater than or equal to value B. (true)
    this.testOperatorBoolean("Gamma", DT, Boolean.TRUE, ">=");

    // Value A is greater than or equal to value B. (true)
    this.testOperatorBoolean("Beta", DT, Boolean.TRUE, "ge");

    // Value A is greater than or equal to value B. (false)
    this.testOperatorBoolean("Alpha", DT, Boolean.FALSE, "ge");

  }

  /**
   * @testName  elStringEqualToTest
   * @assertion_ids  EL:SPEC:22.9
   * @test_Strategy  Validate that if one of the operands in an EL "==" or "eq"
   *                 operation is an String, that both operands are coerced to
   *                 type String and the correct boolean value is returned.
   *
   *                 Equations tested: String "==" &amp; "eq" String
   */
  @Test
  public void elStringEqualToTest() throws Exception {

    // Value A is equal to value B. (true)
    this.testOperatorBoolean("Beta", DT, Boolean.TRUE, "==");

    // Value A is smaller than value B. (false)
    this.testOperatorBoolean("Alpha", DT, Boolean.FALSE, "eq");

  }

  /**
   * @testName  elStringNotEqualToTest
   * @assertion_ids  EL:SPEC:22.9
   * @test_Strategy  Validate that if one of the operands in an EL "!=" or "ne"
   *                 operation is an String, that both operands are coerced to
   *                 type String and the correct boolean value is returned.
   *
   *                 Equations tested: String "!=" &amp; "ne" String
   */
  @Test
  public void elStringNotEqualToTest() throws Exception {

    // Value A is not equal to value B. (true)
    this.testOperatorBoolean("Alpha", DT, Boolean.TRUE, "!=");

    // Value A is not equal to value B. (false)
    this.testOperatorBoolean("Beta", DT, Boolean.FALSE, "ne");

  }

  /**
   * @testName  elOtherLessThanTest
   * @assertion_ids  EL:SPEC:21.8.2
   * @test_Strategy  Validate that if operand A in an EL "&lt;" or "lt" operation
   *                 is comparable, the result A.compareTo(B) is returned.
   *
   *                 Equation example: DougType "&lt;" &amp; "lt" NickType
   */
  @Test
  public void elOtherLessThanTest() throws Exception {

    // Value A is less than value B. (true)
    this.testOperatorBoolean(DT, NT, Boolean.TRUE, "<");

    // Value A is less than value B. (false)
    this.testOperatorBoolean(DT, DT, Boolean.FALSE, "lt");

    // Value A is less than value B. (false)
    this.testOperatorBoolean(NT, DT, Boolean.FALSE, "lt");

  }

  /**
   * @testName  elOtherLessThanEqualTest
   * @assertion_ids  EL:SPEC:21.8.2
   * @test_Strategy  Validate that if operand A in an EL "&lt;=" or "le" operation
   *                 is comparable, the result A.compareTo(B) is returned.
   *
   *                 Equation example: DougType "&lt;=" &amp; "le" NickType
   */
  @Test
  public void elOtherLessThanEqualTest() throws Exception {

    // Value A is less than or equal to value B. (false)
    this.testOperatorBoolean(NT, DT, Boolean.FALSE, "<=");

    // Value A is less than or equal to value B. (true)
    this.testOperatorBoolean(DT, DT, Boolean.TRUE, "le");

    // Value A is less than or equal to value B. (true)
    this.testOperatorBoolean(DT, NT, Boolean.TRUE, "<=");
  }

  /**
   * @testName  elOtherGreaterThanTest
   * @assertion_ids  EL:SPEC:21.8.2
   * @test_Strategy  Validate that if operand A in an EL ">" or "gt" operation
   *                 is comparable, the result A.compareTo(B) is returned.
   *
   *                 Equation example: DougType "&lt;" &amp; "gt" NickType
   */
  @Test
  public void elOtherGreaterThanTest() throws Exception {

    // Value A is greater than value B. (true)
    this.testOperatorBoolean(NT, DT, Boolean.TRUE, ">");

    // Value A greater than value B. (false)
    this.testOperatorBoolean(DT, DT, Boolean.FALSE, "gt");

    // Value A is greater than value B. (false)
    this.testOperatorBoolean(DT, NT, Boolean.FALSE, "gt");

  }

  /**
   * @testName  elOtherGreaterThanEqualTest
   * @assertion_ids  EL:SPEC:21.8.2
   * @test_Strategy  Validate that if operand A in an EL ">=" or "ge" operation
   *                 is comparable, the result A.compareTo(B) is returned.
   *
   *                 Equations tested: DougType ">=" &amp; "ge" NickType
   */
  @Test
  public void elOtherGreaterThanEqualTest() throws Exception {

    // Value A is greater than or equal to value B. (true)
    this.testOperatorBoolean(NT, DT, Boolean.TRUE, ">=");

    // Value A is greater than or equal to value B. (true)
    this.testOperatorBoolean(DT, DT, Boolean.TRUE, "ge");

    // Value A is greater than or equal to value B. (false)
    this.testOperatorBoolean(DT, NT, Boolean.FALSE, "ge");

  }

  /**
   * @testName  elOtherEqualToTest
   * @assertion_ids  EL:SPEC:22.11
   * @test_Strategy  Validate that if operand A in an EL "==" or "eq" operation
   *                 is comparable, the result A.equals(B) is returned.
   *
   *                 Equations Example: DougType "==" &amp; "eq" NickType
   */
  @Test
  public void elOtherEqualToTest() throws Exception {

    // Value A is equal to value B. (true)
    this.testOperatorBoolean(DT, DT, Boolean.TRUE, "==");

    // Value A is smaller than value B. (false)
    this.testOperatorBoolean(DT, NT, Boolean.FALSE, "eq");

  }

  /**
   * @testName  elOtherNotEqualToTest
   * @assertion_ids  EL:SPEC:22.11
   * @test_Strategy  Validate that if operand A in an EL "!=" or "ne" operation
   *                 is comparable, the result A.equals(B) is returned.
   *
   *                 Equation Example: DougType "!=" &amp; "ne" NickType
   */
  @Test
  public void elOtherNotEqualToTest() throws Exception {

    // Value A is not equal to value B. (true)
    this.testOperatorBoolean(DT, NT, Boolean.TRUE, "!=");

    // Value A is not equal to value B. (false)
    this.testOperatorBoolean(DT, DT, Boolean.FALSE, "ne");

  }

  /**
   * @testName  elBooleanEqualToTest
   * @assertion_ids  EL:SPEC:22.7
   * @test_Strategy  Validate that if one of the operands in an EL "==" or "eq"
   *                 operation is an Boolean, that both operands are coerced to
   *                 type Boolean and the correct boolean value is returned.
   *
   *                 Equations tested: Boolean "==" &amp; "eq" String
   */
  @Test
  public void elBooleanEqualToTest() throws Exception {

    // Value A is equal to value B. (true)
    this.testOperatorBoolean("true", Boolean.TRUE, Boolean.TRUE, "==");

    // Value A is smaller than value B. (false)
    this.testOperatorBoolean("false", Boolean.TRUE, Boolean.FALSE, "eq");

  }

  /**
   * @testName  elBooleanNotEqualToTest
   * @assertion_ids  EL:SPEC:22.7
   * @test_Strategy  Validate that if one of the operands in an EL "!=" or "ne"
   *                 operation is an Boolean, that both operands are coerced to
   *                 type Boolean and the correct boolean value is returned.
   *
   *                 Equations tested: String "!=" &amp; "ne" String
   */
  @Test
  public void elBooleanNotEqualToTest() throws Exception {

    // Value A is not equal to value B. (true)
    this.testOperatorBoolean("false", Boolean.TRUE, Boolean.TRUE, "!=");

    // Value A is not equal to value B. (false)
    this.testOperatorBoolean("false", Boolean.FALSE, Boolean.FALSE, "ne");

  }

  /**
   * @testName  elEnumEqualToTest
   * @assertion_ids  EL:SPEC:22.8
   * @test_Strategy  Validate that if one of the operands in an EL "==" or "eq"
   *                 operation is an Enum, that both operands are coerced to
   *                 type Enum and the correct boolean value is returned.
   *
   *                 Example Equation: Enum "==" String or Integer
   */
  @Test
  public void elEnumEqualToTest() throws Exception {

    // Value A is equal to value B. (true)
    this.testOperatorBoolean(TestEnum.APPLE, "APPLE", Boolean.TRUE, "==");
    this.testOperatorBoolean(TestEnum.PEAR, "PEAR", Boolean.TRUE, "eq");

    // Value A is not equal to value B. (false)
    this.testOperatorBoolean(TestEnum.PEAR, "APPLE", Boolean.FALSE, "==");
    this.testOperatorBoolean(TestEnum.APPLE, "PEAR", Boolean.FALSE, "eq");
  }

  /**
   * @testName  elEnumNotEqualToTest
   * @assertion_ids  EL:SPEC:22.8
   * @test_Strategy  Validate that if one of the operands in an EL "!=" or "ne"
   *                 operation is an Enum, that both operands are coerced to
   *                 type String and the correct boolean value is returned.
   *
   *                 Example Equation: Enum "!=" &amp; "ne" Enum
   */
  @Test
  public void elEnumNotEqualToTest() throws Exception {

    // Value A is not equal to value B. (true)
    this.testOperatorBoolean(TestEnum.APPLE, "PEAR", Boolean.TRUE, "!=");
    this.testOperatorBoolean(TestEnum.PEAR, "APPLE", Boolean.TRUE, "ne");

    // Value A is not equal to value B. (false)
    this.testOperatorBoolean(TestEnum.APPLE, "APPLE", Boolean.FALSE, "!=");
    this.testOperatorBoolean(TestEnum.PEAR, "PEAR", Boolean.FALSE, "ne");
  }

  // ---------------------------------------------------------- private
  // methods

  /**
   * This method is used to validate an expression that has at least one
   * BigDecimal in it. We pass in one of the operands(testVal), the other
   * operand is automatically picked up from the numberList.
   *
   * @param testVal
   *          - One of the operands used in the expression.
   * @param expectedVal
   *          - expected result.
   * @param booleanOperator
   *          - The operator in which the operands are compared. (i.e. ">, >=,
   *          &lt;, &lt;=, gt, ge, lt, le, ==, !=, eq, ne)
   */
  private void testOperatorBoolean(BigDecimal testVal, Boolean expectedVal,
      String booleanOperator) throws Exception {

    boolean pass;

    for (int i = 0; numberList.size() > i; i++) {
      logger.log(Logger.Level.TRACE,
          "*** Start " + "\"" + "BigDecimal" + "\"" + " Test Sequence ***");

      Object testNum = numberList.get(i);

      // We don't test numeric strings
      if (!(testNum instanceof Number))
        continue;

      NameValuePair values[] = NameValuePair.buildNameValuePair(testVal,
          testNum);

      try {
        String expr = ExprEval.buildElExpr(false, booleanOperator);
        logger.log(Logger.Level.TRACE, "expression to be evaluated is " + expr);
        logger.log(Logger.Level.TRACE, "types are " + "BigDecimal" + " and "
            + testNum.getClass().getName());

        Object result = ExprEval.evaluateValueExpression(expr, values,
            Object.class);

        logger.log(Logger.Level.TRACE, "result is " + result.toString());

        pass = (ExprEval.compareClass(result, Boolean.class)
            && (((Boolean) result).equals(expectedVal)));

      } catch (Exception e) {
        throw new Exception(e);

      } finally {
        ExprEval.cleanup();
        logger.log(Logger.Level.TRACE,
            "*** End " + "\"" + "BigDecimal" + "\"" + " Test Sequence ***");
      }

      if (!pass)
        throw new Exception("TEST FAILED: pass = false");

    }
  }

  /**
   * This method is used to validate an expression that has at least one
   * BigInteger in it. We pass in one of the operands(testVal), the other
   * operand is automatically picked up from the numberList.
   *
   * @param testVal
   *          - One of the operands used in the expression.
   * @param expectedVal
   *          - expected result.
   * @param booleanOperator
   *          - The operator in which the operands are compared. (i.e. >, >=, &lt;,
   *          &lt;=, gt, ge, lt, le, ==, !=, eq, ne)
   */
  private void testOperatorBoolean(BigInteger testVal, Boolean expectedVal,
      String booleanOperator) throws Exception {

    boolean pass;

    for (int i = 0; numberList.size() > i; i++) {
      logger.log(Logger.Level.TRACE,
          "*** Start " + "\"" + "BigInteger" + "\"" + " Test Sequence ***");

      Object testNum = numberList.get(i);

      // We don't test numeric strings
      if (!(testNum instanceof Number))
        continue;

      if ((testNum instanceof BigDecimal) || (testNum instanceof Float)
          || (testNum instanceof Double)) {
        String skipType = testNum.getClass().getSimpleName();
        logger.log(Logger.Level.TRACE, "Skip " + skipType + " Data type already "
            + "tested for this in the " + skipType + " tests.");
        continue;
      }

      NameValuePair values[] = NameValuePair.buildNameValuePair(testVal,
          testNum);

      try {
        String expr = ExprEval.buildElExpr(false, booleanOperator);
        logger.log(Logger.Level.TRACE, "expression to be evaluated is " + expr);
        logger.log(Logger.Level.TRACE, "types are " + "BigInteger" + " and "
            + testNum.getClass().getName());

        Object result = ExprEval.evaluateValueExpression(expr, values,
            Object.class);

        logger.log(Logger.Level.TRACE, "result is " + result.toString());

        pass = (ExprEval.compareClass(result, Boolean.class)
            && (((Boolean) result).equals(expectedVal)));

      } catch (Exception e) {
        throw new Exception(e);

      } finally {
        ExprEval.cleanup();
        logger.log(Logger.Level.TRACE,
            "*** End " + "\"" + "BigInteger" + "\"" + " Test Sequence ***");
      }

      if (!pass)
        throw new Exception("TEST FAILED: pass = false");

    }
  }

  /**
   * This method is used to validate an expression that has at least one Float
   * in it. We pass in one of the operands(testVal), the other operand is
   * automatically picked up from the numberList.
   *
   * @param testVal
   *          - One of the operands used in the expression.
   * @param expectedVal
   *          - expected result.
   * @param booleanOperator
   *          - The operator in which the operands are compared. (i.e. ">, >=,
   *          &lt;, &lt;=, gt, ge, lt, le, ==, !=, eq, ne)
   */
  private void testOperatorBoolean(Float testVal, Boolean expectedVal,
      String booleanOperator) throws Exception {

    boolean pass;

    for (int i = 0; numberList.size() > i; i++) {
      logger.log(Logger.Level.TRACE,
          "*** Start " + "\"" + "Float" + "\"" + " Test Sequence ***");

      Object testNum = numberList.get(i);

      // We don't test numeric strings
      if (!(testNum instanceof Number))
        continue;

      if ((testNum instanceof BigDecimal)) {
        String skipType = testNum.getClass().getSimpleName();
        logger.log(Logger.Level.TRACE, "Skip " + skipType + " Data type already "
            + "tested for this in the " + skipType + " tests.");
        continue;
      }

      NameValuePair values[] = NameValuePair.buildNameValuePair(testVal,
          testNum);

      try {
        String expr = ExprEval.buildElExpr(false, booleanOperator);
        logger.log(Logger.Level.TRACE, "expression to be evaluated is " + expr);
        logger.log(Logger.Level.TRACE,
            "types are " + "Float" + " and " + testNum.getClass().getName());

        Object result = ExprEval.evaluateValueExpression(expr, values,
            Object.class);

        logger.log(Logger.Level.TRACE, "result is " + result.toString());

        pass = (ExprEval.compareClass(result, Boolean.class)
            && (((Boolean) result).equals(expectedVal)));

      } catch (Exception e) {
        throw new Exception(e);

      } finally {
        ExprEval.cleanup();
        logger.log(Logger.Level.TRACE,
            "*** End " + "\"" + "Float" + "\"" + " Test Sequence ***");
      }

      if (!pass)
        throw new Exception("TEST FAILED: pass = false");

    }
  }

  /**
   * This method is used to validate an expression that has at least one Double
   * in it. We pass in one of the operands(testVal), the other operand is
   * automatically picked up from the numberList.
   *
   * @param testVal
   *          - One of the operands used in the expression.
   * @param expectedVal
   *          - expected result.
   * @param booleanOperator
   *          - The operator in which the operands are compared. (i.e. ">, >=,
   *          &lt;, &lt;=, gt, ge, lt, le, ==, !=, eq, ne)
   */
  private void testOperatorBoolean(Double testVal, Boolean expectedVal,
      String booleanOperator) throws Exception {

    boolean pass;

    for (int i = 0; numberList.size() > i; i++) {
      logger.log(Logger.Level.TRACE,
          "*** Start " + "\"" + "Double" + "\"" + " Test Sequence ***");

      Object testNum = numberList.get(i);

      // We don't test numeric strings
      if (!(testNum instanceof Number))
        continue;

      if ((testNum instanceof BigDecimal || testNum instanceof Float)) {
        String skipType = testNum.getClass().getSimpleName();
        logger.log(Logger.Level.TRACE, "Skip " + skipType + " Data type already "
            + "tested for this in the " + skipType + " tests.");
        continue;
      }

      NameValuePair values[] = NameValuePair.buildNameValuePair(testVal,
          testNum);

      try {
        String expr = ExprEval.buildElExpr(false, booleanOperator);
        logger.log(Logger.Level.TRACE, "expression to be evaluated is " + expr);
        logger.log(Logger.Level.TRACE,
            "types are " + "Double" + " and " + testNum.getClass().getName());

        Object result = ExprEval.evaluateValueExpression(expr, values,
            Object.class);

        logger.log(Logger.Level.TRACE, "result is " + result.toString());

        pass = (ExprEval.compareClass(result, Boolean.class)
            && (((Boolean) result).equals(expectedVal)));

      } catch (Exception e) {
        throw new Exception(e);

      } finally {
        ExprEval.cleanup();
        logger.log(Logger.Level.TRACE,
            "*** End " + "\"" + "Double" + "\"" + " Test Sequence ***");
      }

      if (!pass)
        throw new Exception("TEST FAILED: pass = false");

    }
  }

  /**
   * This method is used to validate an expression that has at least one Long in
   * it. We pass in one of the operands(testVal), the other operand is
   * automatically picked up from the numberList.
   *
   * @param testVal
   *          - One of the operands used in the expression.
   * @param expectedVal
   *          - expected result.
   * @param booleanOperator
   *          - The operator in which the operands are compared. (i.e. ">, >=,
   *          &lt;, &lt;=, gt, ge, lt, le, ==, !=, eq, ne)
   */
  private void testOperatorBoolean(Long testVal, Boolean expectedVal,
      String booleanOperator) throws Exception {

    boolean pass;

    for (int i = 0; numberList.size() > i; i++) {
      logger.log(Logger.Level.TRACE, "*** Start " + "\"" + "Long" + "\"" + " Test Sequence ***");

      Object testNum = numberList.get(i);

      // We don't test numeric strings
      if (!(testNum instanceof Number))
        continue;

      if ((testNum instanceof BigDecimal || testNum instanceof Float
          || testNum instanceof BigInteger || testNum instanceof Double)) {
        String skipType = testNum.getClass().getSimpleName();
        logger.log(Logger.Level.TRACE, "Skip " + skipType + " Data type already "
            + "tested for this in the " + skipType + " tests.");
        continue;
      }

      NameValuePair values[] = NameValuePair.buildNameValuePair(testVal,
          testNum);

      try {
        String expr = ExprEval.buildElExpr(false, booleanOperator);
        logger.log(Logger.Level.TRACE, "expression to be evaluated is " + expr);
        logger.log(Logger.Level.TRACE,
            "types are " + "Long" + " and " + testNum.getClass().getName());

        Object result = ExprEval.evaluateValueExpression(expr, values,
            Object.class);

        logger.log(Logger.Level.TRACE, "result is " + result.toString());

        pass = (ExprEval.compareClass(result, Boolean.class)
            && (((Boolean) result).equals(expectedVal)));

      } catch (Exception e) {
        throw new Exception(e);

      } finally {
        ExprEval.cleanup();
        logger.log(Logger.Level.TRACE, "*** End " + "\"" + "Long" + "\"" + " Test Sequence ***");
      }

      if (!pass)
        throw new Exception("TEST FAILED: pass = false");

    }
  }

  /**
   * This method is used to validate an expression that has at least one Integer
   * in it. We pass in one of the operands(testVal), the other operand is
   * automatically picked up from the numberList.
   *
   * @param testVal
   *          - One of the operands used in the expression.
   * @param expectedVal
   *          - expected result.
   * @param booleanOperator
   *          - The operator in which the operands are compared. (i.e. ">, >=,
   *          &lt;, &lt;=, gt, ge, lt, le, ==, !=, eq, ne)
   */
  private void testOperatorBoolean(Integer testVal, Boolean expectedVal,
      String booleanOperator) throws Exception {

    boolean pass;

    for (int i = 0; numberList.size() > i; i++) {
      logger.log(Logger.Level.TRACE,
          "*** Start " + "\"" + "Integer" + "\"" + " Test Sequence ***");

      Object testNum = numberList.get(i);

      // We don't test numeric strings
      if (!(testNum instanceof Number))
        continue;

      if ((testNum instanceof BigDecimal || testNum instanceof Float
          || testNum instanceof BigInteger || testNum instanceof Long
          || testNum instanceof Double)) {
        String skipType = testNum.getClass().getSimpleName();
        logger.log(Logger.Level.TRACE, "Skip " + skipType + " Data type already "
            + "tested for this in the " + skipType + " tests.");
        continue;
      }

      NameValuePair values[] = NameValuePair.buildNameValuePair(testVal,
          testNum);

      try {
        String expr = ExprEval.buildElExpr(false, booleanOperator);
        logger.log(Logger.Level.TRACE, "expression to be evaluated is " + expr);
        logger.log(Logger.Level.TRACE,
            "types are " + "Integer" + " and " + testNum.getClass().getName());

        Object result = ExprEval.evaluateValueExpression(expr, values,
            Object.class);

        logger.log(Logger.Level.TRACE, "result is " + result.toString());

        pass = (ExprEval.compareClass(result, Boolean.class)
            && (((Boolean) result).equals(expectedVal)));

      } catch (Exception e) {
        throw new Exception(e);

      } finally {
        ExprEval.cleanup();
        logger.log(Logger.Level.TRACE,
            "*** End " + "\"" + "Integer" + "\"" + " Test Sequence ***");
      }

      if (!pass)
        throw new Exception("TEST FAILED: pass = false");

    }
  }

  /**
   * This method is used to validate an expression that has at least one Short
   * in it. We pass in one of the operands(testVal), the other operand is
   * automatically picked up from the numberList.
   *
   * @param testVal
   *          - One of the operands used in the expression.
   * @param expectedVal
   *          - expected result.
   * @param booleanOperator
   *          - The operator in which the operands are compared. (i.e. ">, >=,
   *          &lt;, &lt;=, gt, ge, lt, le, ==, !=, eq, ne)
   */
  private void testOperatorBoolean(Short testVal, Boolean expectedVal,
      String booleanOperator) throws Exception {

    boolean pass;

    for (int i = 0; numberList.size() > i; i++) {
      logger.log(Logger.Level.TRACE,
          "*** Start " + "\"" + "Short" + "\"" + " Test Sequence ***");

      Object testNum = numberList.get(i);

      if (!(testNum instanceof Short || testNum instanceof Byte)) {
        String skipType = testNum.getClass().getSimpleName();
        logger.log(Logger.Level.TRACE, "Skip " + skipType + " Data type already "
            + "tested for this in the " + skipType + " tests.");
        continue;
      }

      NameValuePair values[] = NameValuePair.buildNameValuePair(testVal,
          testNum);

      try {
        String expr = ExprEval.buildElExpr(false, booleanOperator);
        logger.log(Logger.Level.TRACE, "expression to be evaluated is " + expr);
        logger.log(Logger.Level.TRACE,
            "types are " + "Short" + " and " + testNum.getClass().getName());

        Object result = ExprEval.evaluateValueExpression(expr, values,
            Object.class);

        logger.log(Logger.Level.TRACE, "result is " + result.toString());

        pass = (ExprEval.compareClass(result, Boolean.class)
            && (((Boolean) result).equals(expectedVal)));

      } catch (Exception e) {
        throw new Exception(e);

      } finally {
        ExprEval.cleanup();
        logger.log(Logger.Level.TRACE,
            "*** End " + "\"" + "Short" + "\"" + " Test Sequence ***");
      }

      if (!pass)
        throw new Exception("TEST FAILED: pass = false");

    }
  }

  /**
   * This method is used to validate an expression that has at least one Byte in
   * it. We pass in one of the operands(testVal), the other operand is
   * automatically picked up from the numberList.
   *
   * @param testVal
   *          - One of the operands used in the expression.
   * @param expectedVal
   *          - expected result.
   * @param booleanOperator
   *          - The operator in which the operands are compared. (i.e. >, >=, &lt;,
   *          &lt;=, gt, ge, lt, le, ==, !=, eq, ne)
   */
  private void testOperatorBoolean(Byte testVal, Boolean expectedVal,
      String booleanOperator) throws Exception {

    boolean pass;

    for (int i = 0; numberList.size() > i; i++) {
      logger.log(Logger.Level.TRACE, "*** Start " + "\"" + "Byte" + "\"" + " Test Sequence ***");

      Object testNum = numberList.get(i);

      if (!(testNum instanceof Byte)) {
        String skipType = testNum.getClass().getSimpleName();
        logger.log(Logger.Level.TRACE, "Skip " + skipType + " Data type already "
            + "tested for this in the " + skipType + " tests.");
        continue;
      }

      NameValuePair values[] = NameValuePair.buildNameValuePair(testVal,
          testNum);

      try {
        String expr = ExprEval.buildElExpr(false, booleanOperator);
        logger.log(Logger.Level.TRACE, "expression to be evaluated is " + expr);
        logger.log(Logger.Level.TRACE,
            "types are " + "Byte" + " and " + testNum.getClass().getName());

        Object result = ExprEval.evaluateValueExpression(expr, values,
            Object.class);

        logger.log(Logger.Level.TRACE, "result is " + result.toString());

        pass = (ExprEval.compareClass(result, Boolean.class)
            && (((Boolean) result).equals(expectedVal)));

      } catch (Exception e) {
        throw new Exception(e);

      } finally {
        ExprEval.cleanup();
        logger.log(Logger.Level.TRACE, "*** End " + "\"" + "Byte" + "\"" + " Test Sequence ***");
      }

      if (!pass)
        throw new Exception("TEST FAILED: pass = false");

    }
  }

  /**
   * This method is used to validate an expression that has at least one String
   * in it. We pass in one of the operands(testVal), the other operand is
   * automatically picked up from the numberList.
   *
   * @param testVal
   *          - One of the operands used in the expression.
   * @param expectedVal
   *          - expected result.
   * @param booleanOperator
   *          - The operator in which the operands are compared. (i.e. ">, >=,
   *          &lt;, &lt;=, gt, ge, lt, le, ==, !=, eq, ne)
   */
  private void testOperatorBoolean(Object testValOne, Object testValTwo,
      Boolean expectedVal, String booleanOperator) throws Exception {

    boolean pass;

    NameValuePair values[] = NameValuePair.buildNameValuePair(testValOne,
        testValTwo);

    try {
      logger.log(Logger.Level.TRACE,
          "*** Start " + "\"" + "String" + "\"" + " Test Sequence ***");

      String expr = ExprEval.buildElExpr(false, booleanOperator);
      logger.log(Logger.Level.TRACE, "expression to be evaluated is " + expr);
      logger.log(Logger.Level.TRACE, "types are " + "String and String");

      Object result = ExprEval.evaluateValueExpression(expr, values,
          Object.class);

      logger.log(Logger.Level.TRACE, "result is " + result.toString());

      pass = (ExprEval.compareClass(result, Boolean.class)
          && (((Boolean) result).equals(expectedVal)));

    } catch (Exception e) {
      throw new Exception(e);

    } finally {
      ExprEval.cleanup();
      logger.log(Logger.Level.TRACE, "*** End " + "\"" + "String" + "\"" + " Test Sequence ***");
    }

    if (!pass)
      throw new Exception("TEST FAILED: pass = false");

  }

  // ---------------------------------------------------- Inner Classes

  private static class DougType implements Comparable<Object> {

    @Override
    public String toString() {

      return "Beta";
    }

    @Override
    public int compareTo(Object o) {

      if (o == null)
        return -1;
      return this.toString().compareTo(o.toString());
    }

    @Override
    public boolean equals(Object o) {

      // test for null.
      if (o == null)
        return false;

      /*
       * Since all DougType are staticly set to "Beta" All DougTypes are
       * considered equal, and any other object is not.
       */
      return (o instanceof DougType);

    }

    @Override
    public int hashCode() {
      return 42;
    }

  }

  private static class NickType implements Comparable<Object> {

    @Override
    public String toString() {
      return "Gamma";
    }

    @Override
    public int compareTo(Object o) {

      if (o == null)
        return -1;
      return this.toString().compareTo(o.toString());
    }

    @Override
    public boolean equals(Object o) {

      // test for null.
      if (o == null)
        return false;

      /*
       * Since all NickType are statically set to "Gamma" All NickTypes are
       * considered equal, and any other object is not.
       */
      return (o instanceof NickType);

    }

    @Override
    public int hashCode() {
      return 42;
    }

  }

}
