package com.github.kneelawk.nbtcoder.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class TagLongArray extends AbstractTag {

	private long[] value;

	public TagLongArray() {
		value = new long[0];
	}

	public TagLongArray(String name) {
		super(name);
		value = new long[0];
	}

	public TagLongArray(String name, long[] value) {
		super(name);
		this.value = value;
	}

	public long[] getValue() {
		return value;
	}

	public void setValue(long[] value) {
		this.value = value;
	}

	@Override
	public void read(DataInput in, TagFactory factory) throws IOException {
		int length = in.readInt();
		value = new long[length];
		for (int i = 0; i < length; i++) {
			value[i] = in.readLong();
		}
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(value.length);
		for (int i = 0; i < value.length; i++) {
			out.writeLong(value[i]);
		}
	}

	@Override
	public byte getId() {
		return NBTValues.TAG_LONG_ARRAY;
	}

	@Override
	public TagLongArray copy() {
		long[] nvalue = new long[0];
		System.arraycopy(value, 0, nvalue, 0, value.length);
		return new TagLongArray(getName(), nvalue);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(value);
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
		TagLongArray other = (TagLongArray) obj;
		return Arrays.equals(value, other.value);
	}

	@Override
	public String toString() {
		return "TagLongArray(" + getName() + ", " + Arrays.toString(value) + ")";
	}

}
