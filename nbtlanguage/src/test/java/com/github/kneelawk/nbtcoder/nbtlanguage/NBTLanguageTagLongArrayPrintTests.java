package com.github.kneelawk.nbtcoder.nbtlanguage;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.github.kneelawk.nbtcoder.nbt.TagLongArray;
import com.github.kneelawk.nbtcoder.test.utils.LongArrayArgumentConverter;

public class NBTLanguageTagLongArrayPrintTests {
	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagLongArrayWithNameWithHexArrays.csv" })
	public void prettyPrintTagLongArrayWithNameWithHexArrays(String name,
			@ConvertWith(LongArrayArgumentConverter.class) long[] data, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, true, true).build();
		TagLongArray tag = new TagLongArray(name, data);

		String out = printer.print(tag);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagLongArrayWithoutNameWithHexArrays.csv" })
	public void prettyPrintTagLongArrayWithoutNameWithHexArrays(String name,
			@ConvertWith(LongArrayArgumentConverter.class) long[] data, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, false, true).build();
		TagLongArray tag = new TagLongArray(name, data);

		String out = printer.print(tag);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "simplePrintTagLongArrayWithNameWithHexArrays.csv" })
	public void simplePrintTagLongArrayWithNameWithHexArrays(String name,
			@ConvertWith(LongArrayArgumentConverter.class) long[] data, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, true, true).build();
		TagLongArray tag = new TagLongArray(name, data);

		String out = printer.print(tag);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "simplePrintTagLongArrayWithoutNameWithHexArrays.csv" })
	public void simplePrintTagLongArrayWithoutNameWithHexArrays(String name,
			@ConvertWith(LongArrayArgumentConverter.class) long[] data, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, false, true).build();
		TagLongArray tag = new TagLongArray(name, data);

		String out = printer.print(tag);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagLongArrayWithName.csv" })
	public void prettyPrintTagLongArrayWithName(String name, @ConvertWith(LongArrayArgumentConverter.class) long[] data,
			String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, true, false).build();
		TagLongArray tag = new TagLongArray(name, data);

		String out = printer.print(tag);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagLongArrayWithoutName.csv" })
	public void prettyPrintTagLongArrayWithoutName(String name,
			@ConvertWith(LongArrayArgumentConverter.class) long[] data, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, false, false).build();
		TagLongArray tag = new TagLongArray(name, data);

		String out = printer.print(tag);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "simplePrintTagLongArrayWithName.csv" })
	public void simplePrintTagLongArrayWithName(String name, @ConvertWith(LongArrayArgumentConverter.class) long[] data,
			String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, true, false).build();
		TagLongArray tag = new TagLongArray(name, data);

		String out = printer.print(tag);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "simplePrintTagLongArrayWithoutName.csv" })
	public void simplePrintTagLongArrayWithoutName(String name,
			@ConvertWith(LongArrayArgumentConverter.class) long[] data, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, false, false).build();
		TagLongArray tag = new TagLongArray(name, data);

		String out = printer.print(tag);

		assertEquals(expected, out);
	}
}
