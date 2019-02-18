package com.github.kneelawk.nbtcoder.utils;

public class ByteArrayUtils {
	public static boolean isZeros(byte[] data) {
		for (byte b : data) {
			if (b != 0) {
				return false;
			}
		}
		return true;
	}

	public static int lastNonZeroByte(byte[] data) {
		for (int i = data.length - 1; i >= 0; i--) {
			if (data[i] != 0) {
				return i;
			}
		}
		return -1;
	}
}
