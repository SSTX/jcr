/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiralab.jcr.main;

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
        System.out.println(System.getProperty("user.dir"));
        TextInterface t = new TextInterface();
        t.run();
    }
}
