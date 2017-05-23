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
        des = new DES(null);
        testBytes = new byte[4];
    } 

    @Test
    public void permuteBitsWorksCorrectly1() {
        testBytes[0] = (byte) 0;
        testBytes[1] = (byte) 0xff;
        int[] permTable = {
            8,9,10,11,12,13,14,15,
            0,1,2,3,4,5,6,7
        };
        byte[] permuted = des.permuteBits(testBytes, permTable);
        assertEquals(2, permuted.length);
        assertEquals(0x00, permuted[1]);

    }
}
