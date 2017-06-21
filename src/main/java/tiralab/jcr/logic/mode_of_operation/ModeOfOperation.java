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
public abstract class ModeOfOperation {

    /**
     * The block cipher that is used.
     */
    protected BlockCipher cipher;

    /**
     * Block size in bytes.
     */
    protected int blockSize;

    /**
     * Mode of operation for block ciphers.
     *
     * @param cipher Block cipher to be used in this mode of operation.
     * @param blockSize Block size in bytes for the block cipher.
     */
    public ModeOfOperation(BlockCipher cipher, int blockSize) {
        this.cipher = cipher;
        if (blockSize <= 0) {
            throw new IllegalArgumentException("Invalid block size.");
        }
        this.blockSize = blockSize;
    }

    /**
     * Pads a byte array to the closest integer multiple of the block size used.
     * PKCS#7 padding.
     *
     * @param bytes Byte array to be padded.
     * @return PKCS#7 padded byte array.
     */
    public byte[] padBytes(byte[] bytes) {
        int padNeed = this.blockSize - (bytes.length % this.blockSize);
        byte pad = (byte) padNeed;
        byte[] paddedBytes = new byte[bytes.length + padNeed];
        for (int i = 0; i < bytes.length; i++) {
            paddedBytes[i] = bytes[i];
        }
        for (int i = bytes.length; i < paddedBytes.length; i++) {
            paddedBytes[i] = pad;
        }
        return paddedBytes;
    }
    
    /**
     * Remove PKCS#7 padding from a byte array. Inverse to padBytes.
     * @param bytes Byte array to be processed.
     * @return Byte array with PKCS#7 padding removed.
     * @throws IllegalArgumentException
     */
    public byte[] unpadBytes(byte[] bytes) throws IllegalArgumentException {
        byte pads = bytes[bytes.length - 1];
        if (pads <= 0 || pads >= bytes.length) {
            throw new IllegalArgumentException("Malformed padding in data to decrypt");
        }
        byte[] unpad = new byte[bytes.length - pads];
        for (int i = 0; i < unpad.length; i++) {
            unpad[i] = bytes[i];
        }
        return unpad;
    }

    /**
     * Splits an array of bytes into blocks.
     *
     * @param bytes Byte array to split into blocks.
     * @return 2-dimensional array of bytes with each row representing a block.
     */
    public byte[][] makeBlocks(byte[] bytes) {
        if (bytes.length % this.blockSize != 0) {
            throw new IllegalArgumentException("Byte array fed to makeBlocks has"
                    + " length that is not a multiple of blockSize");
        }
        int blockNum = bytes.length / this.blockSize;
        byte[][] blocks = new byte[blockNum][this.blockSize];
        int b = 0;
        for (int i = 0; i < blockNum; i++) {
            for (int j = 0; j < this.blockSize; j++) {
                if (b < bytes.length) {
                    blocks[i][j] = bytes[b];
                    b++;
                }
            }
        }
        return blocks;
    }
    /**
     * Reverse the things done in makeBlocks.
     * @param blocks Array to process.
     * @return 1-dimensional array containing all the bytes that were in the 
     * blocks-array.
     */
    public byte[] unmakeBlocks(byte[][] blocks) {
        int bytesInBlock = blocks[0].length;
        byte[] arr = new byte[blocks.length * bytesInBlock];
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < bytesInBlock; j++) {
                arr[this.blockSize * i + j] = blocks[i][j];
            }
        }
        return arr;
    }

    /**
     * Encrypts an array of bytes. Block cipher used is determined on class
     * instantiation.
     *
     * @param data Array of bytes to be encrypted.
     * @return Encrypted byte array.
     */
    public abstract byte[] encrypt(byte[] data);

    /**
     * Decrypts an array of bytes. Block cipher used is determined on class
     * instantiation.
     *
     * @param data Array of bytes to be decrypted.
     * @return Decrypted byte array.
     */
    public abstract byte[] decrypt(byte[] data);
}
