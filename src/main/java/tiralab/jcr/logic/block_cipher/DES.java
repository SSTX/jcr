/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiralab.jcr.logic.block_cipher;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

/**
 *
 * @author ttiira
 */
public class DES implements BlockCipher {
    private Key k;
    
    public DES(Key k) {
        this.k = k;
    }
    
    @Override
    public byte[] encrypt(byte[] data, byte[] key) {
        try {
            Cipher c = Cipher.getInstance("DES/CBC/PBCS5Padding");
            c.init(Cipher.ENCRYPT_MODE, this.k);
            return c.doFinal(data);
        } catch (Exception e) {
            return new byte[0];
        }
    }

    @Override
    public byte[] decrypt(byte[] data, byte[] key) {
        try {
            Cipher c = Cipher.getInstance("DES/CBC/PBCS5Padding");
            c.init(Cipher.DECRYPT_MODE, this.k);
            data = c.doFinal(data);
            return data;
        } catch (Exception e) {
            return new byte[0];
        }
    }
    
}
