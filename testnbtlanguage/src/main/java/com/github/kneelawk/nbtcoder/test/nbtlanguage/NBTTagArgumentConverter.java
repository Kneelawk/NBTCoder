package com.github.kneelawk.nbtcoder.test.nbtlanguage;

import com.github.kneelawk.nbtcoder.nbt.Tag;
import com.github.kneelawk.nbtcoder.nbtlanguage.NBTLanguageParser;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class NBTTagArgumentConverter extends SimpleArgumentConverter {

	private NBTLanguageParser parser = new NBTLanguageParser();

	@Override
	protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
		assertTrue(Tag.class.isAssignableFrom(targetType));

		String input = (String) source;

		Tag tag;
		try {
			tag = parser.parse(input);
		} catch (IOException e) {
			throw new ArgumentConversionException("Unable to parse tag", e);
		}

		assertTrue(targetType.isAssignableFrom(tag.getClass()));

		return tag;
	}

}
