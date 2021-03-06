package com.github.kneelawk.nbtcoder.nbtlanguage;

import com.github.kneelawk.nbtcoder.nbt.TagFloat;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NBTLanguageTagFloatParseTest {
	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagFloatWithName.csv", "simplePrintTagFloatWithName.csv" })
	public void parseTagFloatWithName(String name, float value, String input) throws IOException {
		NBTLanguageParser parser = new NBTLanguageParser();
		TagFloat b = (TagFloat) parser.parse(input);

		assertEquals(name, b.getName());
		assertEquals(value, b.getValue());
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagFloatWithoutName.csv", "simplePrintTagFloatWithoutName.csv" })
	public void parseTagFloatWithoutName(String name, float value, String input) throws IOException {
		NBTLanguageParser parser = new NBTLanguageParser();
		TagFloat b = (TagFloat) parser.parse(input);

		assertEquals(value, b.getValue());
	}
}
