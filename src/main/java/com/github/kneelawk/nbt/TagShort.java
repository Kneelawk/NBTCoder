package com.github.kneelawk.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TagShort extends AbstractTag {

	private short value;

	public TagShort() {
		super();
		value = 0;
	}

	public TagShort(String name) {
		super(name);
		value = 0;
	}

	public TagShort(String name, short value) {
		super(name);
		this.value = value;
	}

	public short getValue() {
		return value;
	}

	public void setValue(short value) {
		this.value = value;
	}

	@Override
	public void read(DataInput in, TagFactory factory) throws IOException {
		value = in.readShort();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeShort(value);
	}

	@Override
	public byte getId() {
		return NBTValues.TAG_SHORT;
	}

	@Override
	public TagShort copy() {
		return new TagShort(getName(), value);
	}

}
