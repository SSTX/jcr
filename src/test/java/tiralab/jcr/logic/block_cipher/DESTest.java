package tiralab.jcr.logic.block_cipher;

import java.util.Arrays;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import tiralab.jcr.logic.block_cipher.DES;

public class DESTest {

    DES des;
    byte[] testBytes;

    @Before
    public void setUp() {
        des = new DES();
        testBytes = new byte[4];
    }

    @Test
    public void permuteBitsWorksCorrectly1() {
        testBytes[0] = (byte) 0;
        testBytes[1] = (byte) 0b11111111;
        int[] permTable = {
            8, 9, 10, 11, 12, 13, 14, 15, //1111 1111
            0, 1, 2, 3, 4, 5, 6, 7 //0000 0000
        };
        byte[] permuted = des.permuteBits(testBytes, permTable);
        assertEquals(2, permuted.length);
        assertEquals((byte) -1, permuted[0]); // -1 == 0b11111111
        assertEquals((byte) 0, permuted[1]);
    }

    @Test
    public void permuteBitsWorksCorrectly2() {
        testBytes[0] = (byte) 0b10101010;
        testBytes[1] = (byte) 0b01010101;
        int[] permTable = {
            9, 11, 13, 15, 0, 2, 4, 6, //1111 1111
            0, 0, 8, 8, 2, 2, 10, 10, //1100 1100
            8, 9, 10, 11, 12, 13, 14, 15 //0101 0101
        };
        byte[] permuted = des.permuteBits(testBytes, permTable);
        assertEquals(3, permuted.length);
        assertEquals(-1, permuted[0]); //-1 == 0b11111111
        assertEquals(-52, permuted[1]); //-52 == 0b11001100
        assertEquals(85, permuted[2]); //85 == 0b01010101
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
    public void getBitByOffsetReturnsCorrectBit1() {
        testBytes[0] = (byte) 0b10101010;
        assertEquals(1, des.getBitByOffset(0, testBytes));
        assertEquals(0, des.getBitByOffset(1, testBytes));
    }
    
    @Test
    public void getBitByOffsetReturnsCorrectBit2() {
        testBytes[1] = (byte) 0b11110000;
        assertEquals(0, des.getBitByOffset(14, testBytes));
        assertEquals(1, des.getBitByOffset(8, testBytes));
    }
}
