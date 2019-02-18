package com.github.kneelawk.nbtcoder.hexlanguage;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.github.kneelawk.nbtcoder.test.utils.ByteArrayArgumentConverter;

public class HexLanguagePrintTests {
	@ParameterizedTest
	@CsvFileSource(resources = {"printHexWithLineNumbersWithOffset.csv"})
	public void printHexWithLineNumbersWithOffset(@ConvertWith(ByteArrayArgumentConverter.class) byte[] data,
			int offset, String expected) {
		HexLanguagePrinter printer = new HexLanguagePrinter.Builder(true, true).build();

		String out = printer.print(data, offset);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = {"printHexWithoutLineNumbersWithOffset.csv"})
	public void printHexWithoutLineNumbersWithOffset(@ConvertWith(ByteArrayArgumentConverter.class) byte[] data,
			int offset, String expected) {
		HexLanguagePrinter printer = new HexLanguagePrinter.Builder(false, true).build();

		String out = printer.print(data, offset);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = {"printHexWithLineNumbersWithoutOffset.csv"})
	public void printHexWithLineNumbersWithoutOffset(@ConvertWith(ByteArrayArgumentConverter.class) byte[] data,
			int offset, String expected) {
		HexLanguagePrinter printer = new HexLanguagePrinter.Builder(true, false).build();

		String out = printer.print(data, offset);

		assertEquals(expected, out);
	}

	@ParameterizedTest
	@CsvFileSource(resources = {"printHexWithoutLineNumbersWithoutOffset.csv"})
	public void printHexWithoutLineNumbersWithoutOffset(@ConvertWith(ByteArrayArgumentConverter.class) byte[] data,
			int offset, String expected) {
		HexLanguagePrinter printer = new HexLanguagePrinter.Builder(false, false).build();

		String out = printer.print(data, offset);

		assertEquals(expected, out);
	}
}
