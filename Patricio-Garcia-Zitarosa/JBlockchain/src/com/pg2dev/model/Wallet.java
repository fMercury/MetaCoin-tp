package com.pg2dev.model;

import com.pg2dev.util.Utils;
import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Patricio Iván Garcia Zitarosa <patriciogarcia1988@gmail.com>
 */
public class Wallet {

    private PrivateKey privateKey;
    private PublicKey publicKey;
    private HashMap<String, TOutput> transactions;

    private HashMap<String, TOutput> pendingTransactions = new HashMap<>();

    public Wallet() {
        generateKeys();
    }

    public void setTransactions(HashMap<String, TOutput> t) {
        transactions = t;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    private void generateKeys() {
        KeyPair keyPair = Utils.getKeyPair();
        if (keyPair != null) {
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        }
    }

    public float calculateBalance() {
        float total = 0;
        for (Map.Entry<String, TOutput> item : transactions.entrySet()) {
            TOutput trs = item.getValue();
            if (trs.getRecipient() == publicKey) {
                pendingTransactions.put(trs.getId(), trs);
                total += trs.getValue();
            }
        }
        return total;
    }

    public Transaction sendFunds(Wallet to, float value) {
        ArrayList<TInput> inputs = new ArrayList<>();

        float total = 0;
        for (Map.Entry<String, TOutput> item : pendingTransactions.entrySet()) {
            TOutput tOutput = item.getValue();
            total += tOutput.getValue();
            inputs.add(new TInput(tOutput.getId()));
            if (total > value) {
                break;
            }
        }

        Transaction newTransaction = new Transaction(transactions, this, to, value, inputs);
        newTransaction.generateSignature(this);

        for (TInput input : inputs) {
            pendingTransactions.remove(input.getTransactionOutputId());
        }

        return newTransaction;
    }
}
