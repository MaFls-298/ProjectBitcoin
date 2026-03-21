package com.project;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class BitcoinScript {

    private Deque<byte[]> stack;
    private boolean trace;
    private Deque<Boolean> executionStack = new ArrayDeque<>();

    public BitcoinScript(boolean trace) {
        this.stack = new ArrayDeque<>();
        this.trace = trace;
    }

    private void executeOpcode(Opcode opcode) {

        switch (opcode) {

            case OP_DUP: //duplica superior
                if (stack.isEmpty()) {
                    throw new ScriptException("OP_DUP failed: stack empty");
                }
                stack.push(stack.peek().clone());
                break;

            case OP_DROP:
                if (stack.isEmpty()) {
                    throw new ScriptException("OP_DROP failed: stack empty");
                }
                stack.pop();
                break;

            case OP_ADD:
                if (stack.size() < 2) {
                    throw new ScriptException("OP_ADD failed: not enough elements");
                }
                int add1 = bytesToInt(stack.pop());
                int add2 = bytesToInt(stack.pop());
                stack.push(intToBytes(add1 + add2));
                break;

            case OP_SUB:
                if (stack.size() < 2) {
                    throw new ScriptException("OP_SUB failed: not enough elements");
                }
                int sub1 = bytesToInt(stack.pop());
                int sub2 = bytesToInt(stack.pop());
                stack.push(intToBytes(sub2 - sub1));
                break;

            case OP_EQUAL:
                if (stack.size() < 2) {
                    throw new ScriptException("OP_EQUAL failed: not enough elements");
                }
                byte[] v1 = stack.pop(); //compara los arrays completos
                byte[] v2 = stack.pop(); //push boolean 
                stack.push(intToBytes(java.util.Arrays.equals(v1, v2) ? 1 : 0));
                break;

            case OP_VERIFY:
                if (stack.isEmpty()) {
                    throw new ScriptException("OP_VERIFY failed: stack empty");
                }
                if (!toBool(stack.pop())) {
                    throw new ScriptException("OP_VERIFY failed");
                }
                break;

            case OP_EQUALVERIFY:
                if (stack.size() < 2) {
                    throw new ScriptException("OP_EQUALVERIFY failed: not enough elements");
                }
                byte[] ev1 = stack.pop();
                byte[] ev2 = stack.pop();

                if (!java.util.Arrays.equals(ev1, ev2)) {
                    throw new ScriptException("OP_EQUALVERIFY failed");
                }
                break;

            case OP_HASH160:
                if (stack.isEmpty()) {
                    throw new ScriptException("OP_HASH160 failed: stack empty");
                }
                byte[] data = stack.pop();
                stack.push(Hash.hash160(data));
                break;

            case OP_CHECKSIG:
                if (stack.size() < 2) {
                    throw new ScriptException("OP_CHECKSIG failed: not enough elements");
                }
                byte[] signature = stack.pop();
                byte[] pubKey = stack.pop();

                boolean valid = Hash.verifySignature(pubKey, signature);

                stack.push(intToBytes(valid ? 1 : 0));
                break;

            case OP_IF:
            case OP_NOTIF:

                boolean condition = false;

                if (shouldExecute()) {
                    if (stack.isEmpty()) {
                        throw new ScriptException("OP_IF failed: stack empty");
                    }

                    condition = toBool(stack.pop());

                    if (opcode == Opcode.OP_NOTIF) {
                        condition = !condition;
                    }
                }

                executionStack.push(condition);
                break;

            case OP_ELSE:

                if (executionStack.isEmpty()) {
                    throw new ScriptException("OP_ELSE without OP_IF");
                }

                boolean current = executionStack.pop();
                executionStack.push(!current);
                break;

            case OP_ENDIF:

                if (executionStack.isEmpty()) {
                    throw new ScriptException("OP_ENDIF without OP_IF");
                }

                executionStack.pop();
                break;

                default:
                    throw new ScriptException("Opcode ivnalido: " + opcode);
            }
        }

////////////////////////////////////////////////////////////////////////////



    public boolean execute(List<Token> script) {

        stack.clear();
        executionStack.clear();

        for (Token token : script) {

            if (token.isOpcode()) {

                Opcode op = token.getOpcode();

                if (op == Opcode.OP_IF || op == Opcode.OP_NOTIF ||
                    op == Opcode.OP_ELSE || op == Opcode.OP_ENDIF) {

                    executeOpcode(op);

                } else if (shouldExecute()) {
                    executeOpcode(op);
                }

            } else {
                if (shouldExecute()) {
                    stack.push(token.getData());
                }
            }

            if (trace) {
                System.out.println("Token: " + (token.isOpcode() ? token.getOpcode() : bytesToHex(token.getData())));
                printStack();
            }

            if (!executionStack.isEmpty()) {
                throw new ScriptException("Unclosed IF block");
                
            }
        }
        
        return isValid();
    }

    //crea lista nueva, recibe scriptsig y scriptpuubeky, concatena y ejecuta juntos
    public boolean executeTransaction(List<Token> scriptSig, List<Token> scriptPubKey) {

        List<Token> fullScript = new ArrayList<>();
        fullScript.addAll(scriptSig);
        fullScript.addAll(scriptPubKey);

        return execute(fullScript);
    }



    private boolean isValid() {
        if (stack.isEmpty()) return false;
        return toBool(stack.peek());
    }

    private void printStack() {
        System.out.print("STACK: ");
        for (byte[] e : stack) {
            System.out.print(bytesToHex(e) + " ");
            if (e.length == 0) {
                System.out.print("00 ");
}
        }
        System.out.println();
    }


    //little-endian
    private int bytesToInt(byte[] bytes) {

        if (bytes.length == 0) return 0;

        int result = 0;

        for (int i = 0; i < bytes.length; i++) {
            result |= (bytes[i] & 0xFF) << (8 * i);
        }

        if ((bytes[bytes.length - 1] & 0x80) != 0) {
            result &= ~(0x80 << (8 * (bytes.length - 1)));
            return -result;
        }

        return result;
    }

    private byte[] intToBytes(int value) {

        if (value == 0) return new byte[]{};

        boolean negative = value < 0;
        int absValue = Math.abs(value);

        ArrayList<Byte> result = new ArrayList<>();

        while (absValue > 0) {
            result.add((byte) (absValue & 0xFF));
            absValue >>= 8;
        }

        // bit de sign
        if ((result.get(result.size() - 1) & 0x80) != 0) {
            result.add((byte) (negative ? 0x80 : 0x00));
        } else if (negative) {
            result.set(result.size() - 1, (byte) (result.get(result.size() - 1) | 0x80));
        }
//array
        byte[] bytes = new byte[result.size()];
        for (int i = 0; i < result.size(); i++) {
            bytes[i] = result.get(i);
        }

        return bytes;
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    private boolean toBool(byte[] data) {
        for (int i = 0; i < data.length; i++) {
            if (data[i] != 0) {
                
                if (i == data.length - 1 && data[i] == (byte) 0x80) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    ///if else /////
    
    private boolean shouldExecute() {
        return executionStack.stream().allMatch(b -> b);
    }
}



