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

    /**
     * Implementation of the Electronic Code Book (ECB) mode of operation for
     * block ciphers. Each block is encrypted independently of others. This mode
     * of operation is not secure because all identical blocks will be encrypted
     * identically.
     *
     * @param cipher Block cipher to be used.
     * @param blockSize Block size in bytes.
     */
    public ECB(BlockCipher cipher, int blockSize) {
        super(cipher, blockSize);
    }

    /**
     * Encrypts a byte array using ECB and the block cipher determined at class
     * instantiation. Each block is encrypted separately.
     *
     * @param data Byte array to be encrypted.
     * @return Encrypted byte array.
     */
    @Override
    public byte[] encrypt(byte[] data) {
        byte[][] blocks = this.makeBlocks(data);
        for (int i = 0; i < blocks.length; i++) {
            blocks[i] = this.cipher.encrypt(blocks[i]);
        }
        byte[] ret = new byte[this.blockSize * blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < this.blockSize; j++) {
                ret[this.blockSize * i + j] = blocks[i][j];
            }
        }
        return ret;
    }

    /**
     * Decrypts a byte array using ECB and the block cipher determined at class
     * instantiation. Each block is decrypted separately.
     *
     * @param data Byte array to be decrypted.
     * @param key Decryption key.
     * @return Decrypted byte array.
     */
    @Override
    public byte[] decrypt(byte[] data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
