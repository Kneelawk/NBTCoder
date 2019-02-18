package com.github.kneelawk.nbtcoder.nbt;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		TagString other = (TagString) obj;
		if (value == null) {
			return other.value == null;
		} else return value.equals(other.value);
	}

	@Override
	public String toString() {
		return "TagString(" + getName() + ", " + value + ")";
	}

}
