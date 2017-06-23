/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiralab.jcr.logic.mode_of_operation;

import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
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
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.Key;
import java.security.KeyFactory;
import static org.junit.Assert.*;

/**
 *
 * @author ttiira
 */
public class ECBTest {

    ECB ecb;
    Cipher testCipher;
    byte[] testBytes;
    byte[] testBytes2;
    byte[] evenBlocks;
    byte[] evenBlocks2;
    /**
     *
     */
    public ECBTest() {
    }

    /**
     *
     */
    @Before
    public void setUp() {
        DES des = new DES("aaaaaaaa".getBytes());
        ecb = new ECB(des, 8);
        testBytes = new byte[70];
        for (int i = 0; i < 70; i++) {
            testBytes[i] += i; //{0,1,2,...,69}
        }
        testBytes2 = Arrays.copyOfRange(testBytes, 30, 33); //{30, 31, 32}
        evenBlocks = Arrays.copyOfRange(testBytes, 0, 8);
        evenBlocks2 = Arrays.copyOfRange(testBytes, 8, 32);
    }

    /**
     *
     */
    @After
    public void tearDown() {
    }

    /**
     *
     */
    @Test
    public void blockSizeNotZero() {
        try {
            ECB test = new ECB(new DES(new byte[8]), 0);
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    /**
     *
     */
    @Test
    public void blockSizeNotNegative() {
        try {
            ECB test = new ECB(new DES(new byte[8]), -1);
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    /**
     *
     */
    @Test
    public void byteArrayPaddedCorrectlyWhenLengthNotMultipleOfBlockSize1() {
        byte[] test = new byte[34];
        test = ecb.padBytes(test);
        assertEquals(40, test.length);
    }

    /**
     *
     */
    @Test
    public void byteArrayPaddedCorrectlyWhenLengthNotMultipleOfBlockSize2() {
        byte[] test = new byte[9];
        test = ecb.padBytes(test);
        assertEquals(16, test.length);
    }

    /**
     *
     */
    @Test
    public void byteArrayPaddedCorrectlyWhenLengthIsMultipleOfBlockSize() {
        byte[] test = new byte[8];
        test = ecb.padBytes(test);
        assertEquals(16, test.length);
    }

    @Test
    public void padUnpadReturnOriginalBytes1() {
        byte[] padded = ecb.padBytes(testBytes);
        assertArrayEquals(testBytes, ecb.unpadBytes(padded));
    }

    @Test
    public void padUnpadReturnOriginalBytes2() {
        byte[] padded = ecb.padBytes(testBytes2);
        assertArrayEquals(testBytes2, ecb.unpadBytes(padded));
    }

    @Test
    public void padUnpadReturnOriginalBytes3() {
        byte[] padded = ecb.padBytes(new byte[1]);
        assertArrayEquals(new byte[1], ecb.unpadBytes(padded));
    }

    @Test
    public void makeUnmakeBlocksReturnOriginalBytes1() {
        byte[][] blocks = ecb.makeBlocks(evenBlocks);
        assertArrayEquals(evenBlocks, ecb.unmakeBlocks(blocks));
    }

    @Test
    public void makeUnmakeBlocksReturnOriginalBytes2() {
        byte[][] blocks = ecb.makeBlocks(evenBlocks2);
        assertArrayEquals(evenBlocks2, ecb.unmakeBlocks(blocks));
    }


    @Test
    public void encryptDecryptReturnOriginalBytes1() {
        assertArrayEquals(testBytes, ecb.decrypt(ecb.encrypt(testBytes)));
    }

    @Test
    public void ecbEncryptEqualResultToJavax() {
	try {
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            SecretKeyFactory gen = SecretKeyFactory.getInstance("DES");
            Key keyComp = gen.generateSecret(new DESKeySpec("aaaaaaaa".getBytes()));
            cipher.init(Cipher.ENCRYPT_MODE, keyComp);
	    assertArrayEquals(cipher.doFinal(testBytes), ecb.encrypt(testBytes));	
	} catch (Exception e) {
		fail("Error initializing cipher");
	}
    }

}
