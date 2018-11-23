package com.github.kneelawk.nbtcoder;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.github.kneelawk.nbt.TagInt;
import com.github.kneelawk.nbtlanguage.NBTLanguagePrinter;

public class NBTLanguageTagIntPrintTests {
	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagIntWithName.csv" })
	public void prettyPrintTagIntWithName(String name, int value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, true).build();
		TagInt b = new TagInt(name, value);

		String out = printer.print(b);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagIntWithoutName.csv" })
	public void prettyPrintTagIntWithoutName(String name, int value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, false).build();
		TagInt b = new TagInt(name, value);

		String out = printer.print(b);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "simplePrintTagIntWithName.csv" })
	public void simplePrintTagIntWithName(String name, int value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, true).build();
		TagInt b = new TagInt(name, value);

		String out = printer.print(b);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "simplePrintTagIntWithoutName.csv" })
	public void simplePrintTagIntWithoutName(String name, int value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, false).build();
		TagInt b = new TagInt(name, value);

		String out = printer.print(b);

		assertEquals(expected, out);
	}
}
