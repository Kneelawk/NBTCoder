package com.github.kneelawk.nbtlanguage;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.github.kneelawk.nbt.Tag;
import com.github.kneelawk.nbt.TagCompound;

public class NBTLanguageTagCompoundPrintTests {
	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagCompoundWithName.csv" })
	public void prettyPrintTagCompoundWithName(String name, @ConvertWith(TagListArgumentConverter.class) List<Tag> data,
			String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, true, false).build();
		TagCompound tag = new TagCompound(name, data);

		String out = printer.print(tag);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagCompoundWithoutName.csv" })
	public void prettyPrintTagCompoundWithoutName(String name,
			@ConvertWith(TagListArgumentConverter.class) List<Tag> data, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, false, false).build();
		TagCompound tag = new TagCompound(name, data);

		String out = printer.print(tag);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "simplePrintTagCompoundWithName.csv" })
	public void simplePrintTagCompoundWithName(String name, @ConvertWith(TagListArgumentConverter.class) List<Tag> data,
			String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, true, false).build();
		TagCompound tag = new TagCompound(name, data);

		String out = printer.print(tag);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "simplePrintTagCompoundWithoutName.csv" })
	public void simplePrintTagCompoundWithoutName(String name,
			@ConvertWith(TagListArgumentConverter.class) List<Tag> data, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, false, false).build();
		TagCompound tag = new TagCompound(name, data);

		String out = printer.print(tag);

		assertEquals(expected, out);
	}
}
