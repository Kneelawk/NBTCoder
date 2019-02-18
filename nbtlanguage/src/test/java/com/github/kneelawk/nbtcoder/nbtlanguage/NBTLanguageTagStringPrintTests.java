package com.github.kneelawk.nbtcoder.nbtlanguage;

import com.github.kneelawk.nbtcoder.nbt.TagString;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NBTLanguageTagStringPrintTests {
	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagStringWithName.csv" })
	public void prettyPrintTagStringWithName(String name, String value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, true, false).build();
		TagString b = new TagString(name, value);

		String out = printer.print(b);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagStringWithoutName.csv" })
	public void prettyPrintTagStringWithoutName(String name, String value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, false, false).build();
		TagString b = new TagString(name, value);

		String out = printer.print(b);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "simplePrintTagStringWithName.csv" })
	public void simplePrintTagStringWithName(String name, String value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, true, false).build();
		TagString b = new TagString(name, value);

		String out = printer.print(b);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "simplePrintTagStringWithoutName.csv" })
	public void simplePrintTagStringWithoutName(String name, String value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, false, false).build();
		TagString b = new TagString(name, value);

		String out = printer.print(b);

		assertEquals(expected, out);
	}
}
