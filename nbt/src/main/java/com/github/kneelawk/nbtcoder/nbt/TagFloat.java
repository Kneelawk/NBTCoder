package com.github.kneelawk.nbtcoder.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TagFloat extends AbstractTag {

	private float value;

	public TagFloat() {
		value = 0;
	}

	public TagFloat(String name) {
		super(name);
		value = 0;
	}

	public TagFloat(String name, float value) {
		super(name);
		this.value = value;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	@Override
	public void read(DataInput in, TagFactory factory) throws IOException {
		value = in.readFloat();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeFloat(value);
	}

	@Override
	public byte getId() {
		return NBTValues.TAG_FLOAT;
	}

	@Override
	public TagFloat copy() {
		return new TagFloat(getName(), value);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Float.floatToIntBits(value);
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
		TagFloat other = (TagFloat) obj;
		return Float.floatToIntBits(value) == Float.floatToIntBits(other.value);
	}

	@Override
	public String toString() {
		return "TagFloat(" + getName() + ", " + value + ")";
	}

}
