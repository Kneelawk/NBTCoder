package com.github.kneelawk.nbtcoder.hexlanguage;

import static com.github.kneelawk.nbtcoder.utils.StringUtils.*;

public class HexLanguagePrinter {
	public static class Builder {
		private boolean printLineNumbers = true;
		private boolean applyOffset = true;

		public Builder() {
		}

		public Builder(boolean printLineNumbers, boolean applyOffset) {
			this.printLineNumbers = printLineNumbers;
			this.applyOffset = applyOffset;
		}

		public HexLanguagePrinter build() {
			return new HexLanguagePrinter(printLineNumbers, applyOffset);
		}

		public boolean isPrintLineNumbers() {
			return printLineNumbers;
		}

		public Builder setPrintLineNumbers(boolean printLineNumbers) {
			this.printLineNumbers = printLineNumbers;
			return this;
		}

		public boolean isApplyOffset() {
			return applyOffset;
		}

		public Builder setApplyOffset(boolean applyOffset) {
			this.applyOffset = applyOffset;
			return this;
		}
	}

	private boolean printLineNumbers;
	private boolean applyOffset;

	public HexLanguagePrinter(boolean printLineNumbers, boolean applyOffset) {
		this.printLineNumbers = printLineNumbers;
		this.applyOffset = applyOffset;
	}

	public String print(byte[] data, int offset) {
		StringBuilder sb = new StringBuilder();
		print(data, offset, sb);
		return sb.toString();
	}

	public void print(byte[] data, int offset, StringBuilder sb) {
		if (applyOffset) {
			int missing = offset % 0x10;
			spaces(missing * 3 + (missing <= 7 ? 0 : 1), sb);
		}

		for (int i = 0; i < data.length; i++) {
			sb.append(String.format("%02x", data[i]));

			int b = i;
			if (applyOffset) {
				b += offset;
			}

			// add line indicators
			if (printLineNumbers) {
				int base = applyOffset ? offset : 0;
				int start = ((b & 0xfffffff0) >= base ? b & 0xfffffff0 : base);
				if (b % 0x10 == 0xf) {
					sb.append(String.format("  # %08x-%08x", start, b));
				} else if (i == data.length - 1) {
					int remaining = 0xf - (b % 0x10);
					spaces(remaining * 3 + (remaining <= 7 ? 0 : 1) + 2, sb);
					sb.append(String.format("# %08x-%08x", start, b));
				}
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
	}
}
