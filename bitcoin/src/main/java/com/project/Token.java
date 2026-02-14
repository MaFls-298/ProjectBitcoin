package com.project;

public class Token {
    

    private Opcode opcode;
    private byte[] data;

    // Constructor para opcode
    public Token(Opcode opcode) {
        this.opcode = opcode;
    }

    
    public Token(byte[] data) {
        this.data = data;
    }

    public boolean isOpcode() {
        return opcode != null;
    }

    public Opcode getOpcode() {
        return opcode;
    }

    public byte[] getData() {
        return data;
    }
}

    

