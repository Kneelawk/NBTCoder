package com.github.kneelawk.nbtcoder.utils;

public class StringUtils {
	public static final String TAB = "\t";
	public static final char SPACE = ' ';

	public static String tabs(int n) {
		String s = "";
		for (int i = 0; i < n; i++) {
			s += TAB;
		}
		return s;
	}

	public static void tabs(int n, StringBuilder sb) {
		for (int i = 0; i < n; i++) {
			sb.append(TAB);
		}
	}

	public static void spaces(int n, StringBuilder sb) {
		for (int i = 0; i < n; i++) {
			sb.append(SPACE);
		}
	}
}
