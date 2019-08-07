package com.breedish.crypto.utils;

public abstract class XORUtils {

    public static byte[] xor(byte[] left, byte[] right) {
        byte[] shorter = left;
        byte[] longer = right;

        if (left.length > right.length) {
            shorter = right;
            longer = left;
        }

        byte[] res = new byte[longer.length];

        for (int i = 0; i < longer.length; i++) {
            res[i] = (byte) (longer[i] ^ shorter[i % shorter.length]);
        }

        return res;
    }

}
