package com.project;

import java.util.List;

public class BitcoinScript {

    private Stack stack;
    private boolean trace;

    public BitcoinScript(boolean trace) {
        this.stack = new Stack();
        this.trace = trace;
    }

    public boolean execute(List<Token> script) {

        for (Token token : script) {

            if (token.isOpcode()) {
                executeOpcode(token.getOpcode());
            } else {
                stack.push(token.getData());
            }

            if (trace) {
                stack.printStack();
            }
        }

        return isValid();
    }

    private void executeOpcode(Opcode opcode) {

        switch (opcode) {

            case OP_0:
                stack.push(Stack.intToBytes(0));
                break;

            case OP_1:
                stack.push(Stack.intToBytes(1));
                break;

            case OP_2:
                stack.push(Stack.intToBytes(2));
                break;

            case OP_3:
                stack.push(Stack.intToBytes(3));
                break;

            case OP_4:
                stack.push(Stack.intToBytes(4));
                break;

            case OP_5:
                stack.push(Stack.intToBytes(5));
                break;

            case OP_6:
                stack.push(Stack.intToBytes(6));
                break;

            case OP_7:
                stack.push(Stack.intToBytes(7));
                break;

            case OP_8:
                stack.push(Stack.intToBytes(8));
                break;

            case OP_9:
                stack.push(Stack.intToBytes(9));    
                break;
            
            case OP_10:
                stack.push(Stack.intToBytes(10));
                break;

            case OP_11:
                stack.push(Stack.intToBytes(11));
                break;

            case OP_12:
                stack.push(Stack.intToBytes(12));
                break;

            case OP_13:
                stack.push(Stack.intToBytes(13));   
                break;

            case OP_14:
                stack.push(Stack.intToBytes(14));
                break;

            case OP_15:
                stack.push(Stack.intToBytes(15));  
                break;

            case OP_16:
                stack.push(Stack.intToBytes(16));
                break;

            case OP_DUP: //duplica top
                byte[] top = stack.peek();
                stack.push(top);
                break;

            case OP_DROP:
                stack.pop();
                break;

            case OP_ADD:
                int x = Stack.bytesToInt(stack.pop());
                int y = Stack.bytesToInt(stack.pop());
                stack.push(Stack.intToBytes(x + y));
                break;

            case OP_SUB:
                int sub1 = Stack.bytesToInt(stack.pop());
                int sub2 = Stack.bytesToInt(stack.pop());
                stack.push(Stack.intToBytes(sub2 - sub1));
                break;

            case OP_EQUAL:
                byte[] v1 = stack.pop();
                byte[] v2 = stack.pop();
                boolean equal = Stack.bytesToInt(v1) == Stack.bytesToInt(v2);
                stack.push(Stack.intToBytes(equal ? 1 : 0));
                break;

            case OP_VERIFY:
                int value = Stack.bytesToInt(stack.pop());
                if (value == 0) {
                    throw new ScriptException("OP_VERIFY failed");
                }
                break;

            case OP_HASH160:
                byte[] data = stack.pop();
                byte[] hashed = Hash.hash160(data);
                stack.push(hashed);
                break;

            case OP_CHECKSIG:
                byte[] signature = stack.pop();
                byte[] pubKey = stack.pop();

                boolean valid = Hash.verifySignature(pubKey, signature);

                stack.push(Stack.intToBytes(valid ? 1 : 0));
                break;

            case OP_CHECKSIGVERIFY:
                byte[] sig = stack.pop();
                byte[] key = stack.pop();

                boolean verif = Hash.verifySignature(key, sig);

                if (!verif) {
                    throw new ScriptException("CHECKSIGVERIFY failed");
                }
                break;

            case OP_EQUALVERIFY:
                byte[] eq1 = stack.pop();
                byte[] eq2 = stack.pop();
                boolean eqv = Stack.bytesToInt(eq1) == Stack.bytesToInt(eq2);
                if (!eqv) {
                    throw new ScriptException("EQUALVERIFY no son iguales");
                }
                break;

        
            default:
                throw new ScriptException("a");
        }
    }

    private boolean isValid() {
        if (stack.isEmpty()) return false;
        return Stack.bytesToInt(stack.peek()) != 0;
    }
}
