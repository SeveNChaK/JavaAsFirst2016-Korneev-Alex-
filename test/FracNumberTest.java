import org.junit.Test;

import static org.junit.Assert.*;

public class FracNumberTest {
    @Test
    public void minus() throws Exception {
        assertEquals("0", new FracNumber("0.25").minus(new FracNumber("0.25")).toString());
        assertEquals("-113.16", new FracNumber("10.48").minus(new FracNumber("123.64")).toString());
        assertEquals("-37.85345", new FracNumber("7.27").minus(new FracNumber("45.12345")).toString());
        assertEquals("0", new FracNumber("6").minus(new FracNumber("6")).toString());
        assertEquals("2", new FracNumber("6").minus(new FracNumber("4")).toString());
        assertEquals("0.2", new FracNumber("6.2").minus(new FracNumber("6")).toString());
        assertEquals("0.2", new FracNumber("6").minus(new FracNumber("5.8")).toString());
        assertEquals("-6", new FracNumber("0").minus(new FracNumber("6")).toString());
        assertEquals("-6", new FracNumber("-8").minus(new FracNumber("-2")).toString());
        assertEquals("10", new FracNumber("8").minus(new FracNumber("-2")).toString());
        assertEquals("0", new FracNumber("6.3").minus(new FracNumber("6.3")).toString());
        assertEquals("-10", new FracNumber("-8").minus(new FracNumber("2")).toString());
    }

    @Test
    public void plus() throws Exception {
        assertEquals("45.25", new FracNumber("22.12").plus(new FracNumber("23.13")).toString());
        assertEquals("0.025", new FracNumber("0.02").plus(new FracNumber("0.005")).toString());
        assertEquals("22.5", new FracNumber("22").plus(new FracNumber("0.5")).toString());
        assertEquals("123456.123456", new FracNumber("123456").plus(new FracNumber("0.123456")).toString());
        assertEquals("45.25", new FracNumber("22.12").plus(new FracNumber("23.13")).toString());
        assertEquals("8", new FracNumber("2").plus(new FracNumber("6")).toString());
        assertEquals("4", new FracNumber("6").plus(new FracNumber("-2")).toString());
        assertEquals("-4", new FracNumber("2").plus(new FracNumber("-6")).toString());
        assertEquals("-1.01", new FracNumber("22.12").plus(new FracNumber("-23.13")).toString());
        assertEquals("-8", new FracNumber("-2").plus(new FracNumber("-6")).toString());
        assertEquals("-4", new FracNumber("2").plus(new FracNumber("-6")).toString());
        assertEquals("-1", new FracNumber("-0.6").plus(new FracNumber("-0.4")).toString());

    }

    @Test
    public void multiplication() throws Exception {
        assertEquals("511.6356", new FracNumber("22.12").multiplication(new FracNumber("23.13")).toString());
        assertEquals("5938.40319408", new FracNumber("256.198").multiplication(new FracNumber("23.17896")).toString());
        assertEquals("0.015185088", new FracNumber("0.123").multiplication(new FracNumber("0.123456")).toString());
        assertEquals("0.0026", new FracNumber("0.065").multiplication(new FracNumber("0.04")).toString());
        assertEquals("1004.0749", new FracNumber("73.13").multiplication(new FracNumber("13.73")).toString());
        assertEquals("121", new FracNumber("11").multiplication(new FracNumber("11")).toString());
        assertEquals("36", new FracNumber(-6).multiplication(new FracNumber(-6)).toString());
        assertEquals("-1089", new FracNumber(-33).multiplication(new FracNumber(33)).toString());
        assertEquals("-0", new FracNumber(-33).multiplication(new FracNumber(0)).toString());
        assertEquals("-33", new FracNumber(-33).multiplication(new FracNumber(1)).toString());
    }

    @Test
    public void equals() throws Exception {
        assertTrue(new FracNumber("123.21").equals(new FracNumber("123.21")));
        assertFalse(new FracNumber("12.21").equals(new FracNumber("123.21")));
        assertTrue(new FracNumber("-1.21").equals(new FracNumber("-1.21")));
        assertTrue(new FracNumber("-0.21").equals(new FracNumber("-0.21")));
    }

    @org.junit.Test
    public void testToString() throws Exception {
        assertEquals("123.21", new FracNumber("123.21").toString());
        assertEquals("-123.21", new FracNumber("-123.21").toString());
        assertEquals("123", new FracNumber("123").toString());
        assertEquals("123.0", new FracNumber("123.0").toString());
        assertEquals("00123.0", new FracNumber("00123.0").toString());
        assertEquals("-123", new FracNumber("-123").toString());
        assertEquals("-0.1", new FracNumber("-0.1").toString());
    }

}