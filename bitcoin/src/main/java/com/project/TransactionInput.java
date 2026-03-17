package com.project;

import java.util.List;

public class TransactionInput {
    private List<Token> scriptSig;

    public TransactionInput(List<Token> scriptSig) {
        this.scriptSig = scriptSig;
    }

    public List<Token> getScriptSig() {
        return scriptSig;
    }
}
