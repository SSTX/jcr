package tiralab.jcr.logic.block_cipher.key_schedule;

public class DESKeySchedule {
    private byte[] encryptionKey;
    private byte[] left;
    private byte[] right;

    public DESKeySchedule(byte[] key) {
        this.encryptionKey = key;
    }
}
