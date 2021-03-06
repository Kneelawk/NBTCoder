package com.github.kneelawk.nbtcoder.nbtlanguage;

import com.github.kneelawk.nbtcoder.nbt.Tag;
import com.github.kneelawk.nbtcoder.nbt.TagCompound;
import com.github.kneelawk.nbtcoder.test.nbtlanguage.TagListArgumentConverter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class NBTLanguageTagCompoundParseTests {
	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagCompoundWithName.csv", "simplePrintTagCompoundWithName.csv" })
	public void parseTagCompoundWithName(String name, @ConvertWith(TagListArgumentConverter.class) List<Tag> data,
										 String input) throws IOException {
		NBTLanguageParser parser = new NBTLanguageParser();
		TagCompound tag = (TagCompound) parser.parse(input);

		assertEquals(name, tag.getName());
		assertIterableEquals(data, tag.tags());
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "prettyPrintTagCompoundWithoutName.csv", "simplePrintTagCompoundWithoutName.csv" })
	public void parseTagCompoundWithoutName(String name, @ConvertWith(TagListArgumentConverter.class) List<Tag> data,
											String input) throws IOException {
		NBTLanguageParser parser = new NBTLanguageParser();
		TagCompound tag = (TagCompound) parser.parse(input);

		assertIterableEquals(data, tag.tags());
	}
}
