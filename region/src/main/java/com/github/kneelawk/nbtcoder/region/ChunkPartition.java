package com.github.kneelawk.nbtcoder.region;

import com.github.kneelawk.nbtcoder.nbt.Tag;
import com.github.kneelawk.nbtcoder.nbt.TagFactory;

import java.io.IOException;

public interface ChunkPartition extends Partition {

	Tag readTag(TagFactory factory) throws IOException;

	byte getCompressionType();

	int getX();

	int getZ();

	int size();

	int getTimestamp();

}