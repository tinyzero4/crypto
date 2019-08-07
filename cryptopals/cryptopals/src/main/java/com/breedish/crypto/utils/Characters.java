package com.breedish.crypto.utils;

public class Characters {

    public static boolean isEnglishCharacter(byte ch) {
        return (ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')
                || ch == ' ' || ch == '-' || ch == '\'' || ch == '\n' || ch == '/' || ch == ',' || ch == '.' || ch == '?';
    }

    public static int countCharacters(byte[] content) {
        int total = 0;
        for (int i = 0; i < content.length; i++) {
            byte ch = content[i];
            if (isEnglishCharacter(ch)) {
                total++;
            }

        }
        return total;
    }
}
