package com.github.kneelawk.nbtcoder.nbtlanguage;

import com.github.kneelawk.nbtcoder.nbt.TagString;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NBTLanguageTagStringParseTest {
	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagStringWithName.csv", "simplePrintTagStringWithName.csv" })
	public void parseTagStringWithName(String name, String value, String input) throws IOException {
		NBTLanguageParser parser = new NBTLanguageParser();
		TagString b = (TagString) parser.parse(input);

		assertEquals(name, b.getName());
		assertEquals(value, b.getValue());
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagStringWithoutName.csv", "simplePrintTagStringWithoutName.csv" })
	public void parseTagStringWithoutName(String name, String value, String input) throws IOException {
		NBTLanguageParser parser = new NBTLanguageParser();
		TagString b = (TagString) parser.parse(input);

		assertEquals(value, b.getValue());
	}
}
