/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiralab.jcr.logic.block_cipher.key_schedule;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ttiira
 */
public class DESKeyScheduleTest {

    byte[] key;
    DESKeySchedule sched;

    /**
     *
     */
    public DESKeyScheduleTest() {
    }

    /**
     *
     */
    @Before
    public void setUp() {
        key = new byte[]{
            (byte) 0b00010011,
            (byte) 0b00110100,
            (byte) 0b01010111,
            (byte) 0b01111001,
            (byte) 0b10011011,
            (byte) 0b10111100,
            (byte) 0b11011111,
            (byte) 0b11110001
        };
        sched = new DESKeySchedule(key);
    }

    /**
     *
     */
    @Test
    public void permutedChoice1ReturnCorrect() {
        byte[] bytes = new byte[]{
            (byte) 0b00010011,
            (byte) 0b00110100,
            (byte) 0b01010111,
            (byte) 0b01111001,
            (byte) 0b10011011,
            (byte) 0b10111100,
            (byte) 0b11011111,
            (byte) 0b11110001
        };
        byte[] expected = new byte[]{
            (byte) 0b11110000,
            (byte) 0b11001100,
            (byte) 0b10101010,
            (byte) 0b11110101,
            (byte) 0b01010110,
            (byte) 0b01100111,
            (byte) 0b10001111
        };
        assertArrayEquals(expected, sched.permutedChoice1(bytes));
    }

    /**
     *
     */
    @Test
    public void permutedChoice2ReturnCorrect() {
        byte[] bytes = new byte[]{
            (byte) 0b11100001,
            (byte) 0b10011001,
            (byte) 0b01010101,
            (byte) 0b11111010,
            (byte) 0b10101100,
            (byte) 0b11001111,
            (byte) 0b00011110
        };
        byte[] expected = new byte[]{
            (byte) 0b00011011,
            (byte) 0b00000010,
            (byte) 0b11101111,
            (byte) 0b11111100,
            (byte) 0b01110000,
            (byte) 0b01110010
        };
        assertArrayEquals(expected, sched.permutedChoice2(bytes));
    }

    /**
     *
     */
    @Test
    public void encryptionSubKeysReturnCorrectKeys1() {
        byte[] expected = new byte[]{
            (byte) 0b00011011,
            (byte) 0b00000010,
            (byte) 0b11101111,
            (byte) 0b11111100,
            (byte) 0b01110000,
            (byte) 0b01110010
        };
        assertArrayEquals(expected, sched.encryptionSubKeys()[0]);
    }

    /**
     *
     */
    @Test
    public void encryptionSubKeysReturnCorrectKeys2() {
        byte[] expected = new byte[]{
            (byte) 0b01111001,
            (byte) 0b10101110,
            (byte) 0b11011001,
            (byte) 0b11011011,
            (byte) 0b11001001,
            (byte) 0b11100101
        };
        assertArrayEquals(expected, sched.encryptionSubKeys()[1]);
    }

    /**
     *
     */
    @Test
    public void decryptionSubKeysReturnCorrectKeys1() {
        byte[] expected = new byte[]{
            (byte) 0b10010111,
            (byte) 0b11000101,
            (byte) 0b11010001,
            (byte) 0b11111010,
            (byte) 0b10111010,
            (byte) 0b01000001
        };
        assertArrayEquals(expected, sched.decryptionSubKeys()[3]);
    }

    /**
     *
     */
    @Test
    public void decryptionSubKeysReturnCorrectKeys2() {
        byte[] expected = new byte[]{
            (byte) 0b11100000,
            (byte) 0b11011011,
            (byte) 0b11101011,
            (byte) 0b11101101,
            (byte) 0b11100111,
            (byte) 0b10000001
        };
        assertArrayEquals(expected, sched.decryptionSubKeys()[7]);
    }
}
