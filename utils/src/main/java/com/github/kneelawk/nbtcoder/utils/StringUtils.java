package com.github.kneelawk.nbtcoder.utils;

public class StringUtils {
	public static final String TAB = "\t";
	public static final char SPACE = ' ';

	public static String tabs(int n) {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < n; i++) {
			s.append(TAB);
		}
		return s.toString();
	}

	public static void tabs(int n, StringBuilder sb) {
		sb.append(TAB.repeat(Math.max(0, n)));
	}

	public static void spaces(int n, StringBuilder sb) {
		sb.append(String.valueOf(SPACE).repeat(Math.max(0, n)));
	}
}
