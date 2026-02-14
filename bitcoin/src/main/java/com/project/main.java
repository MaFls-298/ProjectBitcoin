package com.project;

import java.util.ArrayList;
import java.util.List;

public class main {
    public static void main(String[] args) {

        boolean trace = true;

        List<Token> scriptList = new ArrayList<>();

        // 2 3 add 5 EQ
        scriptList.add(new Token(Opcode.OP_2));
        scriptList.add(new Token(Opcode.OP_3));
        scriptList.add(new Token(Opcode.OP_ADD));
        scriptList.add(new Token(new byte[]{5}));
        scriptList.add(new Token(Opcode.OP_EQUAL));

        BitcoinScript script = new BitcoinScript(trace);

        try {
            boolean result = script.execute(scriptList);
            System.out.println("Resultado final: " + result);
        } catch (ScriptException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
