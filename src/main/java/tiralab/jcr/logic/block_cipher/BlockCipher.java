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
    byte[] encrypt(byte[] data, byte[] key);
    byte[] decrypt(byte[] data, byte[] key);
}
