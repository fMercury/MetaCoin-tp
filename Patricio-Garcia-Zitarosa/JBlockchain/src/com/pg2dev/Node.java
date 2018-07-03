package com.pg2dev;

import com.pg2dev.model.Wallet;
import com.pg2dev.model.Block;
import com.pg2dev.model.Transaction;
import com.pg2dev.model.TOutput;
import com.pg2dev.util.Validator;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Patricio Iván Garcia Zitarosa <patriciogarcia1988@gmail.com>
 */
public class Node {

    private final IListener listener;

    public interface IListener {

        void log(String str);

        void onSenderBalanceChange(float value);

        void onrecipientBalanceChange(float value);
    }

    private ArrayList<Block> blockchain;
    private HashMap<String, TOutput> transactions;
    private int zeros;
    private Wallet walletBase;
    private Wallet sender;
    private Wallet recipient;
    private Transaction mainNode;

    public Node(IListener l) {
        listener = l;
        blockchain = new ArrayList<>();
        transactions = new HashMap<>();
        zeros = 3;
    }

    private Block getLastBlock() {
        int last = blockchain.size() - 1;
        return blockchain.get(last);
    }

    private void addToBlockchain(Block block) {
        listener.log("Bloque minado: " + block.mineBlock(zeros));
        blockchain.add(block);
    }

    public void initialize(float value) {
        mainNode = new Transaction(transactions, walletBase, sender, value, null);
        mainNode.generateSignature(walletBase);
        mainNode.setTransactionId("INITIAL_ID");

        ArrayList<TOutput> outputs = mainNode.getOutputs();

        outputs.add(new TOutput(mainNode.getrecipient(), mainNode.getAmount(), mainNode.getTransactionId()));
        transactions.put(outputs.get(0).getId(), outputs.get(0));

        Block block = new Block("INITIAL_HASH");
        if (block.addTransaction(mainNode)) {
            listener.log("Bloque genesis agregado.");
        }

        listener.onSenderBalanceChange(sender.calculateBalance());
        listener.onrecipientBalanceChange(recipient.calculateBalance());
        listener.log("---------------------------------------------");

        addToBlockchain(block);
    }

    public void addWrongBLock(float amount) {
        Block lastBlock = getLastBlock();
        Block newBlock = new Block(lastBlock.getHash().replaceAll("0", "1").replaceAll("2", "3").replaceAll("5", "8"));

        listener.log("Se quieren enviar " + amount);
        newBlock.addTransaction(sender.sendFunds(recipient, amount));
        addToBlockchain(newBlock);

        Boolean isValid = Validator.validateChain(zeros, blockchain, mainNode);
        if (isValid) {
            listener.onSenderBalanceChange(sender.calculateBalance());
            listener.onrecipientBalanceChange(recipient.calculateBalance());
        }
        listener.log("Es válida la blockchain? " + isValid);
        listener.log("---------------------------------------------");
    }

    public void send(float amount) {
        if (sender.calculateBalance() < amount) {
            listener.log("Fondos insuficientes: " + sender.calculateBalance());
        } else {
            Block lastBlock = getLastBlock();
            Block newBlock = new Block(lastBlock.getHash());

            listener.log("Se quieren enviar " + amount);
            newBlock.addTransaction(sender.sendFunds(recipient, amount));
            addToBlockchain(newBlock);

            listener.onSenderBalanceChange(sender.calculateBalance());
            listener.onrecipientBalanceChange(recipient.calculateBalance());

            listener.log("Es válida la blockchain? " + Validator.validateChain(zeros, blockchain, mainNode));
            listener.log("---------------------------------------------");
        }
    }

    public void setZeros(int z) {
        zeros = z;
    }

    public void setSender(Wallet s) {
        sender = s;
        sender.setTransactions(transactions);
    }

    public void setRecipient(Wallet r) {
        recipient = r;
        recipient.setTransactions(transactions);
    }

    public void setBaseWallet(Wallet w) {
        walletBase = w;
        walletBase.setTransactions(transactions);
    }

    public ArrayList<Block> getBlockchain() {
        return blockchain;
    }
}
