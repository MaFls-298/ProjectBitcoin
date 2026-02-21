package com.project;

import java.util.ArrayList;
import java.util.List;

public class MainTest {

    public static void main(String[] args) {

        boolean trace = true;

        System.out.println("\n validod");
        ejecutarCaso(true, trace);

        System.out.println("\n no valido");
        ejecutarCaso(false, trace);
    }

    private static void ejecutarCaso(boolean firmaValida, boolean trace) {

        List<Token> script = new ArrayList<>();

        byte[] pubKey = new byte[]{11, 11, 11, 12};

        byte[] pubKeyHash = Hash.hash160(pubKey);

        byte[] signature;

        if (firmaValida) {
            signature = Hash.hash160(pubKey); 
        } else {
            signature = new byte[]{99, 99, 99, 99}; 
        }

        // =scriptsig
        script.add(new Token(signature));
        script.add(new Token(pubKey));

        // pubkey
        script.add(new Token(Opcode.OP_DUP));
        script.add(new Token(Opcode.OP_HASH160));
        script.add(new Token(pubKeyHash));
        script.add(new Token(Opcode.OP_EQUALVERIFY));
        script.add(new Token(Opcode.OP_CHECKSIG));

        BitcoinScript interpreter = new BitcoinScript(trace);

        try {
            boolean result = interpreter.execute(script);
            System.out.println("Resultado final: " + result);
        } catch (ScriptException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}