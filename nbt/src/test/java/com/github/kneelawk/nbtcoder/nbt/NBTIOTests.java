package com.github.kneelawk.nbtcoder.nbt;

import com.github.kneelawk.nbtcoder.test.hexlanguage.HexArgumentConverter;
import com.github.kneelawk.nbtcoder.test.nbtlanguage.NBTTagArgumentConverter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NBTIOTests {
	@ParameterizedTest
	@CsvFileSource(resources = { "uncompressedNBT.csv" })
	public void readUncompressedNBT(@ConvertWith(HexArgumentConverter.class) byte[] data,
									@ConvertWith(NBTTagArgumentConverter.class) Tag expected) throws IOException {
		TagFactory factory = new DefaultTagFactory();
		Tag actual = NBTIO.readStream(new ByteArrayInputStream(data), factory);

		assertEquals(expected, actual);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "uncompressedNBT.csv" })
	public void writeUncompressedNBT(@ConvertWith(HexArgumentConverter.class) byte[] expected,
									 @ConvertWith(NBTTagArgumentConverter.class) Tag tag) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		NBTIO.writeStream(tag, baos);

		assertArrayEquals(expected, baos.toByteArray());
	}
}
