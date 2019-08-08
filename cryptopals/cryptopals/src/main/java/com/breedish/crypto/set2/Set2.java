package com.breedish.crypto.set2;

import com.breedish.crypto.utils.AES;
import com.breedish.crypto.utils.ArrayOps;
import com.breedish.crypto.utils.Encoding;
import com.breedish.crypto.utils.Padding;

import java.util.Arrays;
import java.util.Base64;

import static com.breedish.crypto.utils.RandomOps.random;
import static com.breedish.crypto.utils.RandomOps.randomBytes;

public class Set2 {

    public static String padString(String in, int blockSize) {
        return Encoding.asString(Padding.paddingPKCS7(in.getBytes(), blockSize));
    }

    public static String encryptAESCBC(String in, String key, String iv) {
        var decoded = Base64.getDecoder().decode(in);
        var encrypted = AES.encryptCBC(decoded, key.getBytes(), iv.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decryptAESCBC(String in, String key, String iv) {
        var decoded = Base64.getDecoder().decode(in);
        var decrypted = AES.decryptCBC(decoded, key.getBytes(), iv.getBytes());
        var decryptedNoPadding = Padding.removePadding(decrypted);
        return Base64.getEncoder().encodeToString(decryptedNoPadding);
    }

    public static Tuple<Boolean, String> encryptAESRandomModeRandomKey(String in, int blockSize) {
        byte[] raw = in.getBytes();
        byte[] key = randomBytes(blockSize);

        int prefixLength = random(5, 10);
        int postfixLength = random(5, 10);

        byte[] container = Padding.paddingPKCS7(randomBytes(raw.length + prefixLength + postfixLength), key.length);
        System.arraycopy(raw, 0, container, prefixLength, raw.length);

        byte[] iv = randomBytes(blockSize);

        boolean useECB = random(2) == 0;

        byte[] encrypted = useECB ? AES.encryptECB(container, key, false) : AES.encryptCBC(container, key, iv);
        Tuple<Boolean, String> res = new Tuple<>();
        res.left = useECB;
        res.right = Encoding.asString(encrypted);
        return res;
    }

    public static boolean isECBEncrypted(String in, int blockSize) {
        byte[] raw = in.getBytes();
        int blocksCount = raw.length / blockSize;

        int sameBlock = 0;
        for (int i = 0; i < blocksCount; i++) {
            byte[] block = ArrayOps.extractBlock(raw, blockSize, i);
            for (int j = i + 1; j < blocksCount; j++) {
                if (Arrays.equals(block, ArrayOps.extractBlock(raw, blockSize, j))) {
                    sameBlock++;
                }
            }
        }
        return sameBlock > 0;
    }



    public static class Tuple<T1, T2> {
        public T1 left;
        public T2 right;
    }

}
