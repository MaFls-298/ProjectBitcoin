package com.project;

import java.util.Arrays;

public class Hash {
    // 160
    public static byte[] hash160(byte[] input) {
        // return primeros 4 bytes invertidos
        byte[] result = new byte[Math.min(4, input.length)];
        for (int i = 0; i < result.length; i++) {
            result[i] = input[result.length - 1 - i];
        }
        return result;
    }

    
    public static boolean verifySignature(byte[] pubKey, byte[] signature) {

        // signature = hash160 (pubKey)

        byte[] expected = hash160(pubKey);
        return Arrays.equals(expected, signature);
    }
}
