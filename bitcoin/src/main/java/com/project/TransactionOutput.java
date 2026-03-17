package com.project;

import java.util.List;

public class TransactionOutput {
    private List<Token> scriptPubKey;

    public TransactionOutput(List<Token> scriptPubKey) {
        this.scriptPubKey = scriptPubKey;
    }

    public List<Token> getScriptPubKey() {
        return scriptPubKey;
    }
}
