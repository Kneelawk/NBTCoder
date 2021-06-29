package com.github.kneelawk.nbtcoder.test.utils;

public class NewlineUtils {
    public static String stripCarriageReturn(String str) {
        return str.replace("\r\n", "\n");
    }
}
