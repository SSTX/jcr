package tiralab.jcr.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import tiralab.jcr.logic.block_cipher.DES;
import tiralab.jcr.logic.mode_of_operation.ECB;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ttiira
 */
public class Benchmarking {

    public static void main(String[] args) {
        List<byte[]> inputs = new ArrayList<>();
        int limit = Integer.parseInt(args[0]);
        for (int i = 1; i <= limit; i *= 10) {
            inputs.add(mkArray(i));
            inputs.add(mkArray(5*i));
        }
        long[] times = new long[inputs.size()];
        byte[] key = mkArray(8);
        ECB ecb = new ECB(new DES(key), 8);
        ecb.encrypt(inputs.get(0));//ensure the program is compiled before testing
        for (int i = 0; i < inputs.size(); i++) {
            long timeBefore = System.nanoTime();
            ecb.encrypt(inputs.get(i));
            long timeAfter = System.nanoTime();
            times[i] = timeAfter - timeBefore;
        }
        for (int i = 0; i < times.length; i++) {
            System.out.println(inputs.get(i).length + "\t" + (1.0 * times[i] / 1000000000));
        }
    }
    
    public static byte[] mkArray(int len) {
        byte[] arr = new byte[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (byte)i;
        }
        return arr;
    }

}
