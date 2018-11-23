package com.github.kneelawk.nbtlanguage;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.github.kneelawk.nbt.TagLong;

public class NBTLanguageTagLongPrintTests {
	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagLongWithName.csv" })
	public void prettyPrintTagLongWithName(String name, long value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, true).build();
		TagLong b = new TagLong(name, value);

		String out = printer.print(b);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagLongWithoutName.csv" })
	public void prettyPrintTagLongWithoutName(String name, long value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, false).build();
		TagLong b = new TagLong(name, value);

		String out = printer.print(b);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "simplePrintTagLongWithName.csv" })
	public void simplePrintTagLongWithName(String name, long value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, true).build();
		TagLong b = new TagLong(name, value);

		String out = printer.print(b);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "simplePrintTagLongWithoutName.csv" })
	public void simplePrintTagLongWithoutName(String name, long value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, false).build();
		TagLong b = new TagLong(name, value);

		String out = printer.print(b);

		assertEquals(expected, out);
	}
}
