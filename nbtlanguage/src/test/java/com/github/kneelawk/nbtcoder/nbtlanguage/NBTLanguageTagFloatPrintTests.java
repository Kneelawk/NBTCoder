package com.github.kneelawk.nbtcoder.nbtlanguage;

import com.github.kneelawk.nbtcoder.nbt.TagFloat;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static com.github.kneelawk.nbtcoder.test.utils.NewlineUtils.stripCarriageReturn;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NBTLanguageTagFloatPrintTests {
	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagFloatWithName.csv" })
	public void prettyPrintTagFloatWithName(String name, float value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, true, false).build();
		TagFloat b = new TagFloat(name, value);

		String out = printer.print(b);

		assertEquals(stripCarriageReturn(expected), stripCarriageReturn(out));
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagFloatWithoutName.csv" })
	public void prettyPrintTagFloatWithoutName(String name, float value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(true, false, false).build();
		TagFloat b = new TagFloat(name, value);

		String out = printer.print(b);

		assertEquals(stripCarriageReturn(expected), stripCarriageReturn(out));
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "simplePrintTagFloatWithName.csv" })
	public void simplePrintTagFloatWithName(String name, float value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, true, false).build();
		TagFloat b = new TagFloat(name, value);

		String out = printer.print(b);

		assertEquals(stripCarriageReturn(expected), stripCarriageReturn(out));
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "simplePrintTagFloatWithoutName.csv" })
	public void simplePrintTagFloatWithoutName(String name, float value, String expected) {
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder(false, false, false).build();
		TagFloat b = new TagFloat(name, value);

		String out = printer.print(b);

		assertEquals(stripCarriageReturn(expected), stripCarriageReturn(out));
	}
}
