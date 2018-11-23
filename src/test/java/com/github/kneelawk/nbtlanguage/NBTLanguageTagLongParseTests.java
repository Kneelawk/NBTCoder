package com.github.kneelawk.nbtlanguage;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.github.kneelawk.nbt.TagLong;

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
