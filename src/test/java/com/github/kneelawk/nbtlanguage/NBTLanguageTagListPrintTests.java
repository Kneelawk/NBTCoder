package com.github.kneelawk.nbtlanguage;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.github.kneelawk.nbt.Tag;
import com.github.kneelawk.nbt.TagList;

public class NBTLanguageTagListPrintTests {
	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagListWithName.csv" })
	public void prettyPrintTagListWithName(String name, @ConvertWith(TagListArgumentConverter.class) List<Tag> data,
			String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, true, false).build();
		TagList<Tag> tag = new TagList<>(name, data);

		String out = printer.print(tag);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagListWithoutName.csv" })
	public void prettyPrintTagListWithoutName(String name, @ConvertWith(TagListArgumentConverter.class) List<Tag> data,
			String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, false, false).build();
		TagList<Tag> tag = new TagList<>(name, data);

		String out = printer.print(tag);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "simplePrintTagListWithName.csv" })
	public void simplePrintTagListWithName(String name, @ConvertWith(TagListArgumentConverter.class) List<Tag> data,
			String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, true, false).build();
		TagList<Tag> tag = new TagList<>(name, data);

		String out = printer.print(tag);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "simplePrintTagListWithoutName.csv" })
	public void simplePrintTagListWithoutName(String name, @ConvertWith(TagListArgumentConverter.class) List<Tag> data,
			String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, false, false).build();
		TagList<Tag> tag = new TagList<>(name, data);

		String out = printer.print(tag);

		assertEquals(expected, out);
	}
}
