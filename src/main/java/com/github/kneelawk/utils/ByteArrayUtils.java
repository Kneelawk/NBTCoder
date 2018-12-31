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

	public static int lastNonZeroByte(byte[] data) {
		for (int i = data.length - 1; i >= 0; i--) {
			if (data[i] != 0) {
				return i;
			}
		}
		return -1;
	}

	public static String toHex(byte[] data, int offset) {
		StringBuilder sb = new StringBuilder();
		int missing = offset % 0x10;
		spaces(missing * 3 + (missing <= 7 ? 0 : 1), sb);
		for (int i = 0; i < data.length; i++) {
			int b = i + offset;
			sb.append(String.format("%02x", data[i]));

			// add line indicators
			int start = ((b & 0xfffffff0) > offset ? b & 0xfffffff0 : offset);
			if (b % 0x10 == 0xf) {
				sb.append(String.format("  # %08x-%08x", start, b));
			} else if (i == data.length - 1) {
				int remaining = 0xf - (b % 0x10);
				spaces(remaining * 3 + (remaining <= 7 ? 0 : 1) + 2, sb);
				sb.append(String.format("# %08x-%08x", start, b));
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
