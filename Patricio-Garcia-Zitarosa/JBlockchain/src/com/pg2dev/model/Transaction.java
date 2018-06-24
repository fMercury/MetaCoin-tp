package com.pg2dev.model;

import com.pg2dev.util.Utils;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Patricio Iván Garcia Zitarosa <patriciogarcia1988@gmail.com>
 */
public class Transaction {

    private String transactionId;
    private ArrayList<TInput> inputs = new ArrayList<>();
    private ArrayList<TOutput> outputs = new ArrayList<>();
    private HashMap<String, TOutput> transactions;

    private PublicKey sender;
    private PublicKey recipient;
    private float amount;
    private byte[] signature;

    public Transaction(HashMap<String, TOutput> t, Wallet from, Wallet to, float v, ArrayList<TInput> i) {
        this.transactions = t;
        this.sender = from.getPublicKey();
        this.recipient = to.getPublicKey();
        this.amount = v;
        this.inputs = i;
    }

    public boolean processTransaction() {
        if (verifySignature() == false) {
            return false;
        }

        for (TInput input : inputs) {
            input.setUnspentTransactions(transactions.get(input.getTransactionOutputId()));
        }

        float amount = getIncomings() - this.amount;
        transactionId = calulateHash();
        outputs.add(new TOutput(this.recipient, this.amount, transactionId));
        outputs.add(new TOutput(this.sender, amount, transactionId));

        for (TOutput output : outputs) {
            transactions.put(output.getId(), output);
        }

        for (TInput input : inputs) {
            if (input.getUnspentTransactions() == null) {
                continue;
            }
            transactions.remove(input.getUnspentTransactions().getId());
        }

        return true;
    }

    public float getIncomings() {
        float total = 0;
        for (TInput input : inputs) {
            if (input.getUnspentTransactions() == null) {
                continue;
            }
            total += input.getUnspentTransactions().getValue();
        }
        return total;
    }

    public float getOutcomings() {
        float total = 0;
        for (TOutput output : outputs) {
            total += output.getValue();
        }
        return total;
    }

    public void generateSignature(Wallet w) {
        String data = Utils.getStringFromKey(sender) + Utils.getStringFromKey(recipient) + Float.toString(amount);
        signature = Utils.applyECDSASignature(w.getPrivateKey(), data);
    }

    public boolean verifySignature() {
        String data = Utils.getStringFromKey(sender) + Utils.getStringFromKey(recipient) + Float.toString(amount);
        return Utils.verifyECDSASig(sender, data, signature);
    }

    private String calulateHash() {
        String hash = Utils.getSHA256(Utils.getStringFromKey(sender) + Utils.getStringFromKey(recipient) + Float.toString(amount));
        return hash;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String id) {
        this.transactionId = id;
    }

    public PublicKey getSender() {
        return sender;
    }

    public PublicKey getrecipient() {
        return recipient;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float a) {
        this.amount = a;
    }

    public ArrayList<TInput> getInputs() {
        return inputs;
    }

    public ArrayList<TOutput> getOutputs() {
        return outputs;
    }

    public void setOutputs(ArrayList<TOutput> o) {
        this.outputs = o;
    }
}
