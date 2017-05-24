/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiralab.jcr.text_interface;

import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author ttiira
 */
public class TextInterface {

    private Scanner scan;
    private CryptoHandler crypt;

    /**
     *
     */
    public TextInterface() {
        this.scan = new Scanner(System.in);
        this.crypt = new CryptoHandler();
    }

    /**
     *
     */
    public void run() {
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
        if (command.equals("e")) {
            System.out.println("File to encrypt:");
            String s = this.scan.nextLine();
            System.out.println("Cipher:");
            String c = this.scan.nextLine();
            System.out.println("Keyfile:");
            String k = this.scan.nextLine();
            try {
                System.out.println(this.crypt.encrypt(s, k, c));
            } catch (IOException e) {
                System.out.println("Error reading file");
            }
        } else if (command.equals("d")) {

        } else if (command.equals("h")) {
            this.printHelpText();
        } else {
            System.out.println("Unknown command");
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
    }
}
