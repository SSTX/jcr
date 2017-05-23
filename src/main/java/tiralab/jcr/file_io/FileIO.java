/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiralab.jcr.file_io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * A simple class for reading and writing files.
 * @author ttiira
 */
public class FileIO {

    /**
     *
     * @param filePath Path to the file to be read.
     * @return Raw bytes read from file.
     * @throws IOException
     */
    public byte[] readData(String filePath) throws IOException {
        return Files.readAllBytes(Paths.get(filePath));
    }
    
    /**
     *
     * @param filePath Path to the file to be written.
     * @param data Raw bytes to be written to the file.
     * @throws IOException
     */
    public void writeData(String filePath, byte[] data) throws IOException {
        Files.write(Paths.get(filePath), data);
    }
}
