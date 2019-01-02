package com.github.kneelawk.filelanguage;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Stack;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;

import com.github.kneelawk.file.NBTFile;
import com.github.kneelawk.file.NBTFileValues;
import com.github.kneelawk.file.RegionNBTFile;
import com.github.kneelawk.file.SimpleNBTFile;
import com.github.kneelawk.filelanguage.NBTFileLanguageSystemParser.DataContext;
import com.github.kneelawk.filelanguage.NBTFileLanguageSystemParser.NbtFileContext;
import com.github.kneelawk.filelanguage.NBTFileLanguageSystemParser.PaddingContext;
import com.github.kneelawk.filelanguage.NBTFileLanguageSystemParser.PartitionContext;
import com.github.kneelawk.filelanguage.NBTFileLanguageSystemParser.PropertiesContext;
import com.github.kneelawk.nbt.Tag;
import com.github.kneelawk.nbtlanguage.NBTLanguageParser;
import com.github.kneelawk.region.Chunk;
import com.github.kneelawk.region.EmptyPartition;
import com.github.kneelawk.region.Partition;
import com.github.kneelawk.region.RegionValues;
import com.github.kneelawk.utils.InternalParseException;
import com.google.common.collect.Lists;
import com.google.common.primitives.UnsignedBytes;

public class NBTFileLanguageBuilderListener extends NBTFileLanguageSystemBaseListener {

	private NBTLanguageParser nbtParser;

	private NBTFile file;
	private Stack<PropertiesConfiguration> propertieses = new Stack<>();
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
		PropertiesConfiguration fileProps = propertieses.pop();
		String filename = fileProps.getString("name");
		String fileType = fileProps.getString("type");

		switch (fileType) {
		case NBTFileValues.REGION_FILE_TYPE_STRING:
			file = new RegionNBTFile(filename, partitions);
			break;
		case NBTFileValues.UNCOMPRESSED_FILE_TYPE_STRING:
		case NBTFileValues.COMPRESSED_FILE_TYPE_STRING:
			file = new SimpleNBTFile(filename, tag, NBTFileValues.COMPRESSED_FILE_TYPE_STRING.equals(fileType));
			break;
		default:
			throw new InternalParseException("Invalid NBT file type: " + fileType, ctx.properties());
		}
	}

	@Override
	public void exitPartition(PartitionContext ctx) {
		PropertiesConfiguration partProps = propertieses.pop();
		String partType = partProps.getString("type");
		PropertiesContext propsContext = ctx.properties();
		if (RegionValues.CHUNK_PARTITION_TYPE_STRING.equals(partType)) {
			byte compression;
			String compressionStr = partProps.getString("compression");
			if (RegionValues.DEFLATE_COMPRESSION_STRING.equals(compressionStr)) {
				compression = RegionValues.DEFLATE_COMPRESSION;
			} else if (RegionValues.GZIP_COMPRESSION_STRING.equals(compressionStr)) {
				compression = RegionValues.GZIP_COMPRESSION;
			} else {
				throw new InternalParseException("Invalid compression type: " + compressionStr, propsContext);
			}

			int timestamp = parseIntProperty(partProps, "timestamp", propsContext);
			int x = parseIntProperty(partProps, "x", propsContext);
			int z = parseIntProperty(partProps, "z", propsContext);

			Chunk part = new Chunk.Builder().setCompressionType(compression).setTimestamp(timestamp).setX(x).setZ(z)
					.setPaddingData(padding).build();
			try {
				part.writeTag(tag);
			} catch (IOException e) {
				throw new InternalParseException("Error writing tag", ctx.data());
			}

			tag = null;
			padding = null;

			partitions.add(part);
		} else if (RegionValues.EMPTY_PARTITION_TYPE_STRING.equals(partType)) {
			int size = parseIntProperty(partProps, "size", propsContext);

			EmptyPartition part = new EmptyPartition(size);

			partitions.add(part);
		} else {
			throw new InternalParseException("Invalid partition type: " + partType, propsContext);
		}
	}

	@Override
	public void exitProperties(PropertiesContext ctx) {
		PropertiesConfiguration props = new PropertiesConfiguration();
		props.setIncludesAllowed(false);
		TerminalNode propsData = ctx.PROPERTIES_DATA();
		try {
			props.read(new StringReader(propsData.getText()));
		} catch (IOException e) {
			throw new InternalParseException(e, propsData);
		} catch (ConfigurationException e) {
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

	private int parseIntProperty(PropertiesConfiguration props, String key, ParseTree tree) {
		try {
			return props.getInt(key);
		} catch (Exception e) {
			throw new InternalParseException(e, tree);
		}
	}
}
