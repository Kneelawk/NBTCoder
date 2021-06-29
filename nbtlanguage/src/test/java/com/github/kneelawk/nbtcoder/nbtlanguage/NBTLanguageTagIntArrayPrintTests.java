package com.github.kneelawk.nbtcoder.nbtlanguage;

import com.github.kneelawk.nbtcoder.nbt.TagIntArray;
import com.github.kneelawk.nbtcoder.test.utils.IntArrayArgumentConverter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvFileSource;

import static com.github.kneelawk.nbtcoder.test.utils.NewlineUtils.stripCarriageReturn;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NBTLanguageTagIntArrayPrintTests {
	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagIntArrayWithNameWithHexArrays.csv" })
	public void prettyPrintTagIntArrayWithNameWithHexArrays(String name,
															@ConvertWith(IntArrayArgumentConverter.class) int[] data, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, true, true).build();
		TagIntArray tag = new TagIntArray(name, data);

		String out = printer.print(tag);

		assertEquals(stripCarriageReturn(expected), stripCarriageReturn(out));
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagIntArrayWithoutNameWithHexArrays.csv" })
	public void prettyPrintTagIntArrayWithoutNameWithHexArrays(String name,
															   @ConvertWith(IntArrayArgumentConverter.class) int[] data, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, false, true).build();
		TagIntArray tag = new TagIntArray(name, data);

		String out = printer.print(tag);

		assertEquals(stripCarriageReturn(expected), stripCarriageReturn(out));
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "simplePrintTagIntArrayWithNameWithHexArrays.csv" })
	public void simplePrintTagIntArrayWithNameWithHexArrays(String name,
															@ConvertWith(IntArrayArgumentConverter.class) int[] data, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, true, true).build();
		TagIntArray tag = new TagIntArray(name, data);

		String out = printer.print(tag);

		assertEquals(stripCarriageReturn(expected), stripCarriageReturn(out));
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "simplePrintTagIntArrayWithoutNameWithHexArrays.csv" })
	public void simplePrintTagIntArrayWithoutNameWithHexArrays(String name,
															   @ConvertWith(IntArrayArgumentConverter.class) int[] data, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, false, true).build();
		TagIntArray tag = new TagIntArray(name, data);

		String out = printer.print(tag);

		assertEquals(stripCarriageReturn(expected), stripCarriageReturn(out));
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagIntArrayWithName.csv" })
	public void prettyPrintTagIntArrayWithName(String name, @ConvertWith(IntArrayArgumentConverter.class) int[] data,
											   String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, true, false).build();
		TagIntArray tag = new TagIntArray(name, data);

		String out = printer.print(tag);

		assertEquals(stripCarriageReturn(expected), stripCarriageReturn(out));
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagIntArrayWithoutName.csv" })
	public void prettyPrintTagIntArrayWithoutName(String name, @ConvertWith(IntArrayArgumentConverter.class) int[] data,
												  String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, false, false).build();
		TagIntArray tag = new TagIntArray(name, data);

		String out = printer.print(tag);

		assertEquals(stripCarriageReturn(expected), stripCarriageReturn(out));
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "simplePrintTagIntArrayWithName.csv" })
	public void simplePrintTagIntArrayWithName(String name, @ConvertWith(IntArrayArgumentConverter.class) int[] data,
											   String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, true, false).build();
		TagIntArray tag = new TagIntArray(name, data);

		String out = printer.print(tag);

		assertEquals(stripCarriageReturn(expected), stripCarriageReturn(out));
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "simplePrintTagIntArrayWithoutName.csv" })
	public void simplePrintTagIntArrayWithoutName(String name, @ConvertWith(IntArrayArgumentConverter.class) int[] data,
												  String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, false, false).build();
		TagIntArray tag = new TagIntArray(name, data);

		String out = printer.print(tag);

		assertEquals(stripCarriageReturn(expected), stripCarriageReturn(out));
	}
}
