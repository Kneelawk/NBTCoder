package com.github.kneelawk.nbtcoder.test.hexlanguage;

import com.github.kneelawk.nbtcoder.hexlanguage.HexLanguageParser;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
