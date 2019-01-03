package com.github.kneelawk.nbtlanguage;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;

public class IntArrayArgumentConverter extends SimpleArgumentConverter {
	@Override
	protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
		assertEquals(int[].class, targetType);

		String input = (String) source;
		if (input.isEmpty()) {
			return new int[0];
		}

		return Arrays.stream(input.split(",")).mapToInt(s -> Integer.parseInt(s.trim())).toArray();
	}
}
