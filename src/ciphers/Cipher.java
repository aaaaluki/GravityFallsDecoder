/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ciphers;

import java.text.Normalizer;

/**
 *
 * @author luki
 */
public abstract class Cipher {

    String alphabet;

    public Cipher(String alphabet) {
        this.alphabet = alphabet;
    }

    public abstract String getName();

    public abstract String encrypt(String text, int key);

    public abstract String decrypt(String text, int key);

    protected String normalize(String text) {
        text = Normalizer.normalize(text, Normalizer.Form.NFD);
        text = text.replaceAll("[^\\p{ASCII}]", "");
        text = text.toUpperCase();

        return text;
    }
}
