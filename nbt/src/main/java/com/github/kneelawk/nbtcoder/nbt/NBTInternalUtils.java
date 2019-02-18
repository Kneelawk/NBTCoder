package com.github.kneelawk.nbtcoder.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTInternalUtils {
	public static Tag readNamedTag(DataInput in, TagFactory factory) throws IOException {
		byte type = in.readByte();

		if (type == NBTValues.TAG_END) {
			return new TagEnd();
		}

		String name = in.readUTF();

		Tag ntag = factory.createTag(type, name);
		ntag.read(in, factory);
		return ntag;
	}

	public static void writeNamedTag(Tag tag, DataOutput out) throws IOException {
		byte type = tag.getId();
		out.writeByte(type);

		if (type == NBTValues.TAG_END) {
			return;
		}

		out.writeUTF(tag.getName());
		tag.write(out);
	}
}
