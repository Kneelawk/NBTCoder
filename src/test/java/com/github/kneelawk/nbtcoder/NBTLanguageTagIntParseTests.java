package com.github.kneelawk.nbtcoder;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.github.kneelawk.nbt.TagInt;
import com.github.kneelawk.nbtlanguage.NBTLanguageParser;

public class NBTLanguageTagIntParseTests {
	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagIntWithName.csv", "simplePrintTagIntWithName.csv" })
	public void parseTagIntWithName(String name, int value, String input) throws IOException {
		NBTLanguageParser parser = new NBTLanguageParser();
		TagInt b = (TagInt) parser.parse(input);

		assertEquals(name, b.getName());
		assertEquals(value, b.getValue());
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagIntWithoutName.csv", "simplePrintTagIntWithoutName.csv" })
	public void parseTagIntWithoutName(String name, int value, String input) throws IOException {
		NBTLanguageParser parser = new NBTLanguageParser();
		TagInt b = (TagInt) parser.parse(input);

		assertEquals(value, b.getValue());
	}
}
