package com.github.kneelawk.nbt;

public class NBTUtils {
	public static String getTagType(Tag tag) {
		switch (tag.getId()) {
		case NBTValues.TAG_BYTE:
			return "tag_byte";
		case NBTValues.TAG_SHORT:
			return "tag_short";
		case NBTValues.TAG_INT:
			return "tag_int";
		case NBTValues.TAG_LONG:
			return "tag_long";
		case NBTValues.TAG_FLOAT:
			return "tag_float";
		case NBTValues.TAG_DOUBLE:
			return "tag_double";
		case NBTValues.TAG_BYTE_ARRAY:
			return "tag_byte_array";
		case NBTValues.TAG_STRING:
			return "tag_string";
		case NBTValues.TAG_LIST:
			return "tag_list";
		case NBTValues.TAG_COMPOUND:
			return "tag_compound";
		case NBTValues.TAG_INT_ARRAY:
			return "tag_int_array";
		case NBTValues.TAG_LONG_ARRAY:
			return "tag_long_array";
		default:
			return "unknown";
		}
	}
}
