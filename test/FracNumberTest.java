import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Алеша on 21.02.2017.
 */
public class FracNumberTest {
    @Test
    public void multiplication() throws Exception {
        assertEquals("511.6356", new FracNumber(2,2,new int[]{2,2},new int[]{1,2}).multiplication(new FracNumber(2,2,new int[]{2,3},new int[]{1,3})));
        //assertEquals("5938.40319408", new FracNumber(3,3,new int[]{2,5,6},new int[]{1,9,8}).multiplication(new FracNumber(2,5,new int[]{2,3},new int[]{1,7,8,9,6})));
    }

    @Test
    public void equals() throws Exception {
        assertTrue(new FracNumber(3, 2, new int[]{1,2,3}, new int[]{2,1}).equals(new FracNumber(3, 2, new int[]{1,2,3}, new int[]{2,1})));
    }

    @org.junit.Test
    public void testToString() throws Exception {
        assertEquals("123.21", new FracNumber(3, 2, new int[]{1,2,3}, new int[]{2,1}).toString());
    }

}