/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiralab.jcr.logic.mode_of_operation;

import tiralab.jcr.logic.block_cipher.BlockCipher;

/**
 *
 * @author ttiira
 */
public class ECB extends ModeOfOperation {
    public ECB(BlockCipher cipher, int blockSize) {
        super(cipher, blockSize);
    }

    @Override
    public byte[] encrypt(byte[] data, byte[] key) {
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public byte[] decrypt(byte[] data, byte[] key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
