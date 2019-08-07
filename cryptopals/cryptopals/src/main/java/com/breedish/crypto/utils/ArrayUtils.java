package com.breedish.crypto.utils;

import java.util.Arrays;

public abstract class ArrayUtils {

    public static byte[][] transpose(byte[] in, int blocksCount) {
        int blockSize = in.length / blocksCount;
        if (in.length % blocksCount != 0) blockSize++;
        byte[][] res = new byte[blocksCount][blockSize];

        for (int i = 0; i < in.length; i++) {
            int block = i % blocksCount;
            int blockIndex = i / blocksCount;
            res[block][blockIndex] = in[i];
        }
        return res;
    }

    public static byte[] resolveBlock(byte[] in, int blockLength, int blockIndex) {
        int from = Math.min(in.length, blockIndex * blockLength);
        int to = Math.min(in.length, from + blockLength);
        return Arrays.copyOfRange(in, from, to);
    }
}
