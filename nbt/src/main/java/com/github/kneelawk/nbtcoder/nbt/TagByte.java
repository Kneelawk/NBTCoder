package com.github.kneelawk.nbtcoder.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TagByte extends AbstractTag {

	private byte value;

	public TagByte() {
		super();
		value = 0;
	}

	public TagByte(String name) {
		super(name);
		value = 0;
	}

	public TagByte(String name, byte value) {
		super(name);
		this.value = value;
	}

	public byte getValue() {
		return value;
	}

	public void setValue(byte value) {
		this.value = value;
	}

	@Override
	public void read(DataInput in, TagFactory factory) throws IOException {
		value = in.readByte();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeByte(value);
	}

	@Override
	public byte getId() {
		return NBTValues.TAG_BYTE;
	}

	@Override
	public TagByte copy() {
		return new TagByte(getName(), value);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + value;
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
		TagByte other = (TagByte) obj;
		if (value != other.value)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TagByte(" + getName() + ", " + value + ")";
	}

}
