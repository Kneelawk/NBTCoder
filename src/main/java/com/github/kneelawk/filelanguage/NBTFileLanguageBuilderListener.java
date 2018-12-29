package com.github.kneelawk.filelanguage;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;
import java.util.Stack;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.github.kneelawk.filelanguage.NBTFileLanguageSystemParser.DataContext;
import com.github.kneelawk.filelanguage.NBTFileLanguageSystemParser.NbtFileContext;
import com.github.kneelawk.filelanguage.NBTFileLanguageSystemParser.PartitionContext;
import com.github.kneelawk.filelanguage.NBTFileLanguageSystemParser.PropertiesContext;
import com.github.kneelawk.nbt.Tag;
import com.github.kneelawk.nbtlanguage.NBTLanguageParser;

public class NBTFileLanguageBuilderListener extends NBTFileLanguageSystemBaseListener {

	private NBTLanguageParser nbtParser;

	private Stack<Properties> propertieses;
	private Tag tag;

	public NBTFileLanguageBuilderListener(NBTLanguageParser nbtParser) {
		this.nbtParser = nbtParser;
	}

	@Override
	public void exitNbtFile(NbtFileContext ctx) {
		// TODO Auto-generated method stub
		super.exitNbtFile(ctx);
	}

	@Override
	public void exitPartition(PartitionContext ctx) {
		// TODO Auto-generated method stub
		super.exitPartition(ctx);
	}

	@Override
	public void exitProperties(PropertiesContext ctx) {
		Properties props = new Properties();
		TerminalNode propsData = ctx.PROPERTIES_DATA();
		try {
			props.load(new StringReader(propsData.getText()));
		} catch (IOException e) {
			throw new InternalParseException(e, propsData);
		}
		propertieses.add(props);
	}

	@Override
	public void exitData(DataContext ctx) {
		TerminalNode nbtData = ctx.NBT_TAG();
		try {
			tag = nbtParser.parse(nbtData.getText());
		} catch (IOException e) {
			throw new InternalParseException(e, nbtData);
		}
	}

	@Override
	public void visitErrorNode(ErrorNode node) {
		throw new InternalParseException("Error Node", node);
	}

}
