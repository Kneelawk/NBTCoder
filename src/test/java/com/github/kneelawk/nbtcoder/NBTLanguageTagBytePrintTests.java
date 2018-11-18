package com.github.kneelawk.nbtcoder;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.github.kneelawk.nbt.TagByte;

public class NBTLanguageTagBytePrintTests {
	@Test
	public void prettyPrintByteWithName() {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, true).build();
		TagByte b = new TagByte("name", (byte) 24);

		String out = printer.print(b);

		assertEquals(out, "name: 24b");
	}

	@Test
	public void prettyPrintByteWithoutName() {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, false).build();
		TagByte b = new TagByte("name", (byte) 24);

		String out = printer.print(b);

		assertEquals(out, "24b");
	}

	@Test
	public void simplePrintByteWithName() {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, true).build();
		TagByte b = new TagByte("name", (byte) 24);

		String out = printer.print(b);

		assertEquals(out, "name:24b");
	}

	@Test
	public void simplePrintByteWithoutName() {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, false).build();
		TagByte b = new TagByte("name", (byte) 24);

		String out = printer.print(b);

		assertEquals(out, "24b");
	}
}
