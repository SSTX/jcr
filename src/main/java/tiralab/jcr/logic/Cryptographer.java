package tiralab.jcr.logic;

import java.io.IOException;
import tiralab.jcr.file_io.FileIO;
import tiralab.jcr.logic.block_cipher.DES;
import tiralab.jcr.logic.mode_of_operation.ECB;

public class Cryptographer {
    private FileIO io;
    
    public Cryptographer() {
        this.io = new FileIO();
    }
    
    /**
     * Encrypts a file.
     * @param filePath Path to the file in the filesystem.
     * @param keyPath Path to the key file.
     * @param cipherName Name of the cipher to use.
     * @return Encrypted byte array.
     */
    public byte[] encrypt(String filePath, String keyPath, String cipherName) throws IOException {
        byte[] data = this.io.readData(filePath);
        byte[] key = this.io.readData(keyPath);
        DES des = new DES(key);
        ECB ecb = new ECB(des, 8);
        return ecb.encrypt(data);
    }

    /**
     * Decrypts a file.
     * @param filePath Path to the file in the filesystem.
     * @param keyPath Path to the key file.
     * @param cipherName Name of the cipher to use.
     * @return Decrypted byte array.
     */
    public byte[] decrypt(String filePath, String keyPath, String cipherName) throws IOException {
        //todo
        return null;
    }
}
