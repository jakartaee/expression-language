package org.glassfish.el.test;

import jakarta.el.ELManager;
import jakarta.el.ELProcessor;
import jakarta.el.ValueExpression;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EscapingTest {

    static ELProcessor elp;
    static ELManager elm;

    public EscapingTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        elp = new ELProcessor();
        elm = elp.getELManager();
    }

    @Test
    public void testEscape01() {
        assertEquals("$2", evaluateExpression("$${1+1}"));
        assertEquals("$${1+1}", evaluateExpression("$\\${1+1}"));
    }

    @Test
    public void testEscape02() {
        assertEquals("$2", evaluateExpression("$#{1+1}"));
        assertEquals("$#{1+1}", evaluateExpression("$\\#{1+1}"));
    }

    @Test
    public void testEscape03() {
        assertEquals("#2", evaluateExpression("##{1+1}"));
        assertEquals("##{1+1}", evaluateExpression("#\\#{1+1}"));
    }

    @Test
    public void testEscape04() {
        assertEquals("#2", evaluateExpression("#${1+1}"));
        assertEquals("#${1+1}", evaluateExpression("#\\${1+1}"));
    }

    private String evaluateExpression(String expr) {
        ValueExpression v = ELManager.getExpressionFactory().createValueExpression(
                elm.getELContext(), expr, String.class);
        return (String) v.getValue(elm.getELContext());
    }
}
