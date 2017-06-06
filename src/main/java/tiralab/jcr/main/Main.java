/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiralab.jcr.main;

import tiralab.jcr.logic.BitFunctions;

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
        BitFunctions.bitRepresentation(BitFunctions.chBitsPerByte(test, 4, 6));
    }
}
