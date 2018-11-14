package com.github.kneelawk.nbtcoder;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.github.kneelawk.nbt.AbstractTag;
import com.github.kneelawk.nbt.NBTValues;
import com.github.kneelawk.nbt.Tag;
import com.github.kneelawk.nbt.TagByte;
import com.github.kneelawk.nbt.TagByteArray;
import com.github.kneelawk.nbt.TagDouble;
import com.github.kneelawk.nbt.TagFloat;
import com.github.kneelawk.nbt.TagInt;
import com.github.kneelawk.nbt.TagIntArray;
import com.github.kneelawk.nbt.TagList;
import com.github.kneelawk.nbt.TagLong;
import com.github.kneelawk.nbt.TagLongArray;
import com.github.kneelawk.nbt.TagShort;
import com.github.kneelawk.nbt.TagString;
import com.github.kneelawk.nbtcoder.NBTCoderParser.CompoundItemContext;
import com.github.kneelawk.nbtcoder.NBTCoderParser.ListItemContext;
import com.github.kneelawk.nbtcoder.NBTCoderParser.NbtFileContext;
import com.github.kneelawk.nbtcoder.NBTCoderParser.TagBodyContext;
import com.github.kneelawk.nbtcoder.NBTCoderParser.TagCompoundContext;
import com.github.kneelawk.nbtcoder.NBTCoderParser.TagListContext;
import com.github.kneelawk.nbtcoder.NBTCoderParser.TagStringContext;
import com.github.kneelawk.nbtcoder.NBTCoderParser.TagTypedArrayContext;
import com.github.kneelawk.nbtcoder.NBTCoderParser.TypedArrayItemContext;

public class NBTCoderBuilderListener extends NBTCoderBaseListener {

	private static final Pattern NUMBER = Pattern.compile("([0-9]*\\.)?[0-9]+[a-zA-Z]?");
	private static final BitSet STAGS = new BitSet();

	static {
		STAGS.set(NBTValues.TAG_BYTE, NBTValues.TAG_DOUBLE + 1);
	}

	private AbstractTag root;
	private Stack<AbstractTag> tags = new Stack<>();
	private Stack<List<AbstractTag>> listItems = new Stack<>();
	private List<Object> typedArrayItems = new ArrayList<>();

	public Tag getRoot() {
		return root;
	}

	@Override
	public void enterNbtFile(NbtFileContext ctx) {
	}

	@Override
	public void exitNbtFile(NbtFileContext ctx) {
		root = tags.pop();
		if (ctx.STRING() != null) {
			root.setName(ctx.STRING().getText());
		}
	}

	@Override
	public void enterTagBody(TagBodyContext ctx) {

	}

	@Override
	public void exitTagBody(TagBodyContext ctx) {

	}

	@Override
	public void enterTagString(TagStringContext ctx) {

	}

	@Override
	public void exitTagString(TagStringContext ctx) {
		String str = ctx.STRING().getText();
		AbstractTag tag = null;
		try {
			tag = parseString(str);
		} catch (TagParseException e) {
			throw new NBTLanguageParseException(e, ctx);
		}
		tags.push(tag);
	}

	@Override
	public void enterTagList(TagListContext ctx) {
		listItems.push(new ArrayList<>());
	}

	@Override
	public void exitTagList(TagListContext ctx) {
		TagList<Tag> list = new TagList<>();
		List<AbstractTag> items = listItems.pop();
		byte type = 1;
		try {
			type = determineListType(items);
		} catch (IncompatibleTagTypeException e) {
			throw new NBTLanguageParseException(e, ctx);
		}
		for (AbstractTag tag : items) {
			try {
				list.add(convertTag(tag, type));
			} catch (IncompatibleTagTypeException e) {
				throw new NBTLanguageParseException(e, ctx);
			}
		}
		tags.push(list);
	}

	@Override
	public void enterListItem(ListItemContext ctx) {

	}

	@Override
	public void exitListItem(ListItemContext ctx) {
		listItems.peek().add(tags.pop());
	}

	@Override
	public void enterTagTypedArray(TagTypedArrayContext ctx) {

	}

	@Override
	public void exitTagTypedArray(TagTypedArrayContext ctx) {
		String arrayTypeStr = ctx.STRING().getText().toLowerCase();
		AbstractTag array;
		switch (arrayTypeStr) {
		case "b":
			byte[] bytes = new byte[typedArrayItems.size()];
			for (int i = 0; i < typedArrayItems.size(); i++) {
				bytes[i] = (byte) typedArrayItems.get(i);
			}
			array = new TagByteArray("", bytes);
			break;
		case "i":
			int[] ints = typedArrayItems.stream().mapToInt(o -> (int) o).toArray();
			array = new TagIntArray("", ints);
			break;
		case "l":
			long[] longs = typedArrayItems.stream().mapToLong(o -> (long) o).toArray();
			array = new TagLongArray("", longs);
			break;
		default:
			throw new NBTLanguageParseException("Unknown typed array type: " + arrayTypeStr, ctx);
		}
		typedArrayItems.clear();
		tags.push(array);
	}

	@Override
	public void enterTypedArrayItem(TypedArrayItemContext ctx) {

	}

	@Override
	public void exitTypedArrayItem(TypedArrayItemContext ctx) {

	}

	@Override
	public void enterTagCompound(TagCompoundContext ctx) {

	}

	@Override
	public void exitTagCompound(TagCompoundContext ctx) {

	}

	@Override
	public void enterCompoundItem(CompoundItemContext ctx) {

	}

	@Override
	public void exitCompoundItem(CompoundItemContext ctx) {

	}

	@Override
	public void visitTerminal(TerminalNode node) {

	}

	@Override
	public void visitErrorNode(ErrorNode node) {

	}

	private byte determineListType(List<AbstractTag> list) throws IncompatibleTagTypeException {
		byte type = 0;
		for (Tag tag : list) {
			type = compareListTypes(type, tag.getId());
		}
		return type;
	}

	private byte compareListTypes(byte currentType, byte tagType) throws IncompatibleTagTypeException {
		if (currentType == 0) {
			return tagType;
		} else if (currentType == tagType) {
			return tagType;
		} else if (!STAGS.get(currentType) || !STAGS.get(tagType)) {
			throw new IncompatibleTagTypeException("List contains different types of complex tags");
		} else if (currentType == NBTValues.TAG_INT) {
			return tagType;
		} else {
			throw new IncompatibleTagTypeException("List contains different types of tags");
		}
	}

	private AbstractTag convertTag(AbstractTag oldTag, byte type) throws IncompatibleTagTypeException {
		if (oldTag.getId() == type) {
			return oldTag;
		} else if (oldTag.getId() == NBTValues.TAG_INT) {
			int value = ((TagInt) oldTag).getValue();
			switch (type) {
			case NBTValues.TAG_BYTE:
				if (value > Byte.MAX_VALUE || value < Byte.MIN_VALUE) {
					throw new IncompatibleTagTypeException("This integer is too big to be a byte");
				} else {
					return new TagByte("", (byte) value);
				}
			case NBTValues.TAG_SHORT:
				if (value > Short.MAX_VALUE || value < Short.MIN_VALUE) {
					throw new IncompatibleTagTypeException("This integer is too big to be a short");
				} else {
					return new TagShort("", (short) value);
				}
			case NBTValues.TAG_LONG:
				return new TagLong("", value);
			case NBTValues.TAG_FLOAT:
				return new TagFloat("", value);
			case NBTValues.TAG_DOUBLE:
				return new TagDouble("", value);
			case NBTValues.TAG_STRING:
				return new TagString("", String.valueOf(value));
			default:
				throw new IncompatibleTagTypeException("Unable to convert a TagInt into a tag of type: " + type);
			}
		} else {
			throw new IncompatibleTagTypeException("Unable to convert between different types of tags");
		}
	}

	private AbstractTag parseString(String str) throws TagParseException {
		Matcher match = NUMBER.matcher(str);
		if (str.startsWith("\"") && str.endsWith("\"")) {
			return new TagString(str.substring(1, str.length() - 1));
		} else if (match.matches()) {
			char type = Character.toLowerCase(str.charAt(str.length() - 1));
			if (Character.isDigit(type)) {
				if (str.contains(".")) {
					return new TagDouble("", Double.parseDouble(str));
				} else {
					return new TagInt("", Integer.parseInt(str));
				}
			} else {
				if (str.contains(".")) {
					switch (type) {
					case 'f':
						return new TagFloat("", Float.parseFloat(str));
					case 'd':
						return new TagDouble("", Double.parseDouble(str));
					default:
						throw new TagParseException(
								"Strange floating point number suffix: " + type + ", number: " + str);
					}
				} else {
					switch (type) {
					case 'b':
						return new TagByte("", Byte.parseByte(str));
					case 's':
						return new TagShort("", Short.parseShort(str));
					case 'l':
						return new TagLong("", Long.parseLong(str));
					case 'f':
						return new TagFloat("", Float.parseFloat(str));
					case 'd':
						return new TagDouble("", Double.parseDouble(str));
					default:
						throw new TagParseException("Strange integer number suffix: " + type + ", number: " + str);
					}
				}
			}
		} else {
			return new TagString("", str);
		}
	}
}
