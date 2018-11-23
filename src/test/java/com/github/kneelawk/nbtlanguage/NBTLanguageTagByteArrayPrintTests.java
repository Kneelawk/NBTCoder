package com.github.kneelawk.nbtlanguage;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.github.kneelawk.nbt.TagByteArray;

public class NBTLanguageTagByteArrayPrintTests {
	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagByteArrayWithName.csv" })
	public void prettyPrintTagByteArrayWithName(String name, @ConvertWith(ByteArrayArgumentConverter.class) byte[] data,
			String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, true).build();
		TagByteArray tag = new TagByteArray(name, data);

		String out = printer.print(tag);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagByteArrayWithoutName.csv" })
	public void prettyPrintTagByteArrayWithoutName(String name,
			@ConvertWith(ByteArrayArgumentConverter.class) byte[] data, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, false).build();
		TagByteArray tag = new TagByteArray(name, data);

		String out = printer.print(tag);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "simplePrintTagByteArrayWithName.csv" })
	public void simplePrintTagByteArrayWithName(String name, @ConvertWith(ByteArrayArgumentConverter.class) byte[] data,
			String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, true).build();
		TagByteArray tag = new TagByteArray(name, data);

		String out = printer.print(tag);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "simplePrintTagByteArrayWithoutName.csv" })
	public void simplePrintTagByteArrayWithoutName(String name,
			@ConvertWith(ByteArrayArgumentConverter.class) byte[] data, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, false).build();
		TagByteArray tag = new TagByteArray(name, data);

		String out = printer.print(tag);

		assertEquals(expected, out);
	}
}
