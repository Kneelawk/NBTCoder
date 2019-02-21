package com.github.kneelawk.nbtcoder.nbtlanguage;

import com.github.kneelawk.nbtcoder.nbt.TagIntArray;
import com.github.kneelawk.nbtcoder.test.utils.IntArrayArgumentConverter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NBTLanguageTagIntArrayParseTests {
	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagIntArrayWithName.csv", "simplePrintTagIntArrayWithName.csv",
			"prettyPrintTagIntArrayWithNameWithHexArrays.csv", "simplePrintTagIntArrayWithNameWithHexArrays.csv" })
	public void parseTagIntArrayWithName(String name, @ConvertWith(IntArrayArgumentConverter.class) int[] data,
										 String input) throws IOException {
		NBTLanguageParser parser = new NBTLanguageParser();
		TagIntArray tag = (TagIntArray) parser.parse(input);

		assertEquals(name, tag.getName());
		assertArrayEquals(data, tag.getValue());
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagIntArrayWithoutName.csv", "simplePrintTagIntArrayWithoutName.csv",
			"prettyPrintTagIntArrayWithoutNameWithHexArrays.csv",
			"simplePrintTagIntArrayWithoutNameWithHexArrays.csv" })
	public void parseTagIntArrayWithoutName(String name, @ConvertWith(IntArrayArgumentConverter.class) int[] data,
											String input) throws IOException {
		NBTLanguageParser parser = new NBTLanguageParser();
		TagIntArray tag = (TagIntArray) parser.parse(input);

		assertArrayEquals(data, tag.getValue());
	}
}
