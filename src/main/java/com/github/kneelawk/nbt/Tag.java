package com.github.kneelawk.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public interface Tag {
	public abstract void read(DataInput in, TagFactory factory) throws IOException;

	public abstract void write(DataOutput out) throws IOException;

	public abstract String getName();

	public abstract byte getId();

	public abstract Tag copy();
}
