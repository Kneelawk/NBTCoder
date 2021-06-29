package com.github.kneelawk.nbtcoder.nbtlanguage;

import com.github.kneelawk.nbtcoder.nbt.TagLong;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static com.github.kneelawk.nbtcoder.test.utils.NewlineUtils.stripCarriageReturn;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NBTLanguageTagLongPrintTests {
	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagLongWithName.csv" })
	public void prettyPrintTagLongWithName(String name, long value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, true, false).build();
		TagLong b = new TagLong(name, value);

		String out = printer.print(b);

		assertEquals(stripCarriageReturn(expected), stripCarriageReturn(out));
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagLongWithoutName.csv" })
	public void prettyPrintTagLongWithoutName(String name, long value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, false, false).build();
		TagLong b = new TagLong(name, value);

		String out = printer.print(b);

		assertEquals(stripCarriageReturn(expected), stripCarriageReturn(out));
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "simplePrintTagLongWithName.csv" })
	public void simplePrintTagLongWithName(String name, long value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, true, false).build();
		TagLong b = new TagLong(name, value);

		String out = printer.print(b);

		assertEquals(stripCarriageReturn(expected), stripCarriageReturn(out));
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "simplePrintTagLongWithoutName.csv" })
	public void simplePrintTagLongWithoutName(String name, long value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, false, false).build();
		TagLong b = new TagLong(name, value);

		String out = printer.print(b);

		assertEquals(stripCarriageReturn(expected), stripCarriageReturn(out));
	}
}
