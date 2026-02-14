package com.project;

public class Main {

    public static void main(String[] args) {

        ScriptInterpreter interpreter = new ScriptInterpreter();
        String scriptValido =
        "firmaValida clavePublica OP_DUP OP_HASH160 hashCorrecto OP_EQUALVERIFY OP_CHECKSIG";

        boolean resultado1 = interpreter.execute(scriptValido);

        System.out.println("Caso valido:");
        System.out.println("Script: " + scriptValido);
        if (resultado1) {
            System.out.println("El script es valido");
        } else {
            System.out.println("El script es invalido");
        }

        // p2pkh incorrecto
        ScriptInterpreter interpreter2 = new ScriptInterpreter();

        String scriptInvalido =
        "firmaValida clavePublica OP_DUP OP_HASH160 hashIncorrecto OP_EQUALVERIFY OP_CHECKSIG";

        boolean resultado2 = interpreter2.execute(scriptInvalido);

        System.out.println("Caso invalido:");
        System.out.println("Script: " + scriptInvalido);
        if (resultado2) {
            System.out.println("el script es valido");
        } else {
            System.out.println("el script es invalido");
        }
    }
}
