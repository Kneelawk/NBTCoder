package com.github.kneelawk.hexlanguage;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;

public class HexArgumentConverter extends SimpleArgumentConverter {

	private HexLanguageParser parser = new HexLanguageParser();

	@Override
	protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
		assertEquals(byte[].class, targetType);
		String input = (String) source;

		try {
			return parser.parse(input);
		} catch (IOException e) {
			throw new ArgumentConversionException("Unable to parse hex data", e);
		}
	}

}
