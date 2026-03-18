package com.project;

public enum Opcode {
    OP_0,
    OP_1, 
    OP_2, 
    OP_3, 
    OP_4, 
    OP_5, 
    OP_6, 
    OP_7, 
    OP_8,
    OP_9, 
    OP_10, 
    OP_11, 
    OP_12, 
    OP_13, 
    OP_14, 
    OP_15, 
    OP_16,

    OP_ADD,
    OP_SUB,
    OP_NUMEQUALVERIFY,
    OP_LESSTHAN,
    OP_GREATERTHAN,
    OP_LESSTHANOREQUAL,
    OP_GREATERTHANOREQUAL,

    OP_DUP,
    OP_DROP,
    OP_SWAP,
    OP_OVER,

    OP_EQUAL,
    OP_EQUALVERIFY,
    OP_NOT,
    OP_BOOLAND,
    OP_BOOLOR,

    OP_IF,
    OP_NOTIF,
    OP_ELSE,
    OP_ENDIF,
    OP_VERIFY,
    OP_RETURN,

    OP_SHA256,
    OP_HASH160,
    OP_HASH256,
    OP_CHECKSIG,
    OP_CHECKSIGVERIFY,

    PUSHDATA;

    private Opcode() {
    }
}
