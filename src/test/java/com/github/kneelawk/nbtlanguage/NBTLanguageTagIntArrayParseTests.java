package com.github.kneelawk.nbtlanguage;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.github.kneelawk.nbt.TagIntArray;

public class NBTLanguageTagIntArrayParseTests {
	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagIntArrayWithName.csv", "simplePrintTagIntArrayWithName.csv" })
	public void parseTagIntArrayWithName(String name, @ConvertWith(IntArrayArgumentConverter.class) int[] data,
			String input) throws IOException {
		NBTLanguageParser parser = new NBTLanguageParser();
		TagIntArray tag = (TagIntArray) parser.parse(input);

		assertEquals(name, tag.getName());
		assertArrayEquals(data, tag.getValue());
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagIntArrayWithoutName.csv", "simplePrintTagIntArrayWithoutName.csv" })
	public void parseTagIntArrayWithoutName(String name, @ConvertWith(IntArrayArgumentConverter.class) int[] data,
			String input) throws IOException {
		NBTLanguageParser parser = new NBTLanguageParser();
		TagIntArray tag = (TagIntArray) parser.parse(input);

		assertArrayEquals(data, tag.getValue());
	}
}
