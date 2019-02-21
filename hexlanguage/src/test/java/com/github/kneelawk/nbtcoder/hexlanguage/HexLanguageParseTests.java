package com.github.kneelawk.nbtcoder.hexlanguage;

import com.github.kneelawk.nbtcoder.test.utils.ByteArrayArgumentConverter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class HexLanguageParseTests {
	@ParameterizedTest
	@CsvFileSource(resources = { "printHexWithLineNumbersWithOffset.csv", "printHexWithoutLineNumbersWithOffset.csv",
			"printHexWithLineNumbersWithoutOffset.csv", "printHexWithoutLineNumbersWithoutOffset.csv" })
	public void parseHex(@ConvertWith(ByteArrayArgumentConverter.class) byte[] expected, int offset, String input)
			throws IOException {
		HexLanguageParser parser = new HexLanguageParser();

		byte[] data = parser.parse(input);

		assertArrayEquals(expected, data);
	}
}
