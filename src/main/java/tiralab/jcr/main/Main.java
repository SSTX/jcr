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
            (byte) -127
        };
        byte[] test2 = new byte[]{(byte) -123};
        System.out.println(BitFunctions.bitRepresentation(test));
        System.out.println(BitFunctions.bitRepresentation(test2));
    }
}
