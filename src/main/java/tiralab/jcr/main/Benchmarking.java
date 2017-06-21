package tiralab.jcr.main;

import java.security.Key;
import java.security.KeyFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import tiralab.jcr.logic.BitFunctions;
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
        Map<Integer, List<Long>> times = new TreeMap<>();
        Map<Integer, List<Long>> timesComp = new TreeMap<>();
        int start = Integer.parseInt(args[0]);
        int limit = Integer.parseInt(args[1]);
        int skip = Integer.parseInt(args[2]);
        List<byte[]> inputs = initInputs(start, limit, skip);
        inputs.stream().forEach((a) -> {
            times.put(a.length, new ArrayList<>());
            timesComp.put(a.length, new ArrayList<>());
        });

        byte[] key = mkArray(8);
        ECB ecb = new ECB(new DES(key), 8);
        Cipher cipherComp = initCipher(key);

        //ensure the program is compiled before testing
        ecb.encrypt(inputs.get(0));
        try {
            cipherComp.doFinal(inputs.get(0));
        } catch (Exception ignore) {
        }

        for (int i = 0; i < inputs.size(); i++) {
            for (int j = 0; j < 10; j++) {
                long timeBefore = System.nanoTime();
                ecb.encrypt(inputs.get(i));
                long timeAfter = System.nanoTime();
                times.get(inputs.get(i).length).add(timeAfter - timeBefore);
            }
            for (int j = 0; j < 10; j++) {
                long timeBefore = System.nanoTime();
                try {
                    cipherComp.doFinal(inputs.get(i));
                } catch (Exception ignore) {
                }
                long timeAfter = System.nanoTime();
                timesComp.get(inputs.get(i).length).add(timeAfter - timeBefore);
            }

        }
        System.out.println("Self-implemented");
        System.out.println("size\t\tnanoseconds");
        for (int i : times.keySet()) {
            System.out.println(i + "\t\t" + avg(times.get(i)));
        }
        System.out.println("Java default provider");
        System.out.println("size\t\tnanoseconds");
        for (int i : timesComp.keySet()) {
            System.out.println(i + "\t\t " + avg(timesComp.get(i)));
        }
        System.out.println("Relative times (self / default)");
        System.out.println("size\t\tcoefficient");
        for (int i : times.keySet()) {
            System.out.println(i + "\t\t" + avg(times.get(i)) / avg(timesComp.get(i)));
        }
    }

    public static long avg(List<Long> list) {
        if (list.isEmpty()) {
            return 0;
        }
        long res = 0;
        for (long l : list) {
            res += l;
        }
        return res / list.size();
    }

    public static Cipher initCipher(byte[] key) {
        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            SecretKeyFactory gen = SecretKeyFactory.getInstance("DES");
            Key keyComp = gen.generateSecret(new DESKeySpec(key));
            cipher.init(Cipher.ENCRYPT_MODE, keyComp);
            return cipher;
        } catch (Exception ignore) {
            return null;
        }
    }

    public static List<byte[]> initInputs(int start, int limit, int skip) {
        List<byte[]> inputs = new ArrayList<>();
        for (int i = start; i <= limit; i += skip) {
            inputs.add(mkArray(i));
        }
        return inputs;
    }

    public static byte[] mkArray(int len) {
        byte[] arr = new byte[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (byte) i;
        }
        return arr;
    }

}
