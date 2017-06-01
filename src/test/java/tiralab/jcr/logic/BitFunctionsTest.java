/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiralab.jcr.logic;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ttiira
 */
public class BitFunctionsTest {

    public BitFunctionsTest() {
    }

    byte[] testBytes;

    @Before
    public void setUp() {
        testBytes = new byte[4];
    }

    @After
    public void tearDown() {
    }

    @Test
    public void permuteBitsWorksCorrectly1() {
        testBytes[0] = (byte) 0;
        testBytes[1] = (byte) 0b11111111;
        int[] permTable = {
            8, 9, 10, 11, 12, 13, 14, 15, //1111 1111
            0, 1, 2, 3, 4, 5, 6, 7 //0000 0000
        };
        byte[] permuted = BitFunctions.permuteBits(testBytes, permTable);
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
        byte[] permuted = BitFunctions.permuteBits(testBytes, permTable);
        assertEquals(3, permuted.length);
        assertEquals(-1, permuted[0]); //-1 == 0b11111111
        assertEquals(-52, permuted[1]); //-52 == 0b11001100
        assertEquals(85, permuted[2]); //85 == 0b01010101
    }

    @Test
    public void getBitByOffsetReturnsCorrectBit1() {
        testBytes[0] = (byte) 0b10101010;
        assertEquals(1, BitFunctions.getBitByOffset(0, testBytes));
        assertEquals(0, BitFunctions.getBitByOffset(1, testBytes));
    }

    @Test
    public void getBitByOffsetReturnsCorrectBit2() {
        testBytes[1] = (byte) 0b11110000;
        assertEquals(0, BitFunctions.getBitByOffset(14, testBytes));
        assertEquals(1, BitFunctions.getBitByOffset(8, testBytes));
    }

    @Test
    public void getBitByOffsetReturnsCorrectBit3() {
        testBytes[0] = (byte) 0b11111111;
        testBytes[1] = (byte) 0b10101111;
        assertEquals(1, BitFunctions.getBitByOffset(10, testBytes));
        assertEquals(0, BitFunctions.getBitByOffset(9, testBytes));
    }

    @Test
    public void combineHalfBytesReturnCorrect1() {
        byte a = (byte) 0b00000001;
        byte b = (byte) 0b00001111;
        assertEquals(31, BitFunctions.combineHalfBytes(a, b));
    }

    @Test
    public void combineHalfBytesReturnCorrect2() {
        byte a = (byte) 0b00000101;
        byte b = (byte) 0b00001011;
        assertEquals(91, BitFunctions.combineHalfBytes(a, b));
    }

    @Test
    public void combineHalfBytesIgnoreHighOrderBits() {
        byte a = (byte) 0b11000101;
        byte b = (byte) 0b01101011;
        assertEquals(91, BitFunctions.combineHalfBytes(a, b));
    }

    @Test
    public void rotateLeftReturnsCorrect1() {
        testBytes[0] = (byte) 0b00001111;
        byte[] expected = new byte[]{
            (byte) 0b0011110
        };
        assertArrayEquals(expected, BitFunctions.rotateLeft(1, 8, testBytes));
    }

    @Test
    public void rotateLeftReturnsCorrect2() {
        testBytes[0] = (byte) 0b11111111;
        byte[] expected = new byte[]{
            (byte) 0,
            (byte) 0b11111111
        };
        assertArrayEquals(expected, BitFunctions.rotateLeft(8, 16, testBytes));
    }

    @Test
    public void rotateLeftReturnsCorrect3() {
        testBytes[0] = (byte) 0b10100000;
        testBytes[1] = (byte) 0b11110000;
        testBytes[3] = (byte) 0b00000001;
        byte[] expected = new byte[]{
            (byte) 0b00000111,
            (byte) 0b10000000,
            (byte) 0b01010000
        };
        assertArrayEquals(expected, BitFunctions.rotateLeft(3, 20, testBytes));
    }

    @Test
    public void rotateLeftIgnoresLengthLessThanOne() {
        testBytes[0] = (byte) 0b00011100;
        testBytes[2] = (byte) 0b00101011;
        assertArrayEquals(testBytes, BitFunctions.rotateLeft(230, -1, testBytes));
        assertArrayEquals(testBytes, BitFunctions.rotateLeft(119, 0, testBytes));
    }

    @Test
    public void copyBitsReturnCorrect1() {
        testBytes[0] = (byte) 0b00110011;
        assertArrayEquals(testBytes, BitFunctions.copyBits(0, 32, testBytes));
    }

    @Test
    public void copyBitsReturnCorrect2() {
        testBytes[0] = (byte) 0b11001010;
        byte[] expected = new byte[]{
            (byte) 0b00101000,};
        assertArrayEquals(expected, BitFunctions.copyBits(2, 8, testBytes));
    }

    @Test
    public void copyBitsReturnCorrect3() {
        testBytes[0] = (byte) 0b10101010;
        testBytes[1] = (byte) 0b01010101;
        byte[] expected = new byte[]{
            (byte) 0b10000000
        };
        assertArrayEquals(expected, BitFunctions.copyBits(6, 9, testBytes));
    }

    @Test
    public void concatBitsReturnCorrect1() {
        testBytes[0] = (byte) 0b11111111;
        byte[] test = new byte[]{0b01000000};
        byte[] expected = new byte[]{(byte) 0b11101000};
        assertArrayEquals(expected, BitFunctions.concatBits(testBytes, test, 3, 2));
    }

    @Test
    public void concatBitsReturnCorrect2() {
        testBytes[0] = (byte) 0b11011111;
        byte[] test = new byte[]{0b01000000};
        byte[] expected = new byte[]{
            (byte) 0b01011011,
            (byte) 0b10000000
        };
        assertArrayEquals(expected, BitFunctions.concatBits(test, testBytes, 3, 6));
    }

    @Test
    public void concatBitsReturnCorrect3() {
        testBytes[0] = (byte) 0b00000001;
        testBytes[1] = (byte) 0b10101010;
        byte[] test = new byte[]{
            (byte) 0b11110111,
            (byte) 0b00001010,
            (byte) 0b00001000
        };
        byte[] expected = new byte[]{
            (byte) 0b00000001,
            (byte) 0b10101011,
            (byte) 0b11011100,
            (byte) 0b00101000,
            (byte) 0b00100000
        };
        assertArrayEquals(expected, BitFunctions.concatBits(testBytes, test, 14, 22));
    }

    @Test
    public void bitwiseXORReturnCorrect() {
        byte[] in1 = new byte[]{
            (byte) 0b00011011,
            (byte) 0b00000010,
            (byte) 0b11101111,
            (byte) 0b11111100,
            (byte) 0b01110000,
            (byte) 0b01110010
        };
        byte[] in2 = new byte[]{
            (byte) 0b01111010,
            (byte) 0b00010101,
            (byte) 0b01010101,
            (byte) 0b01111010,
            (byte) 0b00010101,
            (byte) 0b01010101
        };
        byte[] expected = new byte[]{
            (byte) 0b01100001,
            (byte) 0b00010111,
            (byte) 0b10111010,
            (byte) 0b10000110,
            (byte) 0b01100101,
            (byte) 0b00100111
        };
        assertArrayEquals(expected, BitFunctions.bitwiseXOR(in1, in2));
    }
}
