package tiralab.jcr.logic.block_cipher;

import java.util.Arrays;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class DESTest {

    DES des;
    byte[] testBytes;
    byte[] keyBytes;
    javax.crypto.Cipher testCipher;

    @Before
    public void setUp() {
        testBytes = new byte[8];
        Arrays.fill(testBytes, (byte) 0x87);
        keyBytes = new byte[]{
            (byte) 0b00010011,
            (byte) 0b00110100,
            (byte) 0b01010111,
            (byte) 0b01111001,
            (byte) 0b10011011,
            (byte) 0b10111100,
            (byte) 0b11011111,
            (byte) 0b11110001
        };
        des = new DES(keyBytes);
    }

    @Test
    public void substituteOutputCorrect1() {
        assertEquals(13, des.substitute(0, (byte) 0b00111111));
    }

    @Test
    public void substituteOutputCorrect2() {
        assertEquals(1, des.substitute(2, (byte) 0b00110010));
    }

    @Test
    public void substituteOutputCorrect3() {
        assertEquals(9, des.substitute(1, (byte) 0b11111111));
    }

    @Test
    public void initialPermutationReturnCorrect() {
        byte[] bytes = new byte[]{
            (byte) 0b00000001,
            (byte) 0b00100011,
            (byte) 0b01000101,
            (byte) 0b01100111,
            (byte) 0b10001001,
            (byte) 0b10101011,
            (byte) 0b11001101,
            (byte) 0b11101111
        };
        byte[] expected = new byte[]{
            (byte) 0b11001100,
            (byte) 0b00000000,
            (byte) 0b11001100,
            (byte) 0b11111111,
            (byte) 0b11110000,
            (byte) 0b10101010,
            (byte) 0b11110000,
            (byte) 0b10101010
        };
        assertArrayEquals(expected, des.initialPermutation(bytes));
    }

    @Test
    public void expandReturnCorrect() {
        byte[] input = new byte[]{
            (byte) 0b11110000,
            (byte) 0b10101010,
            (byte) 0b11110000,
            (byte) 0b10101010
        };
        byte[] expected = new byte[]{
            (byte) 0b01111010,
            (byte) 0b00010101,
            (byte) 0b01010101,
            (byte) 0b01111010,
            (byte) 0b00010101,
            (byte) 0b01010101
        };
        assertArrayEquals(expected, des.expand(input));
    }

    @Test
    public void feistelFunctionReturnCorrect() {
        byte[] expected = new byte[]{
            (byte) 0b00100011,
            (byte) 0b01001010,
            (byte) 0b10101001,
            (byte) 0b10111011
        };
        byte[] key = new byte[]{
            (byte) 0b00011011,
            (byte) 0b00000010,
            (byte) 0b11101111,
            (byte) 0b11111100,
            (byte) 0b01110000,
            (byte) 0b01110010
        };
        byte[] input = new byte[]{
            (byte) 0b11110000,
            (byte) 0b10101010,
            (byte) 0b11110000,
            (byte) 0b10101010
        };
        assertArrayEquals(expected, des.feistelFunction(input, key));
    }

    @Test
    public void permutationPReturnCorrect() {
        byte[] expected = new byte[]{
            (byte) 0b00100011,
            (byte) 0b01001010,
            (byte) 0b10101001,
            (byte) 0b10111011
        };
        byte[] input = new byte[]{
            (byte) 0b01011100,
            (byte) 0b10000010,
            (byte) 0b10110101,
            (byte) 0b10010111
        };
        assertArrayEquals(expected, des.permutationP(input));
    }

    @Test
    public void finalPermutationReturnCorrect() {
        byte[] input = new byte[]{
            (byte) 0b00001010,
            (byte) 0b01001100,
            (byte) 0b11011001,
            (byte) 0b10010101,
            (byte) 0b01000011,
            (byte) 0b01000010,
            (byte) 0b00110010,
            (byte) 0b00110100
        };
        byte[] expected = new byte[]{
            (byte) 0b10000101,
            (byte) 0b11101000,
            (byte) 0b00010011,
            (byte) 0b01010100,
            (byte) 0b00001111,
            (byte) 0b00001010,
            (byte) 0b10110100,
            (byte) 0b00000101
        };
        assertArrayEquals(expected, des.finalPermutation(input));
    }

    @Test
    public void singleRoundReturnCorrect() {
        byte[] expected = new byte[]{
            (byte) 0b11110000,
            (byte) 0b10101010,
            (byte) 0b11110000,
            (byte) 0b10101010,
            (byte) 0b11101111,
            (byte) 0b01001010,
            (byte) 0b01100101,
            (byte) 0b01000100
        };
        byte[] left = new byte[]{
            (byte) 0b11001100,
            (byte) 0b00000000,
            (byte) 0b11001100,
            (byte) 0b11111111
        };
        byte[] right = new byte[]{
            (byte) 0b11110000,
            (byte) 0b10101010,
            (byte) 0b11110000,
            (byte) 0b10101010
        };
        byte[] roundKey = des.keySched.encryptionSubKeys()[0];
        assertArrayEquals(expected, des.round(roundKey, left, right));
    }

    @Test
    public void desEncryptionCorrect() {
        byte[] expected = new byte[]{
            (byte) 0b10000101,
            (byte) 0b11101000,
            (byte) 0b00010011,
            (byte) 0b01010100,
            (byte) 0b00001111,
            (byte) 0b00001010,
            (byte) 0b10110100,
            (byte) 0b00000101
        };
        byte[] input = new byte[]{
            (byte) 0b00000001,
            (byte) 0b00100011,
            (byte) 0b01000101,
            (byte) 0b01100111,
            (byte) 0b10001001,
            (byte) 0b10101011,
            (byte) 0b11001101,
            (byte) 0b11101111
        };
        assertArrayEquals(expected, des.encrypt(input));
    }
    
    @Test
    public void desEncryptDecryptGivesOriginalPlaintext() {
        byte[] input = new byte[]{
            (byte) 0b11110011,
            (byte) 0b10100010,
            (byte) 0b11010100,
            (byte) 0b01111110,
            (byte) 0b00001000,
            (byte) 0b00101100,
            (byte) 0b11100111,
            (byte) 0b10101011
        };
        assertArrayEquals(input, des.decrypt(des.encrypt(input)));
    }
}
