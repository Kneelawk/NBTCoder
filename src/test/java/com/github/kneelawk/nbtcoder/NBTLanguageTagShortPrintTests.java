package com.github.kneelawk.nbtcoder;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.github.kneelawk.nbt.TagShort;
import com.github.kneelawk.nbtlanguage.NBTLanguagePrinter;

public class NBTLanguageTagShortPrintTests {
	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagShortWithName.csv" })
	public void prettyPrintTagShortWithName(String name, short value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, true).build();
		TagShort b = new TagShort(name, value);

		String out = printer.print(b);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagShortWithoutName.csv" })
	public void prettyPrintTagShortWithoutName(String name, short value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, false).build();
		TagShort b = new TagShort(name, value);

		String out = printer.print(b);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "simplePrintTagShortWithName.csv" })
	public void simplePrintTagShortWithName(String name, short value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, true).build();
		TagShort b = new TagShort(name, value);

		String out = printer.print(b);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "simplePrintTagShortWithoutName.csv" })
	public void simplePrintTagShortWithoutName(String name, short value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, false).build();
		TagShort b = new TagShort(name, value);

		String out = printer.print(b);

		assertEquals(expected, out);
	}
}
