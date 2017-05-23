/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiralab.jcr.main;

import java.util.Arrays;
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
        ECB e = new ECB(new DES(null), 8);
        byte[] testBytes = new byte[70];
        Arrays.fill(testBytes, (byte) 0);
        for (int i = 0; i < 70; i++) {
            testBytes[i] += i; //0,1,2,3,4,5,6...
        }
        System.out.println(Arrays.toString(testBytes));
        for (byte[] b : e.makeBlocks(testBytes)) {
            System.out.println(Arrays.toString(b));
        }
        System.out.println(Integer.divideUnsigned(4, 2));
    }
}
