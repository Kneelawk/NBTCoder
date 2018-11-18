package com.github.kneelawk.nbtcoder;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.github.kneelawk.nbt.TagByte;
import com.github.kneelawk.nbt.TagShort;

public class NBTLanguagePrintTests {
	@ParameterizedTest
	@CsvFileSource(resources = { "printByte.csv" })
	public void printByte(boolean prettyPrint, boolean printRootName, String name, byte value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(prettyPrint, printRootName).build();
		TagByte b = new TagByte(name, value);

		String out = printer.print(b);

		assertEquals(out, expected);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "printShort.csv" })
	public void printShort(boolean prettyPrint, boolean printRootName, String name, short value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(prettyPrint, printRootName).build();
		TagShort b = new TagShort(name, value);

		String out = printer.print(b);

		assertEquals(out, expected);
	}
}
