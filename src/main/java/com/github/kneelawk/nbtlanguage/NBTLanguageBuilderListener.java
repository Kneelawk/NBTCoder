package com.github.kneelawk.nbtlanguage;

import java.util.BitSet;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.github.kneelawk.nbt.AbstractTag;
import com.github.kneelawk.nbt.NBTUtils;
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
import com.github.kneelawk.nbtlanguage.NBTLanguageSystemParser.CompoundItemContext;
import com.github.kneelawk.nbtlanguage.NBTLanguageSystemParser.ListItemContext;
import com.github.kneelawk.nbtlanguage.NBTLanguageSystemParser.NbtFileContext;
import com.github.kneelawk.nbtlanguage.NBTLanguageSystemParser.TagBodyContext;
import com.github.kneelawk.nbtlanguage.NBTLanguageSystemParser.TagCompoundContext;
import com.github.kneelawk.nbtlanguage.NBTLanguageSystemParser.TagListContext;
import com.github.kneelawk.nbtlanguage.NBTLanguageSystemParser.TagStringContext;
import com.github.kneelawk.nbtlanguage.NBTLanguageSystemParser.TagTypedArrayContext;
import com.github.kneelawk.nbtlanguage.NBTLanguageSystemParser.TypedArrayItemContext;
import com.github.kneelawk.utils.InternalParseException;
import com.google.common.collect.Lists;
import com.google.common.primitives.UnsignedBytes;

public class NBTLanguageBuilderListener extends NBTLanguageSystemBaseListener {

	private static final Pattern NUMBER = Pattern.compile("-?([0-9]*\\.)?[0-9]+([eE]-?[0-9]+)?[a-zA-Z]?");
	private static final BitSet STAGS = new BitSet();

	static {
		STAGS.set(NBTValues.TAG_BYTE, NBTValues.TAG_DOUBLE + 1);
	}

	private AbstractTag root;
	private Stack<AbstractTag> tags = new Stack<>();

	private Stack<List<AbstractTag>> listItems = new Stack<>();
	private Stack<List<AbstractTag>> compoundItems = new Stack<>();
	private List<String> typedArrayItems = Lists.newArrayList();

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
			root.setName(unescapeString(ctx.STRING().getText()));
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
			throw new InternalParseException(e, ctx);
		}
		tags.push(tag);
	}

	@Override
	public void enterTagList(TagListContext ctx) {
		listItems.push(Lists.newArrayList());
	}

	@Override
	public void exitTagList(TagListContext ctx) {
		TagList<Tag> list = new TagList<>();
		List<AbstractTag> items = listItems.pop();
		byte type = 0;
		try {
			type = determineListType(items);
		} catch (IncompatibleTagTypeException e) {
			throw new InternalParseException(e, ctx);
		}
		for (AbstractTag tag : items) {
			try {
				list.add(convertTag(tag, type));
			} catch (IncompatibleTagTypeException e) {
				throw new InternalParseException(e, ctx);
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
		try {
			switch (arrayTypeStr) {
			case "b":
				byte[] bytes = new byte[typedArrayItems.size()];
				for (int i = 0; i < typedArrayItems.size(); i++) {
					bytes[i] = Byte.parseByte(typedArrayItems.get(i));
				}
				array = new TagByteArray("", bytes);
				break;
			case "bx":
				byte[] hexBytes = new byte[typedArrayItems.size()];
				for (int i = 0; i < typedArrayItems.size(); i++) {
					hexBytes[i] = UnsignedBytes.parseUnsignedByte(typedArrayItems.get(i), 16);
				}
				array = new TagByteArray("", hexBytes);
			case "i":
				int[] ints = typedArrayItems.stream().mapToInt(o -> Integer.parseInt(o)).toArray();
				array = new TagIntArray("", ints);
				break;
			case "ix":
				int[] hexInts = typedArrayItems.stream().mapToInt(o -> Integer.parseUnsignedInt(o, 16)).toArray();
				array = new TagIntArray("", hexInts);
				break;
			case "l":
				long[] longs = typedArrayItems.stream().mapToLong(o -> Long.parseLong(o)).toArray();
				array = new TagLongArray("", longs);
				break;
			case "lx":
				long[] hexLongs = typedArrayItems.stream().mapToLong(o -> Long.parseUnsignedLong(o, 16)).toArray();
				array = new TagLongArray("", hexLongs);
				break;
			default:
				throw new InternalParseException("Unknown typed array type: " + arrayTypeStr, ctx);
			}
		} catch (NumberFormatException e) {
			throw new InternalParseException(e, ctx);
		}
		typedArrayItems.clear();
		tags.push(array);
	}

	@Override
	public void enterTypedArrayItem(TypedArrayItemContext ctx) {

	}

	@Override
	public void exitTypedArrayItem(TypedArrayItemContext ctx) {
		typedArrayItems.add(ctx.STRING().getText());
	}

	@Override
	public void enterTagCompound(TagCompoundContext ctx) {
		compoundItems.add(Lists.newArrayList());
	}

	@Override
	public void exitTagCompound(TagCompoundContext ctx) {
		tags.push(new TagCompound("", compoundItems.pop()));
	}

	@Override
	public void enterCompoundItem(CompoundItemContext ctx) {

	}

	@Override
	public void exitCompoundItem(CompoundItemContext ctx) {
		AbstractTag tag = tags.pop();
		tag.setName(unescapeString(ctx.STRING().getText()));
		compoundItems.peek().add(tag);
	}

	@Override
	public void visitTerminal(TerminalNode node) {

	}

	@Override
	public void visitErrorNode(ErrorNode node) {
		throw new InternalParseException("Error node", node);
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
		} else if (currentType == NBTValues.TAG_INT && STAGS.get(tagType)) {
			return tagType;
		} else if (tagType == NBTValues.TAG_INT && STAGS.get(currentType)) {
			return currentType;
		} else {
			throw new IncompatibleTagTypeException("List contains different types of tags: both "
					+ NBTUtils.getTagType(currentType) + " and " + NBTUtils.getTagType(tagType));
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
				throw new IncompatibleTagTypeException(
						"Unable to convert a TagInt into a tag of type: " + NBTUtils.getTagType(type));
			}
		} else {
			throw new IncompatibleTagTypeException("Unable to convert from a " + NBTUtils.getTagType(oldTag) + " to a "
					+ NBTUtils.getTagType(type) + "");
		}
	}

	private AbstractTag parseString(String str) throws TagParseException {
		Matcher match = NUMBER.matcher(str);
		if (str.startsWith("\"") && str.endsWith("\"")) {
			return new TagString("", str.substring(1, str.length() - 1).replace("\\\"", "\""));
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
						return new TagByte("", Byte.parseByte(str.substring(0, str.length() - 1)));
					case 's':
						return new TagShort("", Short.parseShort(str.substring(0, str.length() - 1)));
					case 'l':
						return new TagLong("", Long.parseLong(str.substring(0, str.length() - 1)));
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

	private String unescapeString(String input) {
		if (input.startsWith("\"") && input.endsWith("\"")) {
			input = input.substring(1, input.length() - 1).replace("\\\"", "\"");
		}
		return input;
	}
}
