package com.github.kneelawk.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

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
		for (int i = 0; i < value.length; i++) {
			out.writeInt(value[i]);
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

}
