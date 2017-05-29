package tiralab.jcr.logic.block_cipher;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class DESTest {

    DES des;
    byte[] testBytes;

    @Before
    public void setUp() {
        des = new DES();
        testBytes = new byte[4];
    }

    @Test
    public void substituteOutputCorrect1() {
        assertEquals(13, des.substitute(0, (byte)0b00111111));
    }
    
    @Test
    public void substituteOutputCorrect2() {
        assertEquals(1, des.substitute(2, (byte)0b00110010));
    }
    
    @Test
    public void substituteOutputCorrect3() {
        assertEquals(9, des.substitute(1, (byte)0b11111111));
    }
}
