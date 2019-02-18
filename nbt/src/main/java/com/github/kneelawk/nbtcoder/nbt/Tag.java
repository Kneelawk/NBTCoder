package com.github.kneelawk.nbtcoder.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public interface Tag {
	void read(DataInput in, TagFactory factory) throws IOException;

	void write(DataOutput out) throws IOException;

	String getName();

	byte getId();

	Tag copy();
}
