package com.github.kneelawk.filelanguage;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Properties;
import java.util.Stack;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.github.kneelawk.file.NBTFile;
import com.github.kneelawk.file.RegionNBTFile;
import com.github.kneelawk.file.SimpleNBTFile;
import com.github.kneelawk.filelanguage.NBTFileLanguageSystemParser.DataContext;
import com.github.kneelawk.filelanguage.NBTFileLanguageSystemParser.NbtFileContext;
import com.github.kneelawk.filelanguage.NBTFileLanguageSystemParser.PaddingContext;
import com.github.kneelawk.filelanguage.NBTFileLanguageSystemParser.PartitionContext;
import com.github.kneelawk.filelanguage.NBTFileLanguageSystemParser.PropertiesContext;
import com.github.kneelawk.nbt.Tag;
import com.github.kneelawk.nbtlanguage.NBTLanguageParser;
import com.github.kneelawk.region.ChunkPartition;
import com.github.kneelawk.region.EmptyPartition;
import com.github.kneelawk.region.Partition;
import com.github.kneelawk.region.RegionValues;
import com.github.kneelawk.utils.InternalParseException;
import com.google.common.collect.Lists;
import com.google.common.primitives.UnsignedBytes;

public class NBTFileLanguageBuilderListener extends NBTFileLanguageSystemBaseListener {

	private NBTLanguageParser nbtParser;

	private NBTFile file;
	private Stack<Properties> propertieses = new Stack<>();
	private List<Partition> partitions = Lists.newArrayList();
	private Tag tag;
	private byte[] padding;

	public NBTFileLanguageBuilderListener(NBTLanguageParser nbtParser) {
		this.nbtParser = nbtParser;
	}

	public NBTFile getFile() {
		return file;
	}

	@Override
	public void exitNbtFile(NbtFileContext ctx) {
		Properties fileProps = propertieses.pop();
		String filename = fileProps.getProperty("name");
		String fileType = fileProps.getProperty("type");

		switch (fileType) {
		case "region":
			file = new RegionNBTFile(filename, partitions);
			break;
		case "uncompressed":
		case "compressed":
			file = new SimpleNBTFile(filename, tag, "compressed".equals(fileType));
			break;
		default:
			throw new InternalParseException("Invalid NBT file type: " + fileType, ctx.properties());
		}
	}

	@Override
	public void exitPartition(PartitionContext ctx) {
		Properties partProps = propertieses.pop();
		String partType = partProps.getProperty("type");
		PropertiesContext propsContext = ctx.properties();
		if ("chunk".equals(partType)) {
			byte compression;
			String compressionStr = partProps.getProperty("compression");
			if ("deflate".equals(compressionStr)) {
				compression = RegionValues.DEFLATE_COMPRESSION;
			} else if ("gzip".equals(compressionStr)) {
				compression = RegionValues.GZIP_COMPRESSION;
			} else {
				throw new InternalParseException("Invalid compression type: " + compressionStr, propsContext);
			}

			int timestamp = parseIntProperty(partProps, "timestamp", propsContext);
			int x = parseIntProperty(partProps, "x", propsContext);
			int z = parseIntProperty(partProps, "z", propsContext);

			ChunkPartition part = new ChunkPartition.Builder().setCompressionType(compression).setTimestamp(timestamp)
					.setX(x).setZ(z).setPaddingData(padding).build();
			try {
				part.writeTag(tag);
			} catch (IOException e) {
				throw new InternalParseException("Error writing tag", ctx.data());
			}

			tag = null;
			padding = null;

			partitions.add(part);
		} else if ("empty".equals(partType)) {
			int size = parseIntProperty(partProps, "size", propsContext);

			EmptyPartition part = new EmptyPartition(size);

			partitions.add(part);
		} else {
			throw new InternalParseException("Invalid partition type: " + partType, propsContext);
		}
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
	public void exitPadding(PaddingContext ctx) {
		List<TerminalNode> paddingBytes = ctx.PADDING_BYTE();
		int paddingLen = paddingBytes.size();
		padding = new byte[paddingLen];

		for (int i = 0; i < paddingLen; i++) {
			TerminalNode paddingByte = paddingBytes.get(i);
			try {
				padding[i] = UnsignedBytes.parseUnsignedByte(paddingByte.getText(), 16);
			} catch (NumberFormatException e) {
				throw new InternalParseException(e, paddingByte);
			}
		}
	}

	@Override
	public void visitErrorNode(ErrorNode node) {
		throw new InternalParseException("Error Node", node);
	}

	private int parseIntProperty(Properties props, String key, ParseTree tree) {
		String prop = props.getProperty(key);
		try {
			return Integer.parseInt(prop);
		} catch (Exception e) {
			throw new InternalParseException("Unable to parse property: " + key + " with a value of: " + prop, e, tree);
		}
	}
}
