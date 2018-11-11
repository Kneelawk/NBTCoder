package com.github.kneelawk.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TagInt extends AbstractTag {

	private int value;

	public TagInt() {
		super();
		value = 0;
	}

	public TagInt(String name) {
		super(name);
		value = 0;
	}

	public TagInt(String name, int value) {
		super(name);
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public void read(DataInput in, TagFactory factory) throws IOException {
		value = in.readInt();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(value);
	}

	@Override
	public byte getId() {
		return NBTValues.TAG_INT;
	}

	@Override
	public TagInt copy() {
		return new TagInt(getName(), value);
	}

}
