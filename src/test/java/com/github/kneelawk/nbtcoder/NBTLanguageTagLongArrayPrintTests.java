package com.github.kneelawk.nbtcoder;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.github.kneelawk.nbt.TagLongArray;

public class NBTLanguageTagLongArrayPrintTests {
	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagLongArrayWithName.csv" })
	public void prettyPrintTagLongArrayWithName(String name, @ConvertWith(LongArrayArgumentConverter.class) long[] data,
			String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, true).build();
		TagLongArray tag = new TagLongArray(name, data);

		String out = printer.print(tag);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagLongArrayWithoutName.csv" })
	public void prettyPrintTagLongArrayWithoutName(String name,
			@ConvertWith(LongArrayArgumentConverter.class) long[] data, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, false).build();
		TagLongArray tag = new TagLongArray(name, data);

		String out = printer.print(tag);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "simplePrintTagLongArrayWithName.csv" })
	public void simplePrintTagLongArrayWithName(String name, @ConvertWith(LongArrayArgumentConverter.class) long[] data,
			String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, true).build();
		TagLongArray tag = new TagLongArray(name, data);

		String out = printer.print(tag);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "simplePrintTagLongArrayWithoutName.csv" })
	public void simplePrintTagLongArrayWithoutName(String name,
			@ConvertWith(LongArrayArgumentConverter.class) long[] data, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, false).build();
		TagLongArray tag = new TagLongArray(name, data);

		String out = printer.print(tag);

		assertEquals(expected, out);
	}
}
