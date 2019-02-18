package com.github.kneelawk.nbtcoder.region;

import java.io.IOException;

import com.github.kneelawk.nbtcoder.nbt.Tag;
import com.github.kneelawk.nbtcoder.nbt.TagFactory;

public interface ChunkPartition extends Partition {

	Tag readTag(TagFactory factory) throws IOException;

	byte getCompressionType();

	int getX();

	int getZ();

	byte[] getPaddingData();

	boolean hasPaddingData();

	int size();

	int getTimestamp();

}