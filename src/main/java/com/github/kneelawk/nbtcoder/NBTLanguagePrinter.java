package com.github.kneelawk.nbtcoder;

import static com.github.kneelawk.nbtcoder.StringUtils.*;

import java.util.Iterator;

import com.github.kneelawk.nbt.NBTValues;
import com.github.kneelawk.nbt.Tag;
import com.github.kneelawk.nbt.TagByte;
import com.github.kneelawk.nbt.TagByteArray;
import com.github.kneelawk.nbt.TagCompound;
import com.github.kneelawk.nbt.TagDouble;
import com.github.kneelawk.nbt.TagFloat;
import com.github.kneelawk.nbt.TagInt;
import com.github.kneelawk.nbt.TagIntArray;
import com.github.kneelawk.nbt.TagList;
import com.github.kneelawk.nbt.TagLong;
import com.github.kneelawk.nbt.TagLongArray;
import com.github.kneelawk.nbt.TagShort;
import com.github.kneelawk.nbt.TagString;

public class NBTLanguagePrinter {
	private static final int BYTE_ARRAY_LINE_LENGTH = 16;
	private static final int INT_ARRAY_LINE_LENGTH = 8;
	private static final int LONG_ARRAY_LINE_LENGTH = 4;

	public static class Builder {
		private boolean prettyPrint = true;
		private boolean printRootName = true;

		public Builder() {
		}

		public Builder(boolean prettyPrint, boolean printRootName) {
			this.prettyPrint = prettyPrint;
			this.printRootName = printRootName;
		}

		public NBTLanguagePrinter build() {
			return new NBTLanguagePrinter(prettyPrint, printRootName);
		}

		public boolean isPrettyPrint() {
			return prettyPrint;
		}

		public void setPrettyPrint(boolean prettyPrint) {
			this.prettyPrint = prettyPrint;
		}

		public boolean isPrintRootName() {
			return printRootName;
		}

		public void setPrintRootName(boolean printRootName) {
			this.printRootName = printRootName;
		}
	}

	private final boolean prettyPrint;
	private final boolean printRootName;

	public NBTLanguagePrinter(boolean prettyPrint, boolean printRootName) {
		this.prettyPrint = prettyPrint;
		this.printRootName = printRootName;
	}

	public String print(Tag tag) {
		String name = tag.getName();
		if (!printRootName || name.isEmpty()) {
			return printTag(tag, 0);
		} else {
			String s = name + ":";
			if (prettyPrint)
				s += " ";
			s += printTag(tag, 0);
			return s;
		}
	}

	@SuppressWarnings("unchecked")
	protected String printTag(Tag tag, int indent) {
		switch (tag.getId()) {
		case NBTValues.TAG_BYTE:
			return printByte((TagByte) tag, indent);
		case NBTValues.TAG_SHORT:
			return printShort((TagShort) tag, indent);
		case NBTValues.TAG_INT:
			return printInt((TagInt) tag, indent);
		case NBTValues.TAG_LONG:
			return printLong((TagLong) tag, indent);
		case NBTValues.TAG_FLOAT:
			return printFloat((TagFloat) tag, indent);
		case NBTValues.TAG_DOUBLE:
			return printDouble((TagDouble) tag, indent);
		case NBTValues.TAG_BYTE_ARRAY:
			return printByteArray((TagByteArray) tag, indent);
		case NBTValues.TAG_STRING:
			return printString((TagString) tag, indent);
		case NBTValues.TAG_LIST:
			return printList((TagList<Tag>) tag, indent);
		case NBTValues.TAG_COMPOUND:
			return printCompound((TagCompound) tag, indent);
		case NBTValues.TAG_INT_ARRAY:
			return printIntArray((TagIntArray) tag, indent);
		case NBTValues.TAG_LONG_ARRAY:
			return printLongArray((TagLongArray) tag, indent);
		default:
			throw new IllegalArgumentException("Unknown tag type: " + tag.getId());
		}
	}

	protected String printByte(TagByte tag, int indent) {
		return String.valueOf(tag.getValue()) + "b";
	}

	protected String printShort(TagShort tag, int indent) {
		return String.valueOf(tag.getValue()) + "s";
	}

	protected String printInt(TagInt tag, int indent) {
		return String.valueOf(tag.getValue());
	}

	protected String printLong(TagLong tag, int indent) {
		return String.valueOf(tag.getValue()) + "l";
	}

	protected String printFloat(TagFloat tag, int indent) {
		return String.valueOf(tag.getValue()) + "f";
	}

	protected String printDouble(TagDouble tag, int indent) {
		return String.valueOf(tag.getValue()) + "d";
	}

	protected String printByteArray(TagByteArray tag, int indent) {
		String s = "[B;";
		byte[] data = tag.getValue();
		if (data.length > 0) {
			if (prettyPrint)
				s += "\n" + tabs(indent + 1);
			for (int i = 0; i < data.length; i++) {
				if (prettyPrint)
					s += String.format("%3d", data[i]);
				else
					s += String.valueOf(data[i]);
				if (i < data.length - 1) {
					s += ",";
					if (prettyPrint && i % BYTE_ARRAY_LINE_LENGTH == BYTE_ARRAY_LINE_LENGTH - 1) {
						s += "\n" + tabs(indent + 1);
					}
				}
			}
			if (prettyPrint)
				s += "\n" + tabs(indent);
		}
		s += "]";
		return s;
	}

	protected String printString(TagString tag, int indent) {
		return "\"" + tag.getValue() + "\"";
	}

	protected String printList(TagList<Tag> tag, int indent) {
		String s = "[";
		int size = tag.size();
		if (size > 0) {
			if (prettyPrint)
				s += "\n" + tabs(indent + 1);
			for (int i = 0; i < size; i++) {
				s += printTag(tag.get(i), indent + 1);
				if (i < size - 1) {
					s += ",";
					if (prettyPrint)
						s += "\n" + tabs(indent + 1);
				}
			}
			if (prettyPrint)
				s += "\n" + tabs(indent);
		}
		s += "]";
		return s;
	}

	protected String printCompound(TagCompound tag, int indent) {
		String s = "{";
		if (!tag.isEmpty()) {
			if (prettyPrint)
				s += "\n" + tabs(indent + 1);
			for (Iterator<Tag> it = tag.tags().iterator(); it.hasNext();) {
				Tag child = it.next();
				s += child.getName() + ":";
				if (prettyPrint)
					s += " ";
				s += printTag(child, indent + 1);
				if (it.hasNext()) {
					s += ",";
					if (prettyPrint)
						s += "\n" + tabs(indent + 1);
				}
			}
			if (prettyPrint)
				s += "\n" + tabs(indent);
		}
		s += "}";
		return s;
	}

	protected String printIntArray(TagIntArray tag, int indent) {
		String s = "[I;";
		int[] data = tag.getValue();
		if (data.length > 0) {
			if (prettyPrint)
				s += "\n" + tabs(indent + 1);
			for (int i = 0; i < data.length; i++) {
				if (prettyPrint)
					s += String.format("%3d", data[i]);
				else
					s += String.valueOf(data[i]);
				if (i < data.length - 1) {
					s += ",";
					if (prettyPrint && i % INT_ARRAY_LINE_LENGTH == INT_ARRAY_LINE_LENGTH - 1) {
						s += "\n" + tabs(indent + 1);
					}
				}
			}
			if (prettyPrint)
				s += "\n" + tabs(indent);
		}
		s += "]";
		return s;
	}

	protected String printLongArray(TagLongArray tag, int indent) {
		String s = "[L;";
		long[] data = tag.getValue();
		if (data.length > 0) {
			if (prettyPrint)
				s += "\n" + tabs(indent + 1);
			for (int i = 0; i < data.length; i++) {
				if (prettyPrint)
					s += String.format("%3d", data[i]);
				else
					s += String.valueOf(data[i]);
				if (i < data.length - 1) {
					s += ",";
					if (prettyPrint && i % LONG_ARRAY_LINE_LENGTH == LONG_ARRAY_LINE_LENGTH - 1) {
						s += "\n" + tabs(indent + 1);
					}
				}
			}
			if (prettyPrint)
				s += "\n" + tabs(indent);
		}
		s += "]";
		return s;
	}
}
