package com.github.kneelawk.nbtcoder;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.github.kneelawk.nbt.TagFloat;
import com.github.kneelawk.nbtlanguage.NBTLanguagePrinter;

public class NBTLanguageTagFloatPrintTests {
	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagFloatWithName.csv" })
	public void prettyPrintTagFloatWithName(String name, float value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, true).build();
		TagFloat b = new TagFloat(name, value);

		String out = printer.print(b);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagFloatWithoutName.csv" })
	public void prettyPrintTagFloatWithoutName(String name, float value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, false).build();
		TagFloat b = new TagFloat(name, value);

		String out = printer.print(b);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "simplePrintTagFloatWithName.csv" })
	public void simplePrintTagFloatWithName(String name, float value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, true).build();
		TagFloat b = new TagFloat(name, value);

		String out = printer.print(b);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "simplePrintTagFloatWithoutName.csv" })
	public void simplePrintTagFloatWithoutName(String name, float value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, false).build();
		TagFloat b = new TagFloat(name, value);

		String out = printer.print(b);

		assertEquals(expected, out);
	}
}
