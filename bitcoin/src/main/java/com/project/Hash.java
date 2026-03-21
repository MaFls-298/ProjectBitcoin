package com.project;

import java.util.Arrays;

public class Hash {


    public static byte[] reverse(byte[] input) {
        byte[] result = new byte[input.length];
        for (int i = 0; i < input.length; i++) {
            result[i] = input[input.length - 1 - i];
        }
        return result;
    }

    // hash160
    public static byte[] hash160(byte[] input) {
        byte[] reversed = reverse(input);

        byte[] result = new byte[Math.min(4, reversed.length)];
        System.arraycopy(reversed, 0, result, 0, result.length);

        return result;
    }

    // public key de private key
    public static byte[] derivePublicKey(byte[] privateKey) {
        return hash160(privateKey);
    }

    // Sign private key + message
        public static byte[] sign(byte[] privateKey, byte[] message) {
        byte[] pubKey = derivePublicKey(privateKey);

        byte[] combined = new byte[pubKey.length + message.length];
        System.arraycopy(pubKey, 0, combined, 0, pubKey.length);
        System.arraycopy(message, 0, combined, pubKey.length, message.length);

        return hash160(combined);
    }

    // Verify public key + message
    public static boolean verifySignature(byte[] pubKey, byte[] message, byte[] signature) {
        byte[] combined = new byte[pubKey.length + message.length];
        System.arraycopy(pubKey, 0, combined, 0, pubKey.length);
        System.arraycopy(message, 0, combined, pubKey.length, message.length);

        byte[] expected = hash160(combined);

        return Arrays.equals(expected, signature);
    }
}
