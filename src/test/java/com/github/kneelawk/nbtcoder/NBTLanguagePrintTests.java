package com.github.kneelawk.nbtcoder;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.github.kneelawk.nbt.TagByte;

public class NBTLanguagePrintTests {
	@ParameterizedTest
	@CsvFileSource(resources = { "printByte.csv" })
	public void printByte(boolean prettyPrint, boolean printRootName, String name, byte value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(prettyPrint, printRootName).build();
		TagByte b = new TagByte(name, value);

		String out = printer.print(b);

		assertEquals(out, expected);
	}
}
