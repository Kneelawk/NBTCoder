package com.github.kneelawk.nbtcoder.nbt;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (int) (value ^ (value >>> 32));
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
		TagLong other = (TagLong) obj;
		return value == other.value;
	}

	@Override
	public String toString() {
		return "TagLong(" + getName() + ", " + value + ")";
	}

}
