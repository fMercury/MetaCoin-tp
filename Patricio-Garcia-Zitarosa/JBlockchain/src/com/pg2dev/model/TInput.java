package com.pg2dev.model;

/**
 *
 * @author Patricio Iván Garcia Zitarosa <patriciogarcia1988@gmail.com>
 */
public class TInput {

    private final String id;
    private TOutput transactions;

    public TInput(String i) {
        id = i;
    }

    public String getTransactionOutputId() {
        return id;
    }

    public TOutput getUnspentTransactions() {
        return transactions;
    }

    public void setUnspentTransactions(TOutput u) {
        transactions = u;
    }
}
