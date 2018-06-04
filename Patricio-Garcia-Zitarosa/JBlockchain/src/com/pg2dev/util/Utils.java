package com.pg2dev.util;

import com.google.gson.GsonBuilder;
import com.pg2dev.model.Transaction;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.Base64;

/**
 *
 * @author Patricio Iván Garcia Zitarosa <patriciogarcia1988@gmail.com>
 */
public class Utils {

    private static final GsonBuilder GSON = new GsonBuilder().setPrettyPrinting();

    public static final String getSHA256(String input) {
        try {
            MessageDigest SHA256 = MessageDigest.getInstance("SHA-256");
            byte[] hash = SHA256.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

// https://github.com/anonrig/bouncycastle-implementations/blob/master/ecdsa.java#L40
    public static final byte[] applyECDSASignature(PrivateKey key, String i) {
        Signature dsa;
        byte[] output = new byte[0];
        try {
            dsa = Signature.getInstance("ECDSA", "BC");
            dsa.initSign(key);
            byte[] strByte = i.getBytes();
            dsa.update(strByte);
            byte[] realSig = dsa.sign();
            output = realSig;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return output;
    }

    public static boolean verifyECDSASig(PublicKey key, String d, byte[] s) {
        try {
            Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
            ecdsaVerify.initVerify(key);
            ecdsaVerify.update(d.getBytes());
            return ecdsaVerify.verify(s);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static final String toJson(Object o) {
        return GSON.create().toJson(o);
    }

    public static final String getJson(Object o) {
        return GSON.create().toJson(o);
    }

    public static final String getZeros(int z) {
        return new String(new char[z]).replace('\0', '0');
    }

    public static final String getStringFromKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static final String getMerkle(ArrayList<Transaction> transactions) {
        int count = transactions.size();

        ArrayList<String> tree = new ArrayList<>();
        for (Transaction transaction : transactions) {
            tree.add(transaction.getTransactionId());
        }

        ArrayList<String> treeHashes = tree;
        while (count > 1) {
            treeHashes = new ArrayList<>();
            for (int i = 1; i < tree.size(); i += 2) {
                String leafHash = tree.get(i - 1) + tree.get(i);
                treeHashes.add(Utils.getSHA256(leafHash));
            }
            count = treeHashes.size();
            tree = treeHashes;
        }

        String merkleRoot = (treeHashes.size() == 1) ? treeHashes.get(0) : "";
        return merkleRoot;
    }

    // http://www.bouncycastle.org/wiki/display/JA1/Elliptic+Curve+Key+Pair+Generation+and+Key+Factories#EllipticCurveKeyPairGenerationandKeyFactories-FromNamedCurves
    public static KeyPair getKeyPair() {
        KeyPair keyPair = null;
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            keyGen.initialize(ecSpec, random);
            keyPair = keyGen.generateKeyPair();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return keyPair;
    }
}
