package com.github.kneelawk.nbtcoder.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TagEnd implements Tag {

	public TagEnd() {
	}

	@Override
	public void read(DataInput in, TagFactory factory) throws IOException {
	}

	@Override
	public void write(DataOutput out) throws IOException {
	}

	@Override
	public String getName() {
		return "";
	}

	@Override
	public byte getId() {
		return NBTValues.TAG_END;
	}

	@Override
	public TagEnd copy() {
		return new TagEnd();
	}

	@Override
	public String toString() {
		return "TagEnd()";
	}

}
