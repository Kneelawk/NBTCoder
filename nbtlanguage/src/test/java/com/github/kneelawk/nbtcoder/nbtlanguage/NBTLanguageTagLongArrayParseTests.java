package com.github.kneelawk.nbtcoder.nbtlanguage;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.github.kneelawk.nbtcoder.nbt.TagLongArray;
import com.github.kneelawk.nbtcoder.test.utils.LongArrayArgumentConverter;

public class NBTLanguageTagLongArrayParseTests {
	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagLongArrayWithName.csv", "simplePrintTagLongArrayWithName.csv",
			"prettyPrintTagLongArrayWithNameWithHexArrays.csv", "simplePrintTagLongArrayWithNameWithHexArrays.csv" })
	public void parseTagLongArrayWithName(String name, @ConvertWith(LongArrayArgumentConverter.class) long[] data,
			String input) throws IOException {
		NBTLanguageParser parser = new NBTLanguageParser();
		TagLongArray tag = (TagLongArray) parser.parse(input);

		assertEquals(name, tag.getName());
		assertArrayEquals(data, tag.getValue());
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagLongArrayWithoutName.csv", "simplePrintTagLongArrayWithoutName.csv",
			"prettyPrintTagLongArrayWithoutNameWithHexArrays.csv",
			"simplePrintTagLongArrayWithoutNameWithHexArrays.csv" })
	public void parseTagLongArrayWithoutName(String name, @ConvertWith(LongArrayArgumentConverter.class) long[] data,
			String input) throws IOException {
		NBTLanguageParser parser = new NBTLanguageParser();
		TagLongArray tag = (TagLongArray) parser.parse(input);

		assertArrayEquals(data, tag.getValue());
	}
}
