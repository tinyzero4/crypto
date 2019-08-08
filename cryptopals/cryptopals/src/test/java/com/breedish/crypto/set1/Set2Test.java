package com.breedish.crypto.set1;

import com.breedish.crypto.set2.Set2;
import com.breedish.crypto.utils.AES;
import com.breedish.crypto.utils.ArrayOps;
import com.breedish.crypto.utils.Encoding;
import com.breedish.crypto.utils.Padding;
import com.breedish.crypto.utils.RandomOps;
import org.apache.commons.io.Charsets;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static com.breedish.crypto.set2.Set2.encryptAESCBC;
import static com.breedish.crypto.set2.Set2.padString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class Set2Test {

    @Test
    public void testChallenge9() {
        assertThat(
                padString("YELLOW SUBMARINE", 20),
                is("YELLOW SUBMARINE\u0004\u0004\u0004\u0004")
        );
    }

    @Test
    public void testChallenge10() {
        String key = "THIS IS A KEY!!!";
        String iv = "IVIVIVIVIVIVIVIV";
        String message = "THIS IS A TEST TEXT";

        String encrypted = encryptAESCBC(Base64.getEncoder().encodeToString(message.getBytes()), key, iv);

        assertThat(Encoding.asString(Base64.getDecoder().decode(Set2.decryptAESCBC(encrypted, key, iv))), is(message));
    }

    @Test
    public void testChallenge11() {
        int blockSize = 16;

        for (int i = 0; i < 10; i++) {
            byte[] raw = new byte[blockSize * 20];
            Arrays.fill(raw, (byte) 1);
            String message = new String(raw);
            var result = Set2.encryptAESRandomModeRandomKey(message, blockSize);
            assertThat(Set2.isECBEncrypted(result.right, blockSize), is(result.left));
        }
    }

    @Test
    public void testBlockSizeDetection() {
        String key = "1234567890123456";
        String suffix = new String(Base64.getDecoder().decode("Um9sbGluJyBpbiBteSA1LjAKV2l0aCBteSByYWctdG9wIGRvd24gc28gbXkgaGFpciBjYW4gYmxvdwpUaGUgZ2lybGllcyBvbiBzdGFuZGJ5IHdhdmluZyBqdXN0IHRvIHNheSBoaQpEaWQgeW91IHN0b3A/IE5vLCBJIGp1c3QgZHJvdmUgYnkK"));

        var suffixBuilder = new StringBuilder();
        String message = "AAAAAAAAAAAAAAAA";
        byte[] raw = message.getBytes();
        byte[] hackMessage = Arrays.copyOfRange(raw, 0, raw.length - 1);

        Map<byte[], String> mapping = new HashMap<>();

        for (byte i = 0; i < Byte.MAX_VALUE; i++) {
            raw[raw.length - 1] = i;
            var res = oracleFunction(raw, key, suffix);
            mapping.put(ArrayOps.extractBlock(res, key.length(), 0), new String(raw));
        }

        while (!suffix.isEmpty()) {
            var res = oracleFunction(hackMessage, key, suffix);
            for (var e : mapping.entrySet()) {
                if (Arrays.equals(e.getKey(), ArrayOps.extractBlock(res, key.length(), 0))) {
                    suffixBuilder.append( e.getValue().substring(e.getValue().length() - 1));
                    break;
                }
            }
            suffix = suffix.substring(1);
        }

        System.out.println(suffixBuilder.toString());
    }

    private byte[] oracleFunction(byte[] messageRaw, String key, String suffix) {
        String plainText = new String(messageRaw) + suffix;
        byte[] raw = Padding.paddingPKCS7(plainText.getBytes(), key.length());

        return AES.encryptECB(raw, key.getBytes(), false);
    }

}
