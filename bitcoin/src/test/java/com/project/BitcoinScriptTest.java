package com.project;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import java.util.ArrayList;
import java.util.List;


import com.project.ScriptException;



public class BitcoinScriptTest {

    private Token data(int value) {
        return new Token(intToBytes(value));
    }

    private byte[] intToBytes(int value) {
        if (value == 0) return new byte[]{};
        return new byte[]{(byte) value};
    }

    // opcodes - exitos 

    @Test
    void testOpAddSuccess() {
        BitcoinScript script = new BitcoinScript(false);

        List<Token> tokens = List.of(
                new Token(intToBytes(2)),
                new Token(intToBytes(3)),
                new Token(Opcode.OP_ADD)
        );

        assertTrue(script.execute(tokens));
    }

    @Test 
    void testOpEqualSuccess() {
        BitcoinScript script = new BitcoinScript(false);
        List<Token> tokens = List.of(
                new Token(intToBytes(5)),
                new Token(intToBytes(5)),
                new Token(Opcode.OP_EQUAL)
        );

        assertTrue(script.execute(tokens));
    }

    // opcode - fallos
    
    @Test 
    void testOpAddFailStackEmpty() {
        BitcoinScript script = new BitcoinScript(false);
        List<Token> tokens = List.of(
                new Token(Opcode.OP_ADD)
        );

        assertThrows(ScriptException.class, () -> script.execute(tokens));
    }

    @Test
    void testOpDupFailEmptyStack() {
        BitcoinScript script = new BitcoinScript(false);
        List<Token> tokens = List.of(
                new Token(Opcode.OP_DUP)
        );

        assertThrows(ScriptException.class, () -> script.execute(tokens));
    }

    @Test 
    void testOpVerifyFail() {
        BitcoinScript script = new BitcoinScript(false);
        List<Token> tokens = List.of(
                new Token(intToBytes(0)), // false
                new Token(Opcode.OP_VERIFY)
        );

        assertThrows(ScriptException.class, () -> script.execute(tokens));
    }

    // condicionales
    @Test
    void testIfTrueExecutes() {
        BitcoinScript script = new BitcoinScript(false);
        List<Token> tokens = List.of(
                new Token(intToBytes(1)), // true
                new Token(Opcode.OP_IF),
                    new Token(intToBytes(10)),
                new Token(Opcode.OP_ENDIF)
        );

        assertTrue(script.execute(tokens));
    }

    @Test
    void testIfFalseSkipsBlock() {
        BitcoinScript script = new BitcoinScript(false);
        List<Token> tokens = List.of(
                new Token(intToBytes(0)), // false
                new Token(Opcode.OP_IF),
                    new Token(intToBytes(10)), // NO se ejecuta
                new Token(Opcode.OP_ENDIF),
                new Token(intToBytes(1))
        );

        assertTrue(script.execute(tokens));
    }
    @Test
    void testNestedIfElse() {
        BitcoinScript script = new BitcoinScript(false);
        List<Token> tokens = List.of(
                new Token(intToBytes(1)), // true
                new Token(Opcode.OP_IF),

                    new Token(intToBytes(0)), // false
                    new Token(Opcode.OP_IF),
                        new Token(intToBytes(99)), // NO entra
                    new Token(Opcode.OP_ELSE),
                        new Token(intToBytes(1)), // entra aquí
                    new Token(Opcode.OP_ENDIF),

                new Token(Opcode.OP_ENDIF)
        );

        assertTrue(script.execute(tokens));
    }

    // edge cases 

    @Test
    void testEmptyStackAtEnd() {
        BitcoinScript script = new BitcoinScript(false);
        List<Token> tokens = new ArrayList<>();

        assertFalse(script.execute(tokens)); // stack vacío → false
    }

    @Test
    void testTypeHandlingNegativeNumber() {
        BitcoinScript script = new BitcoinScript(false);
        List<Token> tokens = List.of(
                new Token(intToBytes(-1)),
                new Token(intToBytes(1)),
                new Token(Opcode.OP_ADD)
        );

        assertTrue(script.execute(tokens));
    }

    @Test
    void testEqualVerifyFail() {
        BitcoinScript script = new BitcoinScript(false);
        List<Token> tokens = List.of(
                new Token(intToBytes(1)),
                new Token(intToBytes(2)),
                new Token(Opcode.OP_EQUALVERIFY)
        );

        assertThrows(ScriptException.class, () -> script.execute(tokens));
    }

}