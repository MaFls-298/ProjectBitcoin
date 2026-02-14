package com.project;
import java.util.Stack;

public class ScriptInterpreter {

    private Stack<String> pila;

    public ScriptInterpreter() {
        pila = new Stack<>();
    }

    public boolean execute(String script) {

        String[] tokens = script.split(" ");

        for (String token : tokens) {

            if (token.equals("OP_DUP")) {
                opDup();
            }

            else if (token.equals("OP_HASH160")) {
                opHash160();
            }

            else if (token.equals("OP_EQUALVERIFY")) {
                opEqualVerify();
            }

            else if (token.equals("OP_CHECKSIG")) {
                opCheckSig();
            }

            else {
                pila.push(token);
            }
        }

        if (!pila.isEmpty() && pila.peek().equals("TRUE")) {
            return true;
        }

        return false;
    }

    private void opDup() {
        if (!pila.isEmpty()) {
            pila.push(pila.peek());
        }
    }

    private void opHash160() {
        if (!pila.isEmpty()) {
            String data = pila.pop();
            pila.push(hash160(data));
        }
    }

    private void opEqualVerify() {

        if (pila.size() >= 2) {

            String a = pila.pop();
            String b = pila.pop();

            if (!a.equals(b)) {
                pila.clear();
            }
        }
    }

    private void opCheckSig() {

        if (pila.size() >= 2) {

            String pubKey = pila.pop();
            String firma = pila.pop();

            if (checkSig(firma, pubKey)) {
                pila.push("TRUE");
            } else {
                pila.push("FALSE");
            }
        }
    }

    private String hash160(String data) {

        if (data.equals("clavePublica")) {
            return "hashCorrecto";
        }

        return "hashIncorrecto";
    }

    private boolean checkSig(String firma, String clave) {

        if (firma.equals("firmaValida") && clave.equals("clavePublica")) {
            return true;
        }

        return false;
    }
}
