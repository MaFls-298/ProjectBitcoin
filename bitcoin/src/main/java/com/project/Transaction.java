package com.project;
import java.util.List;

public class Transaction {

    private List<TransactionInput> inputs;
    private List<TransactionOutput> outputs;

    public Transaction(List<TransactionInput> inputs, List<TransactionOutput> outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public List<TransactionInput> getInputs() {
        return inputs;
    }

    public List<TransactionOutput> getOutputs() {
        return outputs;
    }
}