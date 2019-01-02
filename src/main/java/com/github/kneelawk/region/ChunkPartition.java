package com.github.kneelawk.region;

import java.io.IOException;

import com.github.kneelawk.nbt.Tag;
import com.github.kneelawk.nbt.TagFactory;

public interface ChunkPartition {

	Tag readTag(TagFactory factory) throws IOException;

	byte getCompressionType();

	int getX();

	int getZ();

	byte[] getPaddingData();

	boolean hasPaddingData();

	int size();

	int getTimestamp();

}