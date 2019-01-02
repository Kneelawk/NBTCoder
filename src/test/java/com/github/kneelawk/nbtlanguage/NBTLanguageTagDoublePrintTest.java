package com.github.kneelawk.nbtlanguage;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.github.kneelawk.nbt.TagDouble;

public class NBTLanguageTagDoublePrintTest {
	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagDoubleWithName.csv" })
	public void prettyPrintTagDoubleWithName(String name, double value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, true, false).build();
		TagDouble b = new TagDouble(name, value);

		String out = printer.print(b);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagDoubleWithoutName.csv" })
	public void prettyPrintTagDoubleWithoutName(String name, double value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, false, false).build();
		TagDouble b = new TagDouble(name, value);

		String out = printer.print(b);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "simplePrintTagDoubleWithName.csv" })
	public void simplePrintTagDoubleWithName(String name, double value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, true, false).build();
		TagDouble b = new TagDouble(name, value);

		String out = printer.print(b);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "simplePrintTagDoubleWithoutName.csv" })
	public void simplePrintTagDoubleWithoutName(String name, double value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, false, false).build();
		TagDouble b = new TagDouble(name, value);

		String out = printer.print(b);

		assertEquals(expected, out);
	}
}
