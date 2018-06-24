package com.pg2dev.model;

import com.pg2dev.util.Utils;

import java.security.PublicKey;

/**
 *
 * @author Patricio Iván Garcia Zitarosa <patriciogarcia1988@gmail.com>
 */
public class TOutput {

    private final String id;
    private final PublicKey recipient;
    private final float value;

    public TOutput(PublicKey r, float v, String prevId) {
        recipient = r;
        value = v;
        id = Utils.getSHA256(Utils.getStringFromKey(recipient) + Float.toString(value) + prevId);
    }

    public String getId() {
        return id;
    }

    public PublicKey getRecipient() {
        return recipient;
    }

    public float getValue() {
        return value;
    }
}
