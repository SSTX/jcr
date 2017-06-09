/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiralab.jcr.logic;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ttiira
 */
public class CryptographerTest {

    Cryptographer crypt;
    byte[] testBytes;
    byte[] testBytes2;
    String basePath;
    String inputFilePath1;
    String inputFilePath2;
    String encryptedFilePath1;
    String encryptedFilePath2;
    String decryptedFilePath1;
    String decryptedFilePath2;
    String keyPath1;
    String keyPath2;

    @Before
    public void setUp() {
        crypt = new Cryptographer();
        basePath = System.getProperty("user.dir") + "/testdata/";
        inputFilePath1 = basePath + "testdata1";
        inputFilePath2 = basePath + "testdata2";
        encryptedFilePath1 = basePath + "encryptedTestData1";
        encryptedFilePath2 = basePath + "encryptedTestData2";
        decryptedFilePath1 = basePath + "decryptedTestData1";
        decryptedFilePath2 = basePath + "decryptedTestData2";
        keyPath1 = basePath + "testkey1";
        keyPath2 = basePath + "testkey2";
    }

    @After
    public void tearDown() {
    }

    @Test
    public void encryptDecryptGivesOriginalBytes1() {
        try {
            crypt.encrypt(inputFilePath1, encryptedFilePath1, keyPath1, "des", "ecb");
            crypt.decrypt(encryptedFilePath1, decryptedFilePath1, keyPath1, "des", "ecb");
            assertArrayEquals(crypt.getIo().readData(inputFilePath1),
                    crypt.getIo().readData(decryptedFilePath1)
            );
        } catch (IOException e) {
            fail("IOError");
        }
    }

    @Test
    public void encryptDecryptGivesOriginalBytes2() {
        try {
            crypt.encrypt(inputFilePath2, encryptedFilePath2, keyPath2, "des", "ecb");
            crypt.decrypt(encryptedFilePath2, decryptedFilePath2, keyPath2, "des", "ecb");
            assertArrayEquals(crypt.getIo().readData(inputFilePath2),
                    crypt.getIo().readData(decryptedFilePath2)
            );
        } catch (IOException e) {
            fail("IOError");
        }
    }

    @Test
    public void decryptWithDifferentKeyReturnDifferentBytes1() {
        try {
            crypt.encrypt(inputFilePath1, encryptedFilePath1, keyPath1, "des", "ecb");
            crypt.decrypt(encryptedFilePath1, decryptedFilePath1, keyPath2, "des", "ecb");
            assertFalse(Arrays.equals(crypt.getIo().readData(inputFilePath1),
                    crypt.getIo().readData(decryptedFilePath1)
            ));
        } catch (IOException e) {
            fail("IOError\n");
        } catch (IllegalArgumentException e) {
            //if the padding bytes come to be negative after decryption
        }
    }

    @Test
    public void decryptWithDifferentKeyReturnDifferentBytes2() {
        try {
            crypt.encrypt(inputFilePath2, encryptedFilePath2, keyPath1, "des", "ecb");
            crypt.decrypt(encryptedFilePath2, decryptedFilePath2, keyPath2, "des", "ecb");
            assertFalse(Arrays.equals(crypt.getIo().readData(inputFilePath2),
                    crypt.getIo().readData(decryptedFilePath2)
            ));
        } catch (IOException e) {
            fail("IOError\n");
        } catch (IllegalArgumentException e) {
            //if the padding bytes come to be negative after decryption
        }
    }
}
