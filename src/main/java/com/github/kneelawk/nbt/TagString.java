package com.github.kneelawk.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TagString extends AbstractTag {

	private String value;

	public TagString() {
		value = "";
	}

	public TagString(String name) {
		super(name);
		value = "";
	}

	public TagString(String name, String value) {
		super(name);

		if (value == null) {
			throw new NullPointerException("A TagString's value cannot be null");
		}

		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		if (value == null) {
			throw new NullPointerException("A TagString's value cannot be null");
		}

		this.value = value;
	}

	@Override
	public void read(DataInput in, TagFactory factory) throws IOException {
		value = in.readUTF();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(value);
	}

	@Override
	public byte getId() {
		return NBTValues.TAG_STRING;
	}

	@Override
	public TagString copy() {
		return new TagString(getName(), value);
	}

}
