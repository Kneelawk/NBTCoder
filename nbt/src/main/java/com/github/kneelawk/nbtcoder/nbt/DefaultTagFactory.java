package com.github.kneelawk.nbtcoder.nbt;

import java.io.IOException;

public class DefaultTagFactory implements TagFactory {
	public DefaultTagFactory() {
	}

	@Override
	public Tag createTag(byte type, String name) throws IOException {
		switch (type) {
		case NBTValues.TAG_BYTE:
			return new TagByte(name);
		case NBTValues.TAG_SHORT:
			return new TagShort(name);
		case NBTValues.TAG_INT:
			return new TagInt(name);
		case NBTValues.TAG_LONG:
			return new TagLong(name);
		case NBTValues.TAG_FLOAT:
			return new TagFloat(name);
		case NBTValues.TAG_DOUBLE:
			return new TagDouble(name);
		case NBTValues.TAG_BYTE_ARRAY:
			return new TagByteArray(name);
		case NBTValues.TAG_STRING:
			return new TagString(name);
		case NBTValues.TAG_LIST:
			return new TagList<Tag>(name);
		case NBTValues.TAG_COMPOUND:
			return new TagCompound(name);
		case NBTValues.TAG_INT_ARRAY:
			return new TagIntArray(name);
		case NBTValues.TAG_LONG_ARRAY:
			return new TagLongArray(name);
		default:
			throw new IOException("Unknown tag type: " + type);
		}
	}
}
