package com.github.kneelawk.nbtcoder;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.github.kneelawk.nbt.TagString;
import com.github.kneelawk.nbtlanguage.NBTLanguagePrinter;

public class NBTLanguageTagStringPrintTests {
	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagStringWithName.csv" })
	public void prettyPrintTagStringWithName(String name, String value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, true).build();
		TagString b = new TagString(name, value);

		String out = printer.print(b);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagStringWithoutName.csv" })
	public void prettyPrintTagStringWithoutName(String name, String value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, false).build();
		TagString b = new TagString(name, value);

		String out = printer.print(b);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "simplePrintTagStringWithName.csv" })
	public void simplePrintTagStringWithName(String name, String value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, true).build();
		TagString b = new TagString(name, value);

		String out = printer.print(b);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "simplePrintTagStringWithoutName.csv" })
	public void simplePrintTagStringWithoutName(String name, String value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, false).build();
		TagString b = new TagString(name, value);

		String out = printer.print(b);

		assertEquals(expected, out);
	}
}
