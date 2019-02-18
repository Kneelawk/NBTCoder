package com.github.kneelawk.nbtcoder.test.utils;

import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LongArrayArgumentConverter extends SimpleArgumentConverter {
	@Override
	protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
		assertEquals(long[].class, targetType);

		String input = (String) source;
		if (input.isEmpty()) {
			return new long[0];
		}

		return Arrays.stream(input.split(",")).mapToLong(s -> Long.parseLong(s.trim())).toArray();
	}
}
