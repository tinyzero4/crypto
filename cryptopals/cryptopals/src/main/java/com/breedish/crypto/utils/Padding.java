package com.breedish.crypto.utils;

import java.util.Arrays;

public abstract class Padding {

    public static byte[] paddingPKCS7(byte[] in, int blockSize) {
        int blocks = in.length / blockSize;
        int blocksPadded = (in.length % blockSize == 0) ? blocks : blocks + 1;

        int paddedLength = blocksPadded * blockSize;

        byte[] padded = Arrays.copyOf(in, paddedLength);
        Arrays.fill(padded, in.length, paddedLength, (byte) (paddedLength - in.length));
        return padded;
    }

    public static byte[] removePadding(byte[] in) {
        byte paddingLength = in[in.length - 1];
        if (in.length - paddingLength < 0)
            return in;

        return Arrays.copyOf(in, in.length - paddingLength);
    }

}
