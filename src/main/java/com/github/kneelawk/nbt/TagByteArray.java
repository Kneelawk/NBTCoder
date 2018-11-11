package com.github.kneelawk.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TagByteArray extends AbstractTag {

	private byte[] value;

	public TagByteArray() {
		value = new byte[0];
	}

	public TagByteArray(String name) {
		super(name);
		value = new byte[0];
	}

	public TagByteArray(String name, byte[] value) {
		super(name);
		this.value = value;
	}

	public byte[] getValue() {
		return value;
	}

	public void setValue(byte[] value) {
		this.value = value;
	}

	@Override
	public void read(DataInput in, TagFactory factory) throws IOException {
		int length = in.readInt();
		value = new byte[length];
		in.readFully(value);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(value.length);
		out.write(value);
	}

	@Override
	public byte getId() {
		return NBTValues.TAG_BYTE_ARRAY;
	}

	@Override
	public Tag copy() {
		byte[] nvalue = new byte[value.length];
		System.arraycopy(value, 0, nvalue, 0, value.length);
		return new TagByteArray(getName(), nvalue);
	}

}
