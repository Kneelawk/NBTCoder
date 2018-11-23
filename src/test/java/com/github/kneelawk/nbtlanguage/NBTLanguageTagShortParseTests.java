package com.github.kneelawk.nbtlanguage;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.github.kneelawk.nbt.TagShort;

public class NBTLanguageTagShortParseTests {
	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagShortWithName.csv", "simplePrintTagShortWithName.csv" })
	public void parseTagShortWithName(String name, short value, String input) throws IOException {
		NBTLanguageParser parser = new NBTLanguageParser();
		TagShort b = (TagShort) parser.parse(input);

		assertEquals(name, b.getName());
		assertEquals(value, b.getValue());
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagShortWithoutName.csv", "simplePrintTagShortWithoutName.csv" })
	public void parseTagShortWithoutName(String name, short value, String input) throws IOException {
		NBTLanguageParser parser = new NBTLanguageParser();
		TagShort b = (TagShort) parser.parse(input);

		assertEquals(value, b.getValue());
	}
}
