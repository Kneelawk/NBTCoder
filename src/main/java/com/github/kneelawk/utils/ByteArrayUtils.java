package com.github.kneelawk.utils;

public class ByteArrayUtils {
	public static boolean isZeros(byte[] data) {
		for (byte b : data) {
			if (b != 0) {
				return false;
			}
		}
		return true;
	}
}
