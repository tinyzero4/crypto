package com.breedish.crypto.utils;

public abstract class Encoding {

    public static byte[] hex2Byte(String hex) {
        var res = new byte[hex.length() / 2];

        for (int i = 0; i < hex.length(); i += 2) {
            res[i / 2] = (byte) (Character.digit(hex.charAt(i), 16) << 4 | Character.digit(hex.charAt(i + 1), 16));
        }
        return res;
    }

    public static String byte2Hex(byte[] in) {
        var buffer = new StringBuilder();

        for (byte b : in) {
            buffer.append(Character.forDigit((b >> 4) & 0xF, 16));
            buffer.append(Character.forDigit(b & 0xF, 16));
        }

        return buffer.toString();
    }
}
