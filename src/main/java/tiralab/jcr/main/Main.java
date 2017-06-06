/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiralab.jcr.main;

import java.io.IOException;
import tiralab.jcr.logic.BitFunctions;
import tiralab.jcr.logic.Cryptographer;

/**
 *
 * @author ttiira
 */
public class Main {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        byte[] test = new byte[]{
            (byte) 0b00001010,
            (byte) 0b11101101,
            (byte) 0b00100001
        };
        Cryptographer g = new Cryptographer();
        try {
            System.out.println(BitFunctions.bitRepresentation(g.encrypt("/home/ttiira/testfile.txt", "/home/ttiira/testkey.txt", "asd")));
        } catch (IOException e) {
            System.out.println("io error");
        }
    }
}
