/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiralab.jcr.main;

import tiralab.jcr.logic.BitFunctions;
import tiralab.jcr.text_interface.TextInterface;

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
        System.out.println(BitFunctions.bitRepresentation(new byte[]{
            (byte) 00011011,
            (byte) 00000010,
            (byte) 11101111,
            (byte) 11111100,
            (byte) 01110000,
            (byte) 01110010
        }));
        System.out.println( "00011011 00000010 11101111 11111100 01110000 01110010 ");
    }
}
