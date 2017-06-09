/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiralab.jcr.text_interface;

import tiralab.jcr.logic.Cryptographer;
import java.io.IOException;
import java.util.Scanner;
import tiralab.jcr.logic.BitFunctions;

/**
 *
 * @author ttiira
 */
public class TextInterface {

    private Scanner scan;
    private Cryptographer crypt;

    /**
     *
     */
    public TextInterface() {
        this.scan = new Scanner(System.in);
        this.crypt = new Cryptographer();
    }

    /**
     *
     */
    public void run() {
        this.printHelpText();
        while (true) {
            System.out.print("> ");
            String input = this.scan.nextLine();
            if (input.equals("q")) {
                System.out.println("Exiting...");
                break;
            }
            this.handleCommand(input);
        }
    }

    /**
     *
     * @param command
     */
    public void handleCommand(String command) {
        switch (command) {
            case "e":
                this.processData("e");
                break;
            case "d":
                this.processData("d");
                break;
            case "h":
                this.printHelpText();
                break;
            default:
                System.out.println("Unknown command.");
                break;
        }
    }

    /**
     *
     * @param mode
     */
    public void processData(String mode) {
        System.out.println("Input file:");
        String i = this.scan.nextLine();
        System.out.println("Output file");
        String o = this.scan.nextLine();
        System.out.println("Cipher:");
        String c = this.scan.nextLine();
        System.out.println("Mode of operation");
        String m = this.scan.nextLine();
        System.out.println("Keyfile:");
        String k = this.scan.nextLine();
        try {
            switch (mode.toLowerCase()) {
                case "d":
                    this.crypt.decrypt(i, o, k, c, m);
                    break;
                case "e":
                    this.crypt.encrypt(i, o, k, c, m);
                    break;
            }
        } catch (IOException e) {
            System.out.println("IOError: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Bad data: " + e.getMessage());
        }

    }

    /**
     *
     */
    public void printHelpText() {
        System.out.println("The following commands are supported:");
        System.out.println("e\tencrypt");
        System.out.println("d\tdecrypt");
        System.out.println("h\tprint this help text");
        System.out.println("q\tquit");
    }
}
