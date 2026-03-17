package com.project;

import java.util.ArrayList;
import java.util.List;



public class MainTest {

    public static void main(String[] args) {

        boolean trace = true;

        System.out.println("=== Simulacion de transaccion ===");

        // original
        byte[] pubKey = new byte[]{11,11,11,12};
        byte[] pubKeyHash = Hash.hash160(pubKey);

        // scriptPubKey)
        List<Token> scriptPubKey = new ArrayList<>();
        scriptPubKey.add(new Token(Opcode.OP_DUP));
        scriptPubKey.add(new Token(Opcode.OP_HASH160));
        scriptPubKey.add(new Token(pubKeyHash));
        scriptPubKey.add(new Token(Opcode.OP_EQUALVERIFY));
        scriptPubKey.add(new Token(Opcode.OP_CHECKSIG));

        TransactionOutput output = new TransactionOutput(scriptPubKey);

        // la salida existente en el sistema
        UTXO utxo = new UTXO(output);

        System.out.println("\n--- intento valido ---");
        ejecutarTransaccion(pubKey, Hash.hash160(pubKey), utxo, trace);

        System.out.println("\n--- intento invalido ---");
        ejecutarTransaccion(pubKey, new byte[]{99,99,99,99}, utxo, trace);
    }

    private static void ejecutarTransaccion(byte[] pubKey, byte[] signature,
                                            UTXO utxo, boolean trace) {

        List<Token> scriptSig = new ArrayList<>();
        scriptSig.add(new Token(signature));
        scriptSig.add(new Token(pubKey));

        TransactionInput input = new TransactionInput(scriptSig);

        BitcoinScript interpreter = new BitcoinScript(trace);

        boolean result = interpreter.executeTransaction(
                input.getScriptSig(),
                utxo.getOutput().getScriptPubKey()
        );

        System.out.println("Transaccion valida: " + result);
    }
}