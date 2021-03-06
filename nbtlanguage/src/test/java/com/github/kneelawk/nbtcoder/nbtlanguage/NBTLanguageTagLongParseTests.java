package com.github.kneelawk.nbtcoder.nbtlanguage;

import com.github.kneelawk.nbtcoder.nbt.TagLong;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NBTLanguageTagLongParseTests {
	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagLongWithName.csv", "simplePrintTagLongWithName.csv" })
	public void parseTagLongWithName(String name, long value, String input) throws IOException {
		NBTLanguageParser parser = new NBTLanguageParser();
		TagLong b = (TagLong) parser.parse(input);

		assertEquals(name, b.getName());
		assertEquals(value, b.getValue());
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagLongWithoutName.csv", "simplePrintTagLongWithoutName.csv" })
	public void parseTagLongWithoutName(String name, long value, String input) throws IOException {
		NBTLanguageParser parser = new NBTLanguageParser();
		TagLong b = (TagLong) parser.parse(input);

		assertEquals(value, b.getValue());
	}
}
