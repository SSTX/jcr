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
     * Make a byte array just large enough to hold a specified number of bits.
     *
     * @param bits Amount of bits needed.
     * @return New byte array initialized to all zeroes, big enough to hold the
     * needed bits.
     */
    public static byte[] nBitByteArray(int bits) {
        if (bits % 8 == 0) {
            return new byte[bits / 8];
        } else {
            return new byte[bits / 8 + 1];
        }
    }

    /**
     * Make a byte array just large enough to hold a specified number of bits,
     * only using some of the bits in each byte.
     *
     * @param bits How many bits to store.
     * @param bitsPerByte How many bits to put in each byte.
     * @return New byte array initialized to all zeroes, big enough to hold the
     * needed bits.
     */
    public static byte[] nBitByteArray(int bits, int bitsPerByte) {
        if (bits % bitsPerByte == 0) {
            return new byte[bits / bitsPerByte];
        } else {
            return new byte[bits / bitsPerByte + 1];
        }
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
     * Insert a single bit into a specified 0-based index from the left.
     *
     * @param bit Byte representing the bit to insert. 1 or 0.
     * @param bitIndex How many bits from the left should the bit be inserted
     * (0-based).
     * @param array Array to insert the bit in.
     */
    public static void insertBit(byte bit, int bitIndex, byte[] array) {
        int nByte = bitIndex / 8;
        int nBit = bitIndex % 8;
        if (bit == 0) {
            bit = (byte) ~(1 << (7 - nBit));
            //bit is now all ones, except a zero in the position defined by bitIndex
            array[nByte] &= bit;
        } else {
            bit <<= (7 - nBit);
            array[nByte] |= bit;
        }
    }

    /**
     * Function for modifying blocks at bit level.
     *
     * @param data Block to be modified. Length in bits must be greater than the
     * largest number in permTable.
     * @param permTable Array of integers that specifies which bits go where in
     * the new block. Each bit-index i in the new block will have permTable[i]
     * as its value.
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
     * Rotate a byte array bitwise to the left.
     *
     * @param rotN How many positions to rotate.
     * @param lengthInBits How many bits from the left to be considered as part
     * of the array.
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
     * Concatenates two byte arrays bitwise.
     *
     * @param left Array whose bits to put left in the concatenation.
     * @param right Array whose bits to put right in the concatenation.
     * @param nBitsLeft Number of bits in the left array.
     * @param nBitsRight Number of bits in the right array.
     * @return Byte array with nBitsLeft bits from the left array starting from
     * the left and nBitsRight bits from the right array after them.
     */
    public static byte[] concatBits(byte[] left, byte[] right, int nBitsLeft, int nBitsRight) {
        byte[] arr = BitFunctions.nBitByteArray(nBitsLeft + nBitsRight);
        for (int i = 0; i < nBitsLeft; i++) {
            byte b = BitFunctions.getBitByOffset(i, left);
            BitFunctions.insertBit(b, i, arr);
        }
        for (int i = 0; i < nBitsRight; i++) {
            byte b = BitFunctions.getBitByOffset(i, right);
            BitFunctions.insertBit(b, nBitsLeft + i, arr);
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

    /**
     * Bitwise exclusive-or operation for two byte arrays.
     *
     * @param arr1 First operand.
     * @param arr2 Second operand.
     * @return Byte array constructed by XORing the bits of arr1 and arr2.
     */
    public static byte[] bitwiseXOR(byte[] arr1, byte[] arr2) {
        int len = arr1.length;
        if (arr2.length < len) {
            len = arr2.length;
        }
        byte[] ret = BitFunctions.copyBits(0, len * 8, arr1);
        for (int i = 0; i < len; i++) {
            ret[i] ^= arr2[i];
        }
        return ret;
    }
    
      /**
     * In-place bitwise exclusive-or operation for two byte arrays. The result
     * is stored in arr1.
     *
     * @param arr1 First operand. Will be overwritten.
     * @param arr2 Second operand.
     */
    public static void inPlaceBitwiseXOR(byte[] arr1, byte[] arr2) {
        int len = arr1.length;
        if (arr2.length < len) {
            len = arr2.length;
        }
        for (int i = 0; i < len; i++) {
            arr1[i] ^= arr2[i];
        }
    }

    /**
     * Stretch or compress a byte array by using a specified amount of bits in
     * each byte. Bit count n means n low-order bits.
     *
     * @param source Array to be stretched/compressed.
     * @param currentBitCount How many bits are currently used per byte
     * (low-order bits).
     * @param targetBitCount How many bits to put in each byte (low-order bits).
     * @return Stretched/compressed byte array.
     */
    public static byte[] chBitsPerByte(byte[] source, int currentBitCount, int targetBitCount) {
        if (targetBitCount <= 0
                || targetBitCount > 8
                || currentBitCount <= 0
                || currentBitCount > 8) {
            throw new IllegalArgumentException(
                    "Amount of bits used in chBitsPerByte must be in 1..8");
        }
        int bits = source.length * currentBitCount;
        //how many high-order bits to ignore in each byte
        int sourceSkip = 8 - currentBitCount;
        int targetSkip = 8 - targetBitCount;
        //start reading and writing from the first valid index
        int sourceIdx = sourceSkip;
        int targetIdx = targetSkip;
        byte[] arr = BitFunctions.nBitByteArray(bits, targetBitCount);
        //loop over each bit in the source array
        while (sourceIdx < 8 * source.length) {
            byte b = BitFunctions.getBitByOffset(sourceIdx, source);
            BitFunctions.insertBit(b, targetIdx, arr);
            sourceIdx++;
            targetIdx++;
            //if we have read currentBitCount bits, skip to the start of the next
            //piece of bits to read
            if (sourceIdx % 8 == 0) {
                sourceIdx += sourceSkip;
            }
            //if we have written targetBitCount bits, skip to the start of the next
            //piece of bits to write
            if (targetIdx % 8 == 0) {
                targetIdx += targetSkip;
            }
        }
        return arr;
    }

}
