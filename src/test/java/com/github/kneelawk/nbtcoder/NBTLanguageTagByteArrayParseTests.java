package com.github.kneelawk.nbtcoder;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.github.kneelawk.nbt.TagByteArray;

public class NBTLanguageTagByteArrayParseTests {
	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagByteArrayWithName.csv", "simplePrintTagByteArrayWithName.csv" })
	public void parseTagByteArrayWithName(String name, @ConvertWith(ByteArrayArgumentConverter.class) byte[] data,
			String input) throws IOException {
		NBTLanguageParser parser = new NBTLanguageParser();
		TagByteArray tag = (TagByteArray) parser.parse(input);

		assertEquals(name, tag.getName());
		assertArrayEquals(data, tag.getValue());
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagByteArrayWithoutName.csv", "simplePrintTagByteArrayWithoutName.csv" })
	public void parseTagByteArrayWithoutName(String name, @ConvertWith(ByteArrayArgumentConverter.class) byte[] data,
			String input) throws IOException {
		NBTLanguageParser parser = new NBTLanguageParser();
		TagByteArray tag = (TagByteArray) parser.parse(input);

		assertArrayEquals(data, tag.getValue());
	}
}
