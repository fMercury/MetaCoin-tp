package com.pg2dev.model;

import com.pg2dev.util.Utils;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Patricio Iván Garcia Zitarosa <patriciogarcia1988@gmail.com>
 */
public class Block {

    private String hash;
    private String previousHash;
    private String merkleHash;
    private ArrayList<Transaction> transactions;
    private long timeStamp;
    private int nonce;

    public Block(String p) {
        previousHash = p;
        transactions = new ArrayList<>();
        timeStamp = new Date().getTime();

        hash = calculateHash();
    }

    public String calculateHash() {
        String calculatedhash = Utils.getSHA256(previousHash + Long.toString(timeStamp) + Integer.toString(nonce) + merkleHash);
        return calculatedhash;
    }

    //  TODO: Agregar los ceros al inicio hasta que el hash sea alcanzado
    public String mineBlock(int zeros) {
        merkleHash = Utils.getMerkle(transactions);
        String temp = Utils.getZeros(zeros);
        while (!hash.substring(0, zeros).equals(temp)) {
            nonce++;
            hash = calculateHash();
        }
        return hash;
    }

    public boolean addTransaction(Transaction transaction) {
        if (!previousHash.equals("INITIAL_HASH") && (transaction.processTransaction() != true)) {
            return false;
        }

        transactions.add(transaction);

        return true;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String h) {
        hash = h;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String p) {
        previousHash = p;
    }

    public String getMerkleRoot() {
        return merkleHash;
    }

    public void setMerkleRoot(String m) {
        merkleHash = m;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long t) {
        timeStamp = t;
    }

    @Override
    public String toString() {
        super.toString();
        return "Hash previo: " + previousHash + "\nHash: " + hash;
    }
}
