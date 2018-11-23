package com.github.kneelawk.nbtlanguage;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.github.kneelawk.nbt.TagIntArray;

public class NBTLanguageTagIntArrayPrintTests {
	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagIntArrayWithName.csv" })
	public void prettyPrintTagIntArrayWithName(String name, @ConvertWith(IntArrayArgumentConverter.class) int[] data,
			String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, true).build();
		TagIntArray tag = new TagIntArray(name, data);

		String out = printer.print(tag);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagIntArrayWithoutName.csv" })
	public void prettyPrintTagIntArrayWithoutName(String name, @ConvertWith(IntArrayArgumentConverter.class) int[] data,
			String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, false).build();
		TagIntArray tag = new TagIntArray(name, data);

		String out = printer.print(tag);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "simplePrintTagIntArrayWithName.csv" })
	public void simplePrintTagIntArrayWithName(String name, @ConvertWith(IntArrayArgumentConverter.class) int[] data,
			String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, true).build();
		TagIntArray tag = new TagIntArray(name, data);

		String out = printer.print(tag);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "simplePrintTagIntArrayWithoutName.csv" })
	public void simplePrintTagIntArrayWithoutName(String name, @ConvertWith(IntArrayArgumentConverter.class) int[] data,
			String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, false).build();
		TagIntArray tag = new TagIntArray(name, data);

		String out = printer.print(tag);

		assertEquals(expected, out);
	}
}
