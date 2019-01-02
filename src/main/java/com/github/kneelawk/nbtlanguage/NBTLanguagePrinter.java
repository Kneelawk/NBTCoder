package com.github.kneelawk.nbtlanguage;

import static com.github.kneelawk.utils.StringUtils.*;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	private static final Pattern LEGAL_STRING = Pattern.compile("[^:;,{}\\[\\] \\t\\r\\n]+");

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

		public Builder setPrettyPrint(boolean prettyPrint) {
			this.prettyPrint = prettyPrint;
			return this;
		}

		public boolean isPrintRootName() {
			return printRootName;
		}

		public Builder setPrintRootName(boolean printRootName) {
			this.printRootName = printRootName;
			return this;
		}
	}

	private final boolean prettyPrint;
	private final boolean printRootName;

	public NBTLanguagePrinter(boolean prettyPrint, boolean printRootName) {
		this.prettyPrint = prettyPrint;
		this.printRootName = printRootName;
	}

	public String print(Tag tag) {
		StringBuilder sb = new StringBuilder();
		print(tag, sb);
		return sb.toString();
	}

	public void print(Tag tag, StringBuilder sb) {
		String name = tag.getName();
		if (!printRootName || name.isEmpty()) {
			printTag(tag, sb, 0);
		} else {
			printName(name, sb);
			sb.append(":");
			if (prettyPrint)
				sb.append(" ");
			printTag(tag, sb, 0);
		}
	}

	@SuppressWarnings("unchecked")
	protected void printTag(Tag tag, StringBuilder sb, int indent) {
		switch (tag.getId()) {
		case NBTValues.TAG_BYTE:
			printByte((TagByte) tag, sb, indent);
			break;
		case NBTValues.TAG_SHORT:
			printShort((TagShort) tag, sb, indent);
			break;
		case NBTValues.TAG_INT:
			printInt((TagInt) tag, sb, indent);
			break;
		case NBTValues.TAG_LONG:
			printLong((TagLong) tag, sb, indent);
			break;
		case NBTValues.TAG_FLOAT:
			printFloat((TagFloat) tag, sb, indent);
			break;
		case NBTValues.TAG_DOUBLE:
			printDouble((TagDouble) tag, sb, indent);
			break;
		case NBTValues.TAG_BYTE_ARRAY:
			printByteArray((TagByteArray) tag, sb, indent);
			break;
		case NBTValues.TAG_STRING:
			printString((TagString) tag, sb, indent);
			break;
		case NBTValues.TAG_LIST:
			printList((TagList<Tag>) tag, sb, indent);
			break;
		case NBTValues.TAG_COMPOUND:
			printCompound((TagCompound) tag, sb, indent);
			break;
		case NBTValues.TAG_INT_ARRAY:
			printIntArray((TagIntArray) tag, sb, indent);
			break;
		case NBTValues.TAG_LONG_ARRAY:
			printLongArray((TagLongArray) tag, sb, indent);
			break;
		default:
			throw new IllegalArgumentException("Unknown tag type: " + tag.getId());
		}
	}

	protected void printName(String name, StringBuilder sb) {
		Matcher match = LEGAL_STRING.matcher(name);
		if (!match.matches()) {
			sb.append("\"").append(name.replace("\"", "\\\"")).append("\"");
		} else {
			sb.append(name);
		}
	}

	protected void printByte(TagByte tag, StringBuilder sb, int indent) {
		sb.append(tag.getValue()).append("b");
	}

	protected void printShort(TagShort tag, StringBuilder sb, int indent) {
		sb.append(tag.getValue()).append("s");
	}

	protected void printInt(TagInt tag, StringBuilder sb, int indent) {
		sb.append(tag.getValue());
	}

	protected void printLong(TagLong tag, StringBuilder sb, int indent) {
		sb.append(tag.getValue()).append("l");
	}

	protected void printFloat(TagFloat tag, StringBuilder sb, int indent) {
		sb.append(tag.getValue()).append("f");
	}

	protected void printDouble(TagDouble tag, StringBuilder sb, int indent) {
		sb.append(tag.getValue()).append("d");
	}

	protected void printByteArray(TagByteArray tag, StringBuilder sb, int indent) {
		sb.append("[B;");
		byte[] data = tag.getValue();
		if (data.length > 0) {
			if (prettyPrint) {
				sb.append("\n");
				tabs(indent + 1, sb);
			}
			for (int i = 0; i < data.length; i++) {
				if (prettyPrint) {
					sb.append(String.format("%3d", data[i]));
				} else {
					sb.append(data[i]);
				}
				if (i < data.length - 1) {
					sb.append(",");
					if (prettyPrint && i % BYTE_ARRAY_LINE_LENGTH == BYTE_ARRAY_LINE_LENGTH - 1) {
						sb.append("\n");
						tabs(indent + 1, sb);
					}
				}
			}
			if (prettyPrint) {
				sb.append("\n");
				tabs(indent, sb);
			}
		}
		sb.append("]");
	}

	protected void printString(TagString tag, StringBuilder sb, int indent) {
		sb.append("\"").append(tag.getValue().replace("\"", "\\\"")).append("\"");
	}

	protected void printList(TagList<Tag> tag, StringBuilder sb, int indent) {
		sb.append("[");
		int size = tag.size();
		if (size > 0) {
			if (prettyPrint) {
				sb.append("\n");
				tabs(indent + 1, sb);
			}
			for (int i = 0; i < size; i++) {
				printTag(tag.get(i), sb, indent + 1);
				if (i < size - 1) {
					sb.append(",");
					if (prettyPrint) {
						sb.append("\n");
						tabs(indent + 1, sb);
					}
				}
			}
			if (prettyPrint) {
				sb.append("\n");
				tabs(indent, sb);
			}
		}
		sb.append("]");
	}

	protected void printCompound(TagCompound tag, StringBuilder sb, int indent) {
		sb.append("{");
		if (!tag.isEmpty()) {
			if (prettyPrint) {
				sb.append("\n");
				tabs(indent + 1, sb);
			}
			for (Iterator<Tag> it = tag.tags().iterator(); it.hasNext();) {
				Tag child = it.next();
				printName(child.getName(), sb);
				sb.append(":");
				if (prettyPrint)
					sb.append(" ");
				printTag(child, sb, indent + 1);
				if (it.hasNext()) {
					sb.append(",");
					if (prettyPrint) {
						sb.append("\n");
						tabs(indent + 1, sb);
					}
				}
			}
			if (prettyPrint) {
				sb.append("\n");
				tabs(indent, sb);
			}
		}
		sb.append("}");
	}

	protected void printIntArray(TagIntArray tag, StringBuilder sb, int indent) {
		sb.append("[I;");
		int[] data = tag.getValue();
		if (data.length > 0) {
			if (prettyPrint) {
				sb.append("\n");
				tabs(indent + 1, sb);
			}
			for (int i = 0; i < data.length; i++) {
				if (prettyPrint) {
					sb.append(String.format("%3d", data[i]));
				} else {
					sb.append(data[i]);
				}
				if (i < data.length - 1) {
					sb.append(",");
					if (prettyPrint && i % INT_ARRAY_LINE_LENGTH == INT_ARRAY_LINE_LENGTH - 1) {
						sb.append("\n");
						tabs(indent + 1, sb);
					}
				}
			}
			if (prettyPrint) {
				sb.append("\n");
				tabs(indent, sb);
			}
		}
		sb.append("]");
	}

	protected void printLongArray(TagLongArray tag, StringBuilder sb, int indent) {
		sb.append("[L;");
		long[] data = tag.getValue();
		if (data.length > 0) {
			if (prettyPrint) {
				sb.append("\n");
				tabs(indent + 1, sb);
			}
			for (int i = 0; i < data.length; i++) {
				if (prettyPrint) {
					sb.append(String.format("%3d", data[i]));
				} else {
					sb.append(data[i]);
				}
				if (i < data.length - 1) {
					sb.append(",");
					if (prettyPrint && i % LONG_ARRAY_LINE_LENGTH == LONG_ARRAY_LINE_LENGTH - 1) {
						sb.append("\n");
						tabs(indent + 1, sb);
					}
				}
			}
			if (prettyPrint) {
				sb.append("\n");
				tabs(indent, sb);
			}
		}
		sb.append("]");
	}
}
