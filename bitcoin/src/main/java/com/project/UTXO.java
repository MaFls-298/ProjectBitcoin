package com.project;

public class UTXO {

    private TransactionOutput output;

    public UTXO(TransactionOutput output) {
        this.output = output;
    }

    public TransactionOutput getOutput() {
        return output;
    }
}
