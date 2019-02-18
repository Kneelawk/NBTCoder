package com.github.kneelawk.nbtcoder.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

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
		TagByteArray other = (TagByteArray) obj;
		return Arrays.equals(value, other.value);
	}

	@Override
	public String toString() {
		return "TagByteArray(" + getName() + ", " + Arrays.toString(value) + ")";
	}

}
