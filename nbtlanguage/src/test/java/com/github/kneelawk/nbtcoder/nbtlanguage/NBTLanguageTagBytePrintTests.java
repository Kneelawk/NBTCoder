package com.github.kneelawk.nbtcoder.nbtlanguage;

import com.github.kneelawk.nbtcoder.nbt.TagByte;
import com.github.kneelawk.nbtcoder.test.utils.NewlineUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static com.github.kneelawk.nbtcoder.test.utils.NewlineUtils.stripCarriageReturn;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NBTLanguageTagBytePrintTests {
	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagByteWithName.csv" })
	public void prettyPrintTagByteWithName(String name, byte value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, true, false).build();
		TagByte b = new TagByte(name, value);

		String out = printer.print(b);

		assertEquals(stripCarriageReturn(expected), stripCarriageReturn(out));
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagByteWithoutName.csv" })
	public void prettyPrintTagByteWithoutName(String name, byte value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, false, false).build();
		TagByte b = new TagByte(name, value);

		String out = printer.print(b);

		assertEquals(stripCarriageReturn(expected), stripCarriageReturn(out));
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "simplePrintTagByteWithName.csv" })
	public void simplePrintTagByteWithName(String name, byte value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, true, false).build();
		TagByte b = new TagByte(name, value);

		String out = printer.print(b);

		assertEquals(stripCarriageReturn(expected), stripCarriageReturn(out));
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "simplePrintTagByteWithoutName.csv" })
	public void simplePrintTagByteWithoutName(String name, byte value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, false, false).build();
		TagByte b = new TagByte(name, value);

		String out = printer.print(b);

		assertEquals(stripCarriageReturn(expected), stripCarriageReturn(out));
	}
}
