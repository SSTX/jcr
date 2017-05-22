/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiralab.jcr.logic;

import java.io.IOException;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.KeyGenerator;
import tiralab.jcr.file_io.FileIO;
import tiralab.jcr.logic.block_cipher.BlockCipher;
import tiralab.jcr.logic.block_cipher.DES;

/**
 *
 * @author ttiira
 */
public class CryptoHandler {

    private Map<String, BlockCipher> ciphers;
    private Key k;
    private FileIO fileIo;

    public CryptoHandler() {
        try {
            this.k = KeyGenerator.getInstance("DES").generateKey();
        } catch (Exception e) {
            this.k = null;
        }
        this.fileIo = new FileIO();
        this.ciphers = new HashMap<>();
        this.ciphers.put("DES", new DES(this.k));
    }
    
    public byte[] encrypt(String filePath, String keyPath, String cipherName)
            throws IOException {
        byte[] data = this.fileIo.readData(filePath);
        byte[] ret = new byte[data.length];
        return null;
    }
    
    public String decrypt(String filePath, String keyPath, String cipherName) 
            throws IOException{
        byte[] data = this.fileIo.readData(filePath);
        return null;
    }
}
