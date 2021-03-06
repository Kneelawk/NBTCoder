package com.github.kneelawk.nbtcoder.nbtlanguage;

import com.github.kneelawk.nbtcoder.nbt.Tag;
import com.github.kneelawk.nbtcoder.nbt.TagList;
import com.github.kneelawk.nbtcoder.test.nbtlanguage.TagListArgumentConverter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.List;

import static com.github.kneelawk.nbtcoder.test.utils.NewlineUtils.stripCarriageReturn;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NBTLanguageTagListPrintTests {
	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagListWithName.csv" })
	public void prettyPrintTagListWithName(String name, @ConvertWith(TagListArgumentConverter.class) List<Tag> data,
										   String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, true, false).build();
		TagList<Tag> tag = new TagList<>(name, data);

		String out = printer.print(tag);

		assertEquals(stripCarriageReturn(expected), stripCarriageReturn(out));
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagListWithoutName.csv" })
	public void prettyPrintTagListWithoutName(String name, @ConvertWith(TagListArgumentConverter.class) List<Tag> data,
											  String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, false, false).build();
		TagList<Tag> tag = new TagList<>(name, data);

		String out = printer.print(tag);

		assertEquals(stripCarriageReturn(expected), stripCarriageReturn(out));
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "simplePrintTagListWithName.csv" })
	public void simplePrintTagListWithName(String name, @ConvertWith(TagListArgumentConverter.class) List<Tag> data,
										   String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, true, false).build();
		TagList<Tag> tag = new TagList<>(name, data);

		String out = printer.print(tag);

		assertEquals(stripCarriageReturn(expected), stripCarriageReturn(out));
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "simplePrintTagListWithoutName.csv" })
	public void simplePrintTagListWithoutName(String name, @ConvertWith(TagListArgumentConverter.class) List<Tag> data,
											  String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, false, false).build();
		TagList<Tag> tag = new TagList<>(name, data);

		String out = printer.print(tag);

		assertEquals(stripCarriageReturn(expected), stripCarriageReturn(out));
	}
}
