package tiralab.jcr.logic.block_cipher.key_schedule;

import tiralab.jcr.logic.BitFunctions;

public class DESKeySchedule {

    private byte[] encryptionKey;
    private byte[] left;
    private byte[] right;

    public DESKeySchedule(byte[] key) {
        this.encryptionKey = this.permutedChoice1(key);
        
    }

    private byte[] permutedChoice1(byte[] block) {
        int[] permTable = new int[]{
            //left 28 bits
            56, 48, 40, 32, 24, 16, 8,
            0, 57, 49, 41, 33, 25, 17,
            9, 1, 58, 50, 42, 34, 26,
            18, 10, 2, 59, 51, 43, 35,
            //right 28 bits
            62, 54, 46, 38, 30, 22, 14,
            6, 61, 53, 45, 37, 29, 21,
            13, 5, 60, 52, 44, 36, 28,
            20, 12, 4, 27, 19, 11, 3
        };
        return BitFunctions.permuteBits(block, permTable);
    }

    private byte[] permutedChoice2(byte[] state) {
        int[] permTable = new int[]{
            13, 16, 10, 23, 0, 4,
            2, 27, 14, 5, 20, 9,
            22, 18, 11, 3, 25, 7,
            15, 6, 26, 19, 12, 1,
            40, 51, 30, 36, 46, 54,
            29, 39, 50, 44, 32, 47,
            43, 48, 38, 55, 33, 52,
            45, 41, 49, 35, 28, 31
        };
        return BitFunctions.permuteBits(state, permTable);
    }
    
    public byte[] nextKey() {
        //todo
        this.left = BitFunctions.rotateLeft(0, 0, this.left);
        this.right = BitFunctions.rotateLeft(0, 0, this.right);
        return null;
    }
}
