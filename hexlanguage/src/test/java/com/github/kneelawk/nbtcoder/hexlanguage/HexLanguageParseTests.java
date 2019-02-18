package com.github.kneelawk.nbtcoder.hexlanguage;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.common.io.ByteStreams;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.github.kneelawk.nbtcoder.test.utils.ByteArrayArgumentConverter;

public class HexLanguageParseTests {
	@ParameterizedTest
	@CsvFileSource(resources = {"printHexWithLineNumbersWithOffset.csv", "printHexWithoutLineNumbersWithOffset.csv",
			"printHexWithLineNumbersWithoutOffset.csv", "printHexWithoutLineNumbersWithoutOffset.csv"})
	public void parseHex(@ConvertWith(ByteArrayArgumentConverter.class) byte[] expected, int offset, String input)
			throws IOException {
		HexLanguageParser parser = new HexLanguageParser();

		byte[] data = parser.parse(input);

		assertArrayEquals(expected, data);
	}
}
