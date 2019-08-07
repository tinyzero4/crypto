package com.breedish.crypto.utils;

public abstract class Hamming {

    public static int hammingDistance(byte[] b1, byte[] b2) {
        int minLength = Math.min(b1.length, b2.length);
        int maxLength = Math.max(b1.length, b2.length);

        int distance = 0;
        for (int i = 0; i < minLength; i++) {
            distance += Integer.bitCount(b1[i] ^ b2[i]);
        }
        distance += 8 * (maxLength - minLength);
        return distance;
    }

}
