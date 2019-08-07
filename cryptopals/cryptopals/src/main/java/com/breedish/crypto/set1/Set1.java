package com.breedish.crypto.set1;

import com.breedish.crypto.utils.ArrayUtils;
import com.breedish.crypto.utils.Characters;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

import static com.breedish.crypto.utils.ArrayUtils.resolveBlock;
import static com.breedish.crypto.utils.Encoding.byte2Hex;
import static com.breedish.crypto.utils.Encoding.hex2Byte;
import static com.breedish.crypto.utils.Hamming.hammingDistance;
import static com.breedish.crypto.utils.XORUtils.xor;
import static java.nio.charset.StandardCharsets.UTF_8;

class Set1 {

    static String hex2base64(String in) {
        return Base64.getEncoder().encodeToString(hex2Byte(in));
    }

    static String fixedXOR(String in1, String in2) {
        return byte2Hex(xor(hex2Byte(in1), hex2Byte(in2)));
    }

    static byte[] decryptSingleCharXORCipher(String in) {
        return decryptSingleCharXORCipher(hex2Byte(in)).decoded;
    }

    static DecryptRes<Character> decryptSingleCharXORCipher(byte[] in) {
        int maxCount = 0;
        byte[] decoded = new byte[]{};
        char key = 0;

        for (char c = 0; c < 255; c++) {
            byte[] decodedCandidate = xor(in, new byte[]{(byte) c});
            int count = Characters.countCharacters(decodedCandidate);
            if (count > maxCount) {
                maxCount = count;
                decoded = decodedCandidate;
                key = c;
            }
        }
        var res = new DecryptRes<Character>();
        res.decoded = decoded;
        res.key = key;
        return res;
    }

    static class DecryptRes<KEY> {
        KEY key;
        byte[] decoded;
    }

    static String repeatingXOR(String in, String key) {
        return byte2Hex(xor(in.getBytes(), key.getBytes()));
    }

    static String decryptRepeatingXORCipher(String in) {
        var raw = Base64.getDecoder().decode(in.getBytes(UTF_8));

        double minDistance = Integer.MAX_VALUE;
        int keySize = Integer.MAX_VALUE;

        for (int i = 2; i <= 40; i++) {
            int blocksCount = raw.length / i;
            int blockLength = raw.length / blocksCount;
            double distance = 0;
            for (int j = 0; j < blocksCount - 1; j++) {
                distance += 1.0 * hammingDistance(resolveBlock(raw, i, j), resolveBlock(raw, i, j + 1)) / blockLength;
            }
            distance /= blocksCount;

            if (distance < minDistance) {
                minDistance = distance;
                keySize = i;
            }
        }

        int blocksCount = keySize;

        var blocks = ArrayUtils.transpose(raw, blocksCount);

        var keyBuilder = new StringBuilder();
        for (int i = 0; i < blocksCount; i++) {
            keyBuilder.append(decryptSingleCharXORCipher(blocks[i]).key);
        }

        var key = keyBuilder.toString();
        return new String(xor(raw, key.getBytes()));
    }

    static String decryptAESECB(String in, String key) throws Exception {
        var raw = Base64.getDecoder().decode(in.getBytes(UTF_8));

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKey secretKey = new SecretKeySpec(key.getBytes(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return new String(cipher.doFinal(raw), UTF_8);
    }
}
