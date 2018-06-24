package com.pg2dev.util;

import com.pg2dev.model.Block;
import com.pg2dev.model.Transaction;
import com.pg2dev.model.TInput;
import com.pg2dev.model.TOutput;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Patricio Iván Garcia Zitarosa <patriciogarcia1988@gmail.com>
 */
public class Validator {

    public static Boolean validateChain(int zeros, ArrayList<Block> blockchain, Transaction genesisTransaction) {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[zeros]).replace('\0', '0');
        HashMap<String, TOutput> aux = new HashMap<>();
        ArrayList<TOutput> outputs = genesisTransaction.getOutputs();
        aux.put(outputs.get(0).getId(), outputs.get(0));

        for (int i = 1; i < blockchain.size(); i++) {
            previousBlock = blockchain.get(i - 1);
            currentBlock = blockchain.get(i);

            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                return false;
            }

            if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
                return false;
            }

            if (!currentBlock.getHash().substring(0, zeros).equals(hashTarget)) {
                return false;
            }

            TOutput tempOutput;
            ArrayList<Transaction> transactions = currentBlock.getTransactions();
            for (int t = 0; t < transactions.size(); t++) {
                Transaction currentTransaction = transactions.get(t);

                if (!currentTransaction.verifySignature()) {
                    return false;
                }
                if (currentTransaction.getIncomings() != currentTransaction.getOutcomings()) {
                    return false;
                }

                for (TInput input : currentTransaction.getInputs()) {
                    tempOutput = aux.get(input.getTransactionOutputId());
                    if (tempOutput != null) {
                        if (input.getUnspentTransactions().getValue() != tempOutput.getValue()) {
                            return false;
                        }

                        aux.remove(input.getTransactionOutputId());
                    } else {
                        return false;
                    }
                }

                outputs = currentTransaction.getOutputs();
                for (TOutput output : outputs) {
                    aux.put(output.getId(), output);
                }

                if (outputs.get(0).getRecipient() != currentTransaction.getrecipient()) {
                    return false;
                }
                if (outputs.get(1).getRecipient() != currentTransaction.getSender()) {
                    return false;
                }
            }
        }
        return true;
    }
}
