package com.breedish.crypto.utils;

public abstract class ArrayOps {

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

    public static byte[] extractBlock(byte[] in, int blockLength, int blockIndex) {
        int from = Math.min(in.length, blockIndex * blockLength);
        int to = Math.min(in.length, from + blockLength);
        return java.util.Arrays.copyOfRange(in, from, to);
    }

    public static void replaceBlock(byte[] block, byte[] target, int blockIndex) {
        System.arraycopy(block, 0, target, block.length * blockIndex, block.length);
    }
}
