package com.github.kneelawk.nbtcoder;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.github.kneelawk.nbt.TagByte;

public class NBTLanguageTagByteParseTests {
	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagByteWithName.csv", "simplePrintTagByteWithName.csv" })
	public void parseTagByteWithName(String name, byte value, String input) throws IOException {
		NBTLanguageParser parser = new NBTLanguageParser();
		TagByte b = (TagByte) parser.parse(input);

		assertEquals(name, b.getName());
		assertEquals(value, b.getValue());
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagByteWithoutName.csv", "simplePrintTagByteWithoutName.csv" })
	public void parseTagByteWithoutName(String name, byte value, String input) throws IOException {
		NBTLanguageParser parser = new NBTLanguageParser();
		TagByte b = (TagByte) parser.parse(input);

		assertEquals(value, b.getValue());
	}
}
