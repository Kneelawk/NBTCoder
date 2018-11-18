package com.github.kneelawk.nbtcoder;

public class StringUtils {
	public static String tabs(int n) {
		String s = "";
		for (int i = 0; i < n; i++) {
			s += "\t";
		}
		return s;
	}
}
