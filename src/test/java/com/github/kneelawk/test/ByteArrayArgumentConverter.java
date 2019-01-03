package com.github.kneelawk.nbtlanguage;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;

public class ByteArrayArgumentConverter extends SimpleArgumentConverter {
	@Override
	protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
		assertEquals(byte[].class, targetType);
		String input = (String) source;

		if (input.isEmpty()) {
			return new byte[0];
		}

		String[] parts = input.split(",");
		byte[] data = new byte[parts.length];
		for (int i = 0; i < parts.length; i++) {
			data[i] = Byte.parseByte(parts[i].trim());
		}

		return data;
	}
}