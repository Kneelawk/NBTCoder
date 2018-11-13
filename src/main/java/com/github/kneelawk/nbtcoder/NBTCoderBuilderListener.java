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
import com.github.kneelawk.nbt.TagDouble;
import com.github.kneelawk.nbt.TagFloat;
import com.github.kneelawk.nbt.TagInt;
import com.github.kneelawk.nbt.TagList;
import com.github.kneelawk.nbt.TagLong;
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
		STAGS.set(NBTValues.TAG_STRING);
	}

	private AbstractTag root;
	private Stack<AbstractTag> tags = new Stack<>();
	private List<AbstractTag> listItems = new ArrayList<>();

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
		AbstractTag tag = parseString(str);
		tags.push(tag);
	}

	@Override
	public void enterTagList(TagListContext ctx) {
		listItems.clear();
	}

	@Override
	public void exitTagList(TagListContext ctx) {
		TagList<Tag> list = new TagList<>();
		byte type = NBTValues.TAG_INT;
		if (!listItems.isEmpty())
			type = listItems.get(0).getId();
		for (Tag tag : listItems) {
			if (tag.getId() != type) {
				throw new RuntimeException("This list conatins mismatched types");
			}
		}
	}

	@Override
	public void enterListItem(ListItemContext ctx) {

	}

	@Override
	public void exitListItem(ListItemContext ctx) {
		listItems.add(tags.pop());
	}

	@Override
	public void enterTagTypedArray(TagTypedArrayContext ctx) {

	}

	@Override
	public void exitTagTypedArray(TagTypedArrayContext ctx) {

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

	private AbstractTag parseString(String str) {
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
						throw new RuntimeException(
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
						throw new RuntimeException("Strange integer number suffix: " + type + ", number: " + str);
					}
				}
			}
		} else {
			return new TagString("", str);
		}
	}
}
