/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiralab.jcr.logic;

/**
 *
 * @author ttiira
 */
public class BitFunctions {

    /**
     * Function for modifying blocks at bit level.
     *
     * @param data Block to be modified. Length in bits must be greater than the
     * largest number in permTable.
     * @param permTable Array of integers that specifies which bits go where in
     * the new block. Each integer is a 0-based index for some bit.
     * @return New block with enough bytes to hold permTable.length bits, with
     * n-th bit from the left being the m-th bit in the original block, where m
     * = permTable[n].
     */
    public static byte[] permuteBits(byte[] data, int[] permTable) {
        byte[] permuted = BitFunctions.nBitByteArray(permTable.length);
        for (int i = 0; i < permTable.length; i++) {
            int nbyteData = permTable[i] / 8;
            int nbitData = permTable[i] % 8;
            int nbytePerm = i / 8;
            int nbitPerm = i % 8;
            byte b = data[nbyteData];
            b >>>= (7 - nbitData); //push the desired bit all the way to the right
            b &= 0b00000001;
            b <<= (7 - nbitPerm); //push the bit to its correct position in the byte
            permuted[nbytePerm] |= b;
        }
        return permuted;
    }

    /**
     * Helper function to make a whole byte from two 4-bit halves.
     *
     * @param b1 First half. Four low-order bits are used.
     * @param b2 Second half. Four low-order bits are used.
     * @return Byte with the four bits from b1 as the high-order bits and four
     * bits from b2 as the low-order bits.
     */
    public static byte combineHalfBytes(byte b1, byte b2) {
        b1 <<= 4;
        b2 &= 0b00001111;
        b1 |= b2;
        return b1;
    }

    /**
     * Get the nth bit from a byte array starting from the left.
     *
     * @param offset Index of the desired bit (0-based).
     * @param data The byte array containing the bit.
     * @return Byte with the rightmost bit being the desired bit and the rest
     * being zero bits.
     */
    public static byte getBitByOffset(int offset, byte[] data) {
        int nbyte = offset / 8;
        int nbit = offset % 8;
        byte b = data[nbyte];
        b >>>= (7 - nbit);
        b &= 0b00000001;
        return b;
    }

    /**
     * Rotate a byte array bitwise to the left.
     *
     * @param rotN How many positions to rotate.
     * @param lengthInBits How many bits to be considered.
     * @param data Byte array to rotate.
     * @return Bitwise-rotated byte array.
     */
    public static byte[] rotateLeft(int rotN, int lengthInBits, byte[] data) {
        if (lengthInBits <= 0) {
            return data;
        }
        int[] permTable = new int[lengthInBits];
        for (int i = 0; i < lengthInBits; i++) {
            permTable[i] = (i + rotN) % lengthInBits;
        }
        return BitFunctions.permuteBits(data, permTable);
    }

    /**
     * Make a byte array just large enough to hold a specified number of bits.
     *
     * @param bits Amount of bits needed.
     * @return New byte array initialized to all zeroes.
     */
    public static byte[] nBitByteArray(int bits) {
        if (bits % 8 == 0) {
            return new byte[bits / 8];
        } else {
            return new byte[bits / 8 + 1];
        }
    }

    /**
     * Concatenates two byte arrays bitwise.
     * @param left Array whose bits to put left in the concatenation.
     * @param right Array whose bits to put right in the concatenation.
     * @param nBitsLeft Number of bits in the left array.
     * @param nBitsRight Number of bits in the right array.
     * @return Byte array with nBitsLeft bits from the left array starting from left (0,0)
     * and nBitsRigth bits from the right array after them.
     */
    public static byte[] concatBits(byte[] left, byte[] right, int nBitsLeft, int nBitsRight) {
        byte[] bits = new byte[nBitsLeft + nBitsRight];
        int i = 0;
        while (i < nBitsLeft) {
            bits[i] = BitFunctions.getBitByOffset(i, left);
            i++;
        }
        int j = 0;
        while (j < nBitsRight) {
            bits[i] = BitFunctions.getBitByOffset(j, right);
            j++;
            i++;
        }
        return BitFunctions.bitsToBytes(bits);
    }

    /**
     * Compresses a byte array with each byte representing a single bit, into
     * a denseley packed byte array with the bits starting from the left.
     * @param bits Byte array with each byte representing a single bit
     * (rightmost bit in the byte).
     * @return Dense byte array with the bits starting from the left.
     */
    public static byte[] bitsToBytes(byte[] bits) {
        byte[] arr = BitFunctions.nBitByteArray(bits.length);
        for (int i = 0; i < bits.length; i++) {
            byte b = bits[i];
            b <<= 7 - (i % 8);
            arr[i / 8] |= b;
        }
        return arr;
    }

    /**
     * Copy a range of bits from a byte array.
     *
     * @param startInclusive Starting bit index, inclusive.
     * @param endExclusive Ending bit index, exclusive.
     * @param data Byte array to be copied from.
     * @return New byte array with the copied range starting from the left.
     */
    public static byte[] copyBits(int startInclusive, int endExclusive, byte[] data) {
        int nBits = endExclusive - startInclusive;
        byte[] arr = BitFunctions.nBitByteArray(nBits);
        for (int i = 0; i < nBits; i++) {
            byte b = BitFunctions.getBitByOffset(startInclusive + i, data);
            b <<= (7 - (i % 8)); //push the rightmost bit to its correct position
            arr[i / 8] |= b;
        }
        return arr;
    }
}
