/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiralab.jcr.logic;

import java.io.IOException;
import junit.framework.Assert;
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

    @Before
    public void setUp() {
        crypt = new Cryptographer();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void encryptDecryptReturnOriginalBytes1() {
        try {
            byte[] encrypted = crypt.encrypt("testdata/testdata1", "testdata/testkey1",
                     "des", "ecb");
            crypt.getIo().writeData("testdata/encryptedTestdata1", encrypted);
            byte[] decrypted = crypt.decrypt("testdata/encryptedTestdata1", "testdata/testkey",
                    "des", "ecb");
            assertArrayEquals(crypt.getIo().readData("testdata/testdata1"), decrypted);
        } catch (IOException e) {
            fail("IOError");
        }

    }
}
