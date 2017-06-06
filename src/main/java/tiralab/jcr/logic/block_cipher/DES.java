/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiralab.jcr.logic.block_cipher;

import tiralab.jcr.logic.BitFunctions;
import tiralab.jcr.logic.block_cipher.key_schedule.DESKeySchedule;

/**
 * Implementation of the Data Encryption Standard (DES) block cipher.
 *
 * @author ttiira
 */
public class DES implements BlockCipher {

    private int round;
    private byte[] key;
    private DESKeySchedule keySched;

    public DES(byte[] key) {
        this.key = key;
        this.round = 0;
        this.keySched = new DESKeySchedule(this.key);
    }

    /**
     * Expand function (E) from the DES cipher. Used in the Feistel function to
     * expand the right half-block to 48 bits.
     *
     * @param data 32-bit half-block to be expanded.
     * @return 48-bit block.
     */
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
        return BitFunctions.permuteBits(data, permTable);
    }

    /**
     * Initial permutation (IP) of the DES cipher.
     *
     * @param data 64-bit block to undergo IP.
     * @return Permuted 64-bit block.
     */
    public byte[] initialPermutation(byte[] data) {
        int[] permTable = {
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7,
            56, 48, 40, 32, 24, 16, 8, 0,
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6
        };
        return BitFunctions.permuteBits(data, permTable);
    }

    /**
     * Final permutation (FP) of the DES cipher.
     *
     * @param data 64-bit block to undergo FP.
     * @return Permuted 64-bit block.
     */
    public byte[] finalPermutation(byte[] data) {
        int[] permTable = {
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25,
            32, 0, 40, 8, 48, 16, 56, 24
        };
        return BitFunctions.permuteBits(data, permTable);
    }

    /**
     * Feistel function for DES.
     *
     * @param data Right half-block (32 bits) of the block being processed.
     * @param subkey 48-bit subkey from the key schedule.
     * @return 32-bit half-block to be XORed with the left half-block.
     */
    public byte[] feistelFunction(byte[] data, byte[] subkey) {
        //expansion
        byte[] block = this.expand(data);

        //key mixing
        block = BitFunctions.bitwiseXOR(block, subkey);

        //substitution
        byte[] stretched = BitFunctions.chBitsPerByte(block, 8, 6);
        byte[] subs = new byte[8];
        for (int i = 0; i < subs.length; i++) {
            subs[i] = this.substitute(i, stretched[i]);
        }

        //permutation
        subs = BitFunctions.chBitsPerByte(subs, 4, 8);
        byte[] out = this.permutationP(subs);
        return out;
    }

    /**
     * Substitution function for DES.
     *
     * @param n Number of the s-box used (0-based).
     * @param data Six bits of data to be fed into the s-box. Two high-order
     * bits are unused.
     * @return 4-bit value from the s-box. Four high-order bits are unused.
     */
    public byte substitute(int n, byte data) {
        int[][] substitutionBox = this.getSubstitutionBox(n);
        //6th and 1st bit from the right represent the row in the s-box
        int row = (((data >> 5) & 1) << 1) | (data & 1);
        //bits 5..2 from the right represent the column
        int col = 0;
        for (int i = 4; i > 0; i--) {
            col |= ((data >> i) & 1) << (i - 1);
        }
        return (byte) substitutionBox[row][col];
    }

    private int[][] getSubstitutionBox(int n) {
        switch (n) {
            case 0:
                return new int[][]{
                    {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
                    {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
                    {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
                    {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
                };
            case 1:
                return new int[][]{
                    {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
                    {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
                    {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
                    {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}
                };
            case 2:
                return new int[][]{
                    {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
                    {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
                    {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
                    {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}
                };
            case 3:
                return new int[][]{
                    {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
                    {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
                    {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
                    {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}
                };
            case 4:
                return new int[][]{
                    {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
                    {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
                    {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
                    {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}
                };
            case 5:
                return new int[][]{
                    {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
                    {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
                    {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
                    {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13},};
            case 6:
                return new int[][]{
                    {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
                    {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
                    {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
                    {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
                };
            case 7:
                return new int[][]{
                    {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
                    {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
                    {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
                    {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
                };
            default:
                return null;
        }
    }

    /**
     * Permutation part of the feistel function. Performed after substitution.
     *
     * @param data 32-bit half-block.
     * @return permuted half-block.
     */
    public byte[] permutationP(byte[] data) {
        int[] permTable = new int[]{
            15, 6, 19, 20, 28, 11, 27, 16,
            0, 14, 22, 25, 4, 17, 30, 9,
            1, 7, 23, 13, 31, 26, 2, 8,
            18, 12, 29, 5, 21, 10, 3, 24
        };
        return BitFunctions.permuteBits(data, permTable);
    }

    private byte[] process(byte[] data, byte[][] keys) {
        byte[] permutedInput = this.initialPermutation(data);
        byte[] left = BitFunctions.copyBits(0, 32, permutedInput);
        byte[] right = BitFunctions.copyBits(32, 64, permutedInput);
        for (int i = 0; i < 16; i++) {
            byte[] roundKey = keys[i];
            byte[] newRight = this.feistelFunction(right, roundKey);
            newRight = BitFunctions.bitwiseXOR(left, newRight);
            left = right;
            right = newRight;
        }
        byte[] out = BitFunctions.concatBits(left, right, 32, 32);
        return this.finalPermutation(out);
    }
    
    public byte[] round(byte[] roundKey, byte[] left, byte[] right) {
        byte[] newRight = BitFunctions.bitwiseXOR(left, this.feistelFunction(right, roundKey));
        left = right;
        right = newRight
        return BitFunctions.bitwiseConcat(left, right, 32, 32);
    }

    /**
     * Encrypt a single block of data.
     *
     * @param data Block of data to be encrypted.
     * @return Encrypted block.
     */
    @Override
    public byte[] encrypt(byte[] data) {
        byte[][] keys = this.keySched.encryptionSubKeys();
        return this.process(data, keys);
    }

    /**
     * Decrypt a single block of data.
     *
     * @param data Block of data to be decrypted.
     * @return Decrypted block.
     */
    @Override
    public byte[] decrypt(byte[] data) {
        byte[][] keys = this.keySched.decryptionSubKeys();
        return this.process(data, keys);
    }

}
