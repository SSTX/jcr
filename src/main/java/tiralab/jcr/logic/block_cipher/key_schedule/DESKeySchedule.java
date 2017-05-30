package tiralab.jcr.logic.block_cipher.key_schedule;

import tiralab.jcr.logic.BitFunctions;

public class DESKeySchedule {

    private byte[] encryptionKey;
    private byte[] left;
    private byte[] right;
    private int iteration;
    
    public DESKeySchedule(byte[] key) {
        this.encryptionKey = this.permutedChoice1(key);
        this.init();
    }
    
    public void init() {
        this.left = BitFunctions.copyBits(0, 28, this.encryptionKey);
        this.right = BitFunctions.copyBits(28,56,this.encryptionKey);
        this.iteration = 0;
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
    
    private int nLeftShift(int iteration) {
        switch (iteration) {
            case 0:
            case 1:
            case 8:
            case 15:
                return 1;
            default:
                return 2;
        }
    }
    
    public byte[] nextKey() {
        int shift = this.nLeftShift(this.iteration);
        this.left = BitFunctions.rotateLeft(shift, 28, this.left);
        this.right = BitFunctions.rotateLeft(shift, 28, this.right);
        byte[] key = BitFunctions.concatBits(left, right, 28, 28);
        this.iteration++;
        if (this.iteration >= 16) {
            this.init();
        }
        return this.permutedChoice2(key);
    }
}
