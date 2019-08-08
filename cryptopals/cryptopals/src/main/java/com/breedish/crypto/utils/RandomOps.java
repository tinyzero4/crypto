package com.breedish.crypto.utils;

import java.security.SecureRandom;
import java.util.Arrays;

public class RandomOps {

    static SecureRandom random = new SecureRandom();

    public static byte[] randomBytes(int length) {
        var random = new SecureRandom();
        var res = new byte[length];
        random.nextBytes(res);
        return res;
    }

    public static int random(int minValue, int maxValue) {
        return minValue + random.nextInt(maxValue);
    }

    public static int random(int maxValue) {
        return random.nextInt(maxValue);
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(randomBytes(6)));
        System.out.println(Arrays.toString(randomBytes(1)));
    }
}
