package com.github.kneelawk.nbt;

public class NBTUtils {
	public static String getTagType(Tag tag) {
		return getTagType(tag.getId());
	}

	public static String getTagType(byte type) {
		switch (type) {
		case NBTValues.TAG_BYTE:
			return "TagByte";
		case NBTValues.TAG_SHORT:
			return "TagShort";
		case NBTValues.TAG_INT:
			return "TagInt";
		case NBTValues.TAG_LONG:
			return "TagLong";
		case NBTValues.TAG_FLOAT:
			return "TagFloat";
		case NBTValues.TAG_DOUBLE:
			return "TagDouble";
		case NBTValues.TAG_BYTE_ARRAY:
			return "TagByteArray";
		case NBTValues.TAG_STRING:
			return "TagString";
		case NBTValues.TAG_LIST:
			return "TagList";
		case NBTValues.TAG_COMPOUND:
			return "TagCompound";
		case NBTValues.TAG_INT_ARRAY:
			return "TagIntArray";
		case NBTValues.TAG_LONG_ARRAY:
			return "TagLongArray";
		default:
			return "TagUnknown" + type;
		}
	}
}
