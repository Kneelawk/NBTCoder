package com.github.kneelawk.nbtcoder.test.nbtlanguage;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.github.kneelawk.nbtcoder.nbtlanguage.NBTLanguageParser;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;

import com.github.kneelawk.nbtcoder.nbt.Tag;
import com.google.common.collect.ImmutableList;

public class TagListArgumentConverter extends SimpleArgumentConverter {
	@Override
	protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
		assertTrue(targetType.isAssignableFrom(List.class));

		String input = (String) source;
		if (input.isEmpty()) {
			return ImmutableList.<Tag>of();
		}

		NBTLanguageParser parser = new NBTLanguageParser();

		return Arrays.stream(input.split("\\|")).map(str -> {
			try {
				return parser.parse(str);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}).collect(Collectors.toList());
	}

}
