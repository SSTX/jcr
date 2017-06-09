package tiralab.jcr.logic;

import java.io.IOException;
import tiralab.jcr.file_io.FileIO;
import tiralab.jcr.logic.block_cipher.BlockCipher;
import tiralab.jcr.logic.block_cipher.DES;
import tiralab.jcr.logic.mode_of_operation.ECB;
import tiralab.jcr.logic.mode_of_operation.ModeOfOperation;

/**
 *
 * @author ttiira
 */
public class Cryptographer {

    private FileIO io;

    /**
     *
     */
    public Cryptographer() {
        this.io = new FileIO();
    }

    /**
     *
     * @param cipherName
     * @param modeOfOperation
     * @param key
     * @return
     */
    public ModeOfOperation selectMode(String cipherName, String modeOfOperation,
            byte[] key) {
        BlockCipher cipher;
        ModeOfOperation mode;
        int blockSize;
        switch (cipherName.toLowerCase()) {
            case "des":
            default:
                cipher = new DES(key);
                blockSize = 8;
        }
        switch (modeOfOperation.toLowerCase()) {
            case "ecb":
            default:
                mode = new ECB(cipher, blockSize);
        }
        return mode;
    }

    /**
     * Encrypts a file.
     *
     * @param inputFilePath Path to the input file.
     * @param outputFilePath Path to the output file.
     * @param keyPath Path to the key file.
     * @param cipherName Name of the cipher to use.
     * @param modeOfOperation Mode of operation to use the block cipher with.
     * @throws java.io.IOException
     */
    public void encrypt(String inputFilePath, String outputFilePath, String keyPath,
            String cipherName, String modeOfOperation) throws IOException {
        byte[] data = this.getIo().readData(inputFilePath);
        byte[] key = this.getIo().readData(keyPath);
        ModeOfOperation mode = this.selectMode(cipherName, modeOfOperation, key);
        this.getIo().writeData(outputFilePath, mode.encrypt(data));
    }

    /**
     * Decrypts a file.
     *
     * @param inputFilePath Path to the input file.
     * @param outputFilePath Path to the output file.
     * @param keyPath Path to the key file.
     * @param cipherName Name of the cipher to use.
     * @param modeOfOperation Mode of operation to use the cipher with.
     * @throws java.io.IOException
     */
    public void decrypt(String inputFilePath, String outputFilePath, String keyPath,
            String cipherName, String modeOfOperation) throws IOException {
        byte[] data = this.getIo().readData(inputFilePath);
        byte[] key = this.getIo().readData(keyPath);
        ModeOfOperation mode = this.selectMode(cipherName, modeOfOperation, key);
        this.getIo().writeData(outputFilePath, mode.decrypt(data));
    }

    /**
     * @return the io
     */
    public FileIO getIo() {
        return io;
    }
}
