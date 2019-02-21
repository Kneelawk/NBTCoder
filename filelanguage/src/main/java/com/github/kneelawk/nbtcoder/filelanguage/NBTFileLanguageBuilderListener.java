package com.github.kneelawk.nbtcoder.filelanguage;

import com.github.kneelawk.nbtcoder.file.NBTFile;
import com.github.kneelawk.nbtcoder.file.NBTFileValues;
import com.github.kneelawk.nbtcoder.file.RegionNBTFile;
import com.github.kneelawk.nbtcoder.file.SimpleNBTFile;
import com.github.kneelawk.nbtcoder.filelanguage.NBTFileLanguageSystemParser.*;
import com.github.kneelawk.nbtcoder.nbt.Tag;
import com.github.kneelawk.nbtcoder.nbtlanguage.NBTLanguageParser;
import com.github.kneelawk.nbtcoder.region.*;
import com.github.kneelawk.nbtcoder.utils.InternalParseException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.primitives.UnsignedBytes;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.regex.Matcher;

public class NBTFileLanguageBuilderListener extends NBTFileLanguageSystemBaseListener {

	private NBTLanguageParser nbtParser;

	private NBTFile file;
	private Deque<PropertiesConfiguration> propertieses = new ArrayDeque<>();
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
			// collect all the unused timestamp properties
			Map<ChunkLocation, Integer> unusedTimestamps = Maps.newHashMap();
			Iterator<String> keys = fileProps.getKeys();
			while (keys.hasNext()) {
				String key = keys.next();
				Matcher matcher = NBTFileLanguageValues.UNUSED_TIMESTAMP_KEY_PATTERN.matcher(key);
				if (matcher.matches()) {
					int x = Integer.parseInt(matcher.group("x"));
					int z = Integer.parseInt(matcher.group("z"));
					unusedTimestamps.put(new ChunkLocation(x, z), fileProps.getInt(key));
				}
			}

			file = new RegionNBTFile(filename, new SimpleRegionFile(partitions, unusedTimestamps));
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

			EmptyPartition part = new EmptyPartition(size, padding);

			padding = null;

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
		propertieses.push(props);
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
