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
     * Encrypt a single block.
     * @param data Block to encrypt.
     * @return
     */
    byte[] encrypt(byte[] data);

    /**
     * Decrypt a single block.
     * @param data Block to decrypt.
     * @return
     */
    byte[] decrypt(byte[] data);
}
