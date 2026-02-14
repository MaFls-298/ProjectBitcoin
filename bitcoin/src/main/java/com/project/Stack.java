package com.project;

import java.util.ArrayDeque;
import java.util.Deque;

public class Stack {
        private Deque<byte[]> stack = new ArrayDeque<>();

    public void push(byte[] value) {
        stack.push(value);
    }

    public byte[] pop() {
        if (stack.isEmpty()) {
            throw new ScriptException("Stack underflow");
        }
        return stack.pop();
    }

    public byte[] peek() {
        if (stack.isEmpty()) {
            throw new ScriptException("Stack vacio");
        }
        return stack.peek();
    }

    public int size() {
        return stack.size();
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public void printStack() {
        System.out.print("STACK: ");
        for (byte[] element : stack) {
            System.out.print(bytesToInt(element) + " ");
        }
        System.out.println();
    }

    public static int bytesToInt(byte[] bytes) {
        if (bytes.length == 0) return 0;
        return bytes[0];
    }

    public static byte[] intToBytes(int value) {
        return new byte[]{(byte) value};
    }
}
    

