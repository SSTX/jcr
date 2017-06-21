/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiralab.jcr.main;

import java.util.Map;
import java.util.function.Function;
import java.util.function.ObjDoubleConsumer;
import tiralab.jcr.logic.Cryptographer;
import tiralab.jcr.text_interface.TextInterface;

/**
 *
 * @author ttiira
 */
public class Main {

    static Cryptographer g = new Cryptographer();

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        Cryptographer g = new Cryptographer();
        if (args.length < 4) {
            System.out.println("USAGE: jcr -d|-e inputFile outputFile keyFile");
            return;
        }
        String inputFilePath = null;
        String outputFilePath = null;
        String keyPath = null;
        String mode = "e";
        for (String s : args) {
            switch (s) {
                case "-d":
                    mode = "d";
                    break;
                case "-e":
                    mode = "e";
                    break;
                default:
                    if (inputFilePath == null) {
                        inputFilePath = s;
                    } else if (outputFilePath == null) {
                        outputFilePath = s;
                    } else if (keyPath == null) {
                        keyPath = s;
                    }
                    break;
            }
        }
        try {
            switch (mode) {
                case "d":
                    g.decrypt(inputFilePath, outputFilePath, keyPath, "DES", "ECB");
                    break;
                default:
                    g.encrypt(inputFilePath, outputFilePath, keyPath, "DES", "ECB");
                    break;
            }
        } catch (Exception e) {
            System.out.println(e.getCause());
        }
    }

}
