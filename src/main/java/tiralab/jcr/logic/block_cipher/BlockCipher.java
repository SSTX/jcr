/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiralab.jcr.logic.block_cipher;

/**
 *
 * @author ttiira
 */
public interface BlockCipher {

    /**
     *
     * @param data
     * @param key
     * @return
     */
    byte[] encrypt(byte[] data, byte[] key);

    /**
     *
     * @param data
     * @param key
     * @return
     */
    byte[] decrypt(byte[] data, byte[] key);
}
