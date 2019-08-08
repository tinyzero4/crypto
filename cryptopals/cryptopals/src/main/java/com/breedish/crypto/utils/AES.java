package com.breedish.crypto.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public abstract class AES {

    public static byte[] encryptECB(byte[] in, byte[] key, boolean usePadding) {
        return doECB(in, key, usePadding, Cipher.ENCRYPT_MODE);
    }

    public static byte[] decryptECB(byte[] in, byte[] key, boolean usePadding) {
        byte[] res = doECB(in, key, usePadding, Cipher.DECRYPT_MODE);
        if (usePadding) res = Padding.removePadding(res);
        return res;
    }

    private static byte[] doECB(byte[] in, byte[] key, boolean usePadding, int mode) {
        try {
            Cipher cipher = Cipher.getInstance(usePadding ? "AES/ECB/PKCS5Padding" : "AES/ECB/NoPadding");
            SecretKey secretKey = new SecretKeySpec(key, "AES");
            cipher.init(mode, secretKey);
            return cipher.doFinal(in);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static byte[] encryptCBC(byte[] in, byte[] key, byte[] iv) {
        int blockLength = key.length;
        byte[] padded = Padding.paddingPKCS7(in, key.length);

        int blocksCount = padded.length / blockLength;

        byte[] result = new byte[padded.length];

        byte[] prevBlock = iv;
        for (int i = 0; i < blocksCount; i++) {
            byte[] block = ArrayOps.extractBlock(padded, blockLength, i);
            prevBlock = encryptECB(XOR.xor(prevBlock, block), key, false);
            ArrayOps.replaceBlock(prevBlock, result, i);
        }
        return result;
    }

    public static byte[] decryptCBC(byte[] in, byte[] key, byte[] iv) {
        int blocksCount = in.length / key.length;
        int blockLength = key.length;

        byte[] result = new byte[in.length];
        for (int i = blocksCount - 1; i >= 0; i--) {
            byte[] block = decryptECB(ArrayOps.extractBlock(in, blockLength, i), key, false);
            byte[] prevBlock = i == 0 ? iv : ArrayOps.extractBlock(in, blockLength, i - 1);
            ArrayOps.replaceBlock(XOR.xor(prevBlock, block), result, i);
        }
        return result;
    }

}
