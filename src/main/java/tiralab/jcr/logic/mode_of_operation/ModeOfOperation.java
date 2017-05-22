/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiralab.jcr.logic.mode_of_operation;

import java.util.Arrays;
import tiralab.jcr.logic.block_cipher.BlockCipher;

/**
 *
 * @author ttiira
 */
public abstract class ModeOfOperation {
    protected BlockCipher cipher;
    protected int blockSize;
    
    public ModeOfOperation(BlockCipher cipher, int blockSize) {
        this.cipher = cipher;
        this.blockSize = blockSize;
    }
    
    protected byte[] padBytes(byte[] bytes) {
        int padNeed = this.blockSize - (bytes.length % this.blockSize);
        
        if (bytes.length % this.blockSize == 0) {
            byte[] padded = Arrays.copyOf(bytes, bytes.length + this.blockSize / 8);
            byte pad = Byte.decode(Integer.toHexString(blockSize / 8));
            for (int i = bytes.length; i < padded.length; i++) {
                padded[i] = pad;
            }
        }
    }
    
    public abstract byte[] encrypt(byte[] data, byte[] key);
    public abstract byte[] decrypt(byte[] data, byte[] key);
}
