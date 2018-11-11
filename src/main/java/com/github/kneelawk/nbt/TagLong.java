package com.github.kneelawk.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TagLong extends AbstractTag {

	private long value;

	public TagLong() {
		value = 0;
	}

	public TagLong(String name) {
		super(name);
		value = 0;
	}

	public TagLong(String name, long value) {
		super(name);
		this.value = value;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	@Override
	public void read(DataInput in, TagFactory factory) throws IOException {
		value = in.readLong();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeLong(value);
	}

	@Override
	public byte getId() {
		return NBTValues.TAG_LONG;
	}

	@Override
	public TagLong copy() {
		return new TagLong(getName(), value);
	}

}
