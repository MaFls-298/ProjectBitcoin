package com.project;

import java.util.ArrayList;
import java.util.List;







public class MainTest {

    public static void main(String[] args) {

        boolean trace = true;

        System.out.println("===== SIMULACION P2PKH =====");

        
        byte[] pubKey = new byte[]{11, 11, 11, 12};
        byte[] pubKeyHash = Hash.hash160(pubKey);
//utxo
        List<Token> scriptPubKey = crearScriptPubKey(pubKeyHash);

        TransactionOutput output = new TransactionOutput(scriptPubKey);
        UTXO utxo = new UTXO(output);


        ////////////////////  Caso válido/////////

        System.out.println("\n--- TRANSACCION VALIDA ---");

        byte[] signatureValida = Hash.hash160(pubKey);

        Transaction txValida = crearTransaccion(signatureValida, pubKey);

        validarTransaccion(txValida, utxo, trace);

        
        // ///////////////////// Caso inválido
        
        System.out.println("\n--- TRANSACCION INVALIDA ---");

        byte[] signatureInvalida = new byte[]{67, 67, 67, 67};

        Transaction txInvalida = crearTransaccion(signatureInvalida, pubKey);

        validarTransaccion(txInvalida, utxo, trace);
    }

    // locking script scrptpubkey
    private static List<Token> crearScriptPubKey(byte[] pubKeyHash) {

        List<Token> scriptPubKey = new ArrayList<>();

        scriptPubKey.add(new Token(Opcode.OP_DUP));
        scriptPubKey.add(new Token(Opcode.OP_HASH160));
        scriptPubKey.add(new Token(pubKeyHash));
        scriptPubKey.add(new Token(Opcode.OP_EQUALVERIFY));
        scriptPubKey.add(new Token(Opcode.OP_CHECKSIG));

        return scriptPubKey;
    }

    // crea transaccion con scriptsig
    private static Transaction crearTransaccion(byte[] signature, byte[] pubKey) {

        List<Token> scriptSig = new ArrayList<>();
        scriptSig.add(new Token(signature));
        scriptSig.add(new Token(pubKey));

        TransactionInput input = new TransactionInput(scriptSig);

        return new Transaction(
                List.of(input),
                new ArrayList<>()
        );
    }

    /// Validacion
    private static void validarTransaccion(Transaction tx, UTXO utxo, boolean trace) {

        BitcoinScript interpreter = new BitcoinScript(trace);

        for (TransactionInput input : tx.getInputs()) {

            try {
                boolean result = interpreter.executeTransaction(
                        input.getScriptSig(),
                        utxo.getOutput().getScriptPubKey()
                );

                System.out.println("Resultado: " + result);

            } catch (ScriptException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}