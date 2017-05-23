/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiralab.jcr.logic.block_cipher;

import java.security.Key;

import javax.crypto.Cipher;

/**
 * Implementation of the Data Encryption Standard (DES) block cipher.
 * @author ttiira
 */
public class DES implements BlockCipher {
    private Key k;
    
    /**
     *
     * @param k
     */
    public DES(Key k) {
        this.k = k;

    }

    public byte[] permuteBits(byte[] data, int[] permTable) {
        byte[] permuted = new byte[permTable.length / 8];
        for (int i = 0; i < data.length; i++) {
            int nbyte = permTable[i] / 8;
            int nbit = permTable[i] % 8;
            byte b = data[nbyte];
            b <<= nbit - 1;
            b >>= 7;
            nbyte = i / 8;
            nbit = i % 8;
            permuted[nbyte] |= b << 7 - nbit;
        }
        return permuted;
    }

    public byte[] expand(byte[] data) {
        int[] permTable = {
            31, 0, 1, 2, 3, 4,
            3, 4, 5, 6, 7, 8,
            7, 8, 9, 10, 11, 12,
            11, 12, 13, 14, 15, 16,
            15, 16, 17, 18, 19, 20,
            19, 20, 21, 22, 23, 24,
            23, 24, 25, 26, 27, 28,
            27, 28, 29, 30, 31, 0
        };
        return this.permuteBits(data, permTable);
    }

    private byte[] feistelFunction(byte[] data, byte[] subkey) {
        return null;
    }

    /**
     * Encrypt a single block of data.
     * @param data Block of data to be encrypted.
     * @param key Encryption key.
     * @return Encrypted block.
     */
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

    /**
     * Decrypt a single block of data.
     * @param data Block of data to be decrypted.
     * @param key Decryption key.
     * @return Decrypted block.
     */
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
