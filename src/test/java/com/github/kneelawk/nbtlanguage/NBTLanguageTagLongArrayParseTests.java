package com.github.kneelawk.nbtlanguage;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.github.kneelawk.nbt.TagLongArray;

public class NBTLanguageTagLongArrayParseTests {
	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagLongArrayWithName.csv", "simplePrintTagLongArrayWithName.csv" })
	public void parseTagLongArrayWithName(String name, @ConvertWith(LongArrayArgumentConverter.class) long[] data,
			String input) throws IOException {
		NBTLanguageParser parser = new NBTLanguageParser();
		TagLongArray tag = (TagLongArray) parser.parse(input);

		assertEquals(name, tag.getName());
		assertArrayEquals(data, tag.getValue());
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagLongArrayWithoutName.csv", "simplePrintTagLongArrayWithoutName.csv" })
	public void parseTagLongArrayWithoutName(String name, @ConvertWith(LongArrayArgumentConverter.class) long[] data,
			String input) throws IOException {
		NBTLanguageParser parser = new NBTLanguageParser();
		TagLongArray tag = (TagLongArray) parser.parse(input);

		assertArrayEquals(data, tag.getValue());
	}
}
