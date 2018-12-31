package com.github.kneelawk.utils;

import static com.github.kneelawk.utils.StringUtils.spaces;

public class ByteArrayUtils {
	public static boolean isZeros(byte[] data) {
		for (byte b : data) {
			if (b != 0) {
				return false;
			}
		}
		return true;
	}

	public static String toHex(byte[] data, int offset) {
		StringBuilder sb = new StringBuilder();
		spaces((offset % 0x10) * 3 + (offset % 0x10 <= 7 ? 0 : 1), sb);
		for (int i = 0; i < data.length; i++) {
			int b = i + offset;
			sb.append(String.format("%02x", data[i]));
			
			// add line indicators
			if (b % 0x10 == 0xf) {
				sb.append("  # ").append(String.format("%08x", b - 0xf)).append('-')
				.append(String.format("%08x", b));
			}
			
			// add formatting
			if (i < data.length - 1) {
				if (b % 0x10 == 0xf) {
					sb.append('\n');
				} else if (b % 0x10 == 0x7) {
					sb.append("  ");
				} else {
					sb.append(' ');
				}
			}
		}
		return sb.toString();
	}
}
