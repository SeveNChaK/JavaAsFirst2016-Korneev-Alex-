import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class FracNumberTest {
    @Test
    public void minus() throws Exception {
        assertEquals("0", new FracNumber("0.25").minus(new FracNumber("0.25")));
        assertEquals("-113.16", new FracNumber("10,48").minus(new FracNumber("123.64")));
        assertEquals("-37.85345", new FracNumber("7.27").minus(new FracNumber("45.12345")));
        assertEquals("0", new FracNumber("0.25").minus( new FracNumber("0,25")));
    }

    @Test
    public void plus() throws Exception {
        assertEquals("45.25", new FracNumber("22.12").plus(new FracNumber("23.13")));
        assertEquals("0.025", new FracNumber("0.02").plus(new FracNumber("0.005")));
        assertEquals("22.5", new FracNumber("22").plus(new FracNumber("0.5")));
        assertEquals("123456.123456", new FracNumber("123456").plus(new FracNumber("0.123456")));

    }

    @Test
    public void multiplication() throws Exception {
        assertEquals("511.6356", new FracNumber("22.12").multiplication(new FracNumber("23.13")));
        assertEquals("5938.40319408", new FracNumber("256.198").multiplication(new FracNumber("23.17896")));
        assertEquals("0.015185088", new FracNumber("0.123").multiplication(new FracNumber("0.123456")));
        assertEquals("0.0026", new FracNumber("0.065").multiplication(new FracNumber("0.04")));
        assertEquals("1004.0749", new FracNumber("73.13").multiplication(new FracNumber("13.73")));
    }

    @Test
    public void equals() throws Exception {
        assertTrue(new FracNumber("123.21").equals(new FracNumber("123.21")));
        assertFalse(new FracNumber("12.21").equals(new FracNumber("123.21")));
    }

    @org.junit.Test
    public void testToString() throws Exception {
        List before = new ArrayList();
        List after = new ArrayList();
        Collections.addAll(before, 1,2,3); Collections.addAll(after, 2,1);
        assertEquals("123.21", new FracNumber(3, 2, before, after).toString());
    }

}