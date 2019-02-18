package com.github.kneelawk.nbtcoder.nbtlanguage;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.github.kneelawk.nbtcoder.nbt.TagByteArray;
import com.github.kneelawk.nbtcoder.test.utils.ByteArrayArgumentConverter;

public class NBTLanguageTagByteArrayParseTests {
	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagByteArrayWithName.csv", "simplePrintTagByteArrayWithName.csv",
			"prettyPrintTagByteArrayWithNameWithHexArrays.csv", "simplePrintTagByteArrayWithNameWithHexArrays.csv" })
	public void parseTagByteArrayWithName(String name, @ConvertWith(ByteArrayArgumentConverter.class) byte[] data,
			String input) throws IOException {
		NBTLanguageParser parser = new NBTLanguageParser();
		TagByteArray tag = (TagByteArray) parser.parse(input);

		assertEquals(name, tag.getName());
		assertArrayEquals(data, tag.getValue());
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagByteArrayWithoutName.csv", "simplePrintTagByteArrayWithoutName.csv",
			"prettyPrintTagByteArrayWithoutNameWithHexArrays.csv",
			"simplePrintTagByteArrayWithoutNameWithHexArrays.csv" })
	public void parseTagByteArrayWithoutName(String name, @ConvertWith(ByteArrayArgumentConverter.class) byte[] data,
			String input) throws IOException {
		NBTLanguageParser parser = new NBTLanguageParser();
		TagByteArray tag = (TagByteArray) parser.parse(input);

		assertArrayEquals(data, tag.getValue());
	}
}
