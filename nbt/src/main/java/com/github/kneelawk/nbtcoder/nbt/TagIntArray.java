package com.github.kneelawk.nbtcoder.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class TagIntArray extends AbstractTag {

	private int[] value;

	public TagIntArray() {
		super();
		value = new int[0];
	}

	public TagIntArray(String name) {
		super(name);
		value = new int[0];
	}

	public TagIntArray(String name, int[] value) {
		super(name);
		this.value = value;
	}

	public int[] getValue() {
		return value;
	}

	public void setValue(int[] value) {
		this.value = value;
	}

	@Override
	public void read(DataInput in, TagFactory factory) throws IOException {
		int length = in.readInt();
		value = new int[length];
		for (int i = 0; i < length; i++) {
			value[i] = in.readInt();
		}
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(value.length);
		for (int item : value) {
			out.writeInt(item);
		}
	}

	@Override
	public byte getId() {
		return NBTValues.TAG_INT_ARRAY;
	}

	@Override
	public TagIntArray copy() {
		int[] nvalue = new int[value.length];
		System.arraycopy(value, 0, nvalue, 0, value.length);
		return new TagIntArray(getName(), nvalue);
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
		TagIntArray other = (TagIntArray) obj;
		return Arrays.equals(value, other.value);
	}

	@Override
	public String toString() {
		return "TagIntArray(" + getName() + ", " + Arrays.toString(value) + ")";
	}

}
