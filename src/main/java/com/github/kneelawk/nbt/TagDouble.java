package com.github.kneelawk.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TagDouble extends AbstractTag {

	private double value;

	public TagDouble() {
		value = 0;
	}

	public TagDouble(String name) {
		super(name);
		value = 0;
	}

	public TagDouble(String name, double value) {
		super(name);
		this.value = value;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public void read(DataInput in, TagFactory factory) throws IOException {
		value = in.readDouble();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeDouble(value);
	}

	@Override
	public byte getId() {
		return NBTValues.TAG_DOUBLE;
	}

	@Override
	public TagDouble copy() {
		return new TagDouble(getName(), value);
	}

}
