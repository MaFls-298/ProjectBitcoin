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

            case OP_DUP:
                byte[] top = stack.peek();
                stack.push(top);
                break;

            case OP_DROP:
                stack.pop();
                break;

            case OP_SWAP:
                byte[] a = stack.pop();
                byte[] b = stack.pop();
                stack.push(a);
                stack.push(b);
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

            case OP_RETURN:
                throw new ScriptException("Script failed, OP_RETURN");

            default:
                throw new ScriptException("a");
        }
    }

    private boolean isValid() {
        if (stack.isEmpty()) return false;
        return Stack.bytesToInt(stack.peek()) != 0;
    }
}
