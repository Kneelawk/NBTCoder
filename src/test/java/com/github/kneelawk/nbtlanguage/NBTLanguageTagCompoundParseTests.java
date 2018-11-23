package com.github.kneelawk.nbtlanguage;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.github.kneelawk.nbt.Tag;
import com.github.kneelawk.nbt.TagCompound;

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
