/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiralab.jcr.main;

import java.util.Arrays;
import tiralab.jcr.logic.BitFunctions;
import tiralab.jcr.logic.block_cipher.DES;
import tiralab.jcr.logic.mode_of_operation.ECB;

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

        DES des = new DES(new byte[]{0, 0, 0, 0, 0, 0, 0, 0});
        byte[] test = new byte[]{
            (byte) 0,
            (byte) 0,
            (byte) 0b11111111,
            (byte) 0b11111111
        };
        byte[] key = new byte[]{
            (byte) 1,
            (byte) 1,
            (byte) 1,
            (byte) 1,
            (byte) 1,
            (byte) 1
        };
        System.out.println(-1 % 8);
    }
}
