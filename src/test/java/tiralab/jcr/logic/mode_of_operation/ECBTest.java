/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiralab.jcr.logic.mode_of_operation;

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

/**
 *
 * @author ttiira
 */
public class ECBTest {

    ECB ecb;
    byte[] testBytes;
    byte[] testBytes2;

    public ECBTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        ecb = new ECB(new DES(null), 8);
        testBytes = new byte[70];
        for (int i = 0; i < 70; i++) {
            testBytes[i] += i; //{0,1,2,...,69}
        }
        testBytes2 = Arrays.copyOfRange(testBytes, 30, 33); //{30, 31, 32}
    }

    @After
    public void tearDown() {
    }

    @Test
    public void blockSizeNotZero() {
        try {
            ECB test = new ECB(new DES(null), 0);
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void blockSizeNotNegative() {
        try {
            ECB test = new ECB(new DES(null), -1);
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void byteArrayPaddedCorrectlyWhenLengthNotMultipleOfBlockSize1() {
        byte[] test = new byte[34];
        test = ecb.padBytes(test);
        assertEquals(40, test.length);
    }

    @Test
    public void byteArrayPaddedCorrectlyWhenLengthNotMultipleOfBlockSize2() {
        byte[] test = new byte[9];
        test = ecb.padBytes(test);
        assertEquals(16, test.length);
    }

    @Test
    public void byteArrayPaddedCorrectlyWhenLengthIsMultipleOfBlockSize() {
        byte[] test = new byte[8];
        test = ecb.padBytes(test);
        assertEquals(16, test.length);
    }

    @Test
    public void makeBlocksSplitsCorrectly1() {
        byte[][] blocks = ecb.makeBlocks(testBytes);
        assertEquals(9, blocks.length);
        assertArrayEquals(Arrays.copyOfRange(testBytes, 0, 8), blocks[0]);
        assertArrayEquals(Arrays.copyOfRange(testBytes, 8, 16), blocks[1]);
    }

    @Test
    public void makeBlocksSplitsCorrectly2() {
        byte[][] blocks = ecb.makeBlocks(testBytes2);
        assertEquals(1, blocks.length);
        byte[] expected = new byte[]{(byte) 30, (byte) 31, (byte) 32, (byte) 5, (byte) 5, (byte) 5, (byte) 5, (byte) 5};
        assertArrayEquals(expected, blocks[0]);
    }
}
