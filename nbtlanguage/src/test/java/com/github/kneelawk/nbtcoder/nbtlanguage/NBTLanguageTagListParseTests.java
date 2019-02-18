package com.github.kneelawk.nbtcoder.nbtlanguage;

import com.github.kneelawk.nbtcoder.nbt.Tag;
import com.github.kneelawk.nbtcoder.nbt.TagList;
import com.github.kneelawk.nbtcoder.test.nbtlanguage.TagListArgumentConverter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class NBTLanguageTagListParseTests {
	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagListWithName.csv", "simplePrintTagListWithName.csv" })
	public void parseTagListWithName(String name, @ConvertWith(TagListArgumentConverter.class) List<Tag> data,
			String input) throws IOException {
		NBTLanguageParser parser = new NBTLanguageParser();
		@SuppressWarnings("unchecked")
		TagList<Tag> tag = (TagList<Tag>) parser.parse(input);

		assertEquals(name, tag.getName());
		assertIterableEquals(data, tag.getElements());
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagListWithoutName.csv", "simplePrintTagListWithoutName.csv" })
	public void parseTagListWithoutName(String name, @ConvertWith(TagListArgumentConverter.class) List<Tag> data,
			String input) throws IOException {
		NBTLanguageParser parser = new NBTLanguageParser();
		@SuppressWarnings("unchecked")
		TagList<Tag> tag = (TagList<Tag>) parser.parse(input);

		assertIterableEquals(data, tag.getElements());
	}
}
