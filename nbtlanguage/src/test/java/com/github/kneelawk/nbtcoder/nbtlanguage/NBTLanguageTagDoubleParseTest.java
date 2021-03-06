package com.github.kneelawk.nbtcoder.nbtlanguage;

import com.github.kneelawk.nbtcoder.nbt.TagDouble;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NBTLanguageTagDoubleParseTest {
	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagDoubleWithName.csv", "simplePrintTagDoubleWithName.csv" })
	public void parseTagDoubleWithName(String name, double value, String input) throws IOException {
		NBTLanguageParser parser = new NBTLanguageParser();
		TagDouble b = (TagDouble) parser.parse(input);

		assertEquals(name, b.getName());
		assertEquals(value, b.getValue());
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagDoubleWithoutName.csv", "simplePrintTagDoubleWithoutName.csv" })
	public void parseTagDoubleWithoutName(String name, double value, String input) throws IOException {
		NBTLanguageParser parser = new NBTLanguageParser();
		TagDouble b = (TagDouble) parser.parse(input);

		assertEquals(value, b.getValue());
	}
}
