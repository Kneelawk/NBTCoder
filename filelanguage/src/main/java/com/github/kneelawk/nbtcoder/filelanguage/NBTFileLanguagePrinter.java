package com.github.kneelawk.nbtcoder.filelanguage;

import com.github.kneelawk.nbtcoder.file.NBTFile;
import com.github.kneelawk.nbtcoder.file.PartitionedFile;
import com.github.kneelawk.nbtcoder.file.SimpleFile;
import com.github.kneelawk.nbtcoder.hexlanguage.HexLanguagePrinter;
import com.github.kneelawk.nbtcoder.nbt.Tag;
import com.github.kneelawk.nbtcoder.nbt.TagFactory;
import com.github.kneelawk.nbtcoder.nbtlanguage.NBTLanguagePrinter;
import com.github.kneelawk.nbtcoder.region.*;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.io.output.StringBuilderWriter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class NBTFileLanguagePrinter {
	private NBTLanguagePrinter nbtPrinter;
	private HexLanguagePrinter hexPrinter;

	public NBTFileLanguagePrinter(NBTLanguagePrinter nbtPrinter, HexLanguagePrinter hexPrinter) {
		this.nbtPrinter = nbtPrinter;
		this.hexPrinter = hexPrinter;
	}

	public String print(NBTFile file, TagFactory factory) throws IOException {
		StringBuilder sb = new StringBuilder();
		printNBTFile(file, factory, sb);
		return sb.toString();
	}

	public void print(NBTFile file, TagFactory factory, StringBuilder sb) throws IOException {
		printNBTFile(file, factory, sb);
	}

	private void printNBTFile(NBTFile file, TagFactory factory, StringBuilder sb) throws IOException {
		sb.append("(file\n");

		PropertiesConfiguration fileProps = new PropertiesConfiguration();
		fileProps.setProperty("name", file.getFilename());
		fileProps.setProperty("type", file.getFileType());

		if (file instanceof SimpleFile) {
			printProperties(fileProps, sb);

			sb.append('\n');

			SimpleFile sf = (SimpleFile) file;
			printData(sf.getData(), sb);
			sb.append('\n');
		} else if (file instanceof PartitionedFile) {
			PartitionedFile rf = (PartitionedFile) file;

			for (Map.Entry<ChunkLocation, Integer> entry : rf.getUnusedTimestamps().entrySet()) {
				ChunkLocation location = entry.getKey();
				fileProps.setProperty(String.format(NBTFileLanguageValues.UNUSED_TIMESTAMP_KEY_FORMAT, location.getX(), location.getZ()), entry.getValue());
			}

			printProperties(fileProps, sb);

			sb.append('\n');

			List<Partition> partitions = rf.getPartitions();

			for (Partition part : partitions) {
				printPartition(part, factory, sb);
				sb.append('\n');
			}
		} else {
			throw new IllegalArgumentException("Unknown NBTFile type: " + file.getClass());
		}

		sb.append(')');
	}

	private void printPartition(Partition part, TagFactory factory, StringBuilder sb) throws IOException {
		sb.append("(partition\n");

		PropertiesConfiguration partProps = new PropertiesConfiguration();
		partProps.setProperty("type", part.getPartitionType());

		if (part instanceof EmptyPartition) {
			partProps.setProperty("size", String.valueOf(part.getSectorCount()));
			printProperties(partProps, sb);

			if (part.hasPaddingData()) {
				sb.append('\n');

				printPadding(part.getPaddingData(), 0, sb);
			}
		} else if (part instanceof ChunkPartition) {
			ChunkPartition chunk = (ChunkPartition) part;

			partProps.setProperty("timestamp", String.valueOf(chunk.getTimestamp()));
			partProps.setProperty("compression", getCompression(chunk.getCompressionType()));
			partProps.setProperty("x", String.valueOf(chunk.getX()));
			partProps.setProperty("z", String.valueOf(chunk.getZ()));
			printProperties(partProps, sb);

			sb.append('\n');

			printData(chunk.readTag(factory), sb);

			if (chunk.hasPaddingData()) {
				sb.append('\n');

				printPadding(chunk.getPaddingData(), chunk.size(), sb);
			}
		} else {
			throw new IllegalArgumentException("Unknown Partition type: " + part.getClass());
		}

		sb.append("\n)");
	}

	private void printProperties(PropertiesConfiguration props, StringBuilder sb) throws IOException {
		sb.append("(properties\n");

		StringBuilderWriter writer = new StringBuilderWriter(sb);
		try {
			props.write(writer);
		} catch (ConfigurationException e) {
			throw new IOException(e);
		}

		sb.append(')');
	}

	private void printData(Tag data, StringBuilder sb) {
		sb.append("(data\n");

		nbtPrinter.print(data, sb);

		sb.append("\n)");
	}

	private void printPadding(byte[] data, int offset, StringBuilder sb) {
		sb.append("(padding\n");

		hexPrinter.print(data, offset, sb);

		sb.append("\n)");
	}

	private String getCompression(byte compression) {
		if (compression == RegionValues.GZIP_COMPRESSION) {
			return RegionValues.GZIP_COMPRESSION_STRING;
		} else if (compression == RegionValues.DEFLATE_COMPRESSION) {
			return RegionValues.DEFLATE_COMPRESSION_STRING;
		} else {
			throw new IllegalArgumentException("Unknown compression type: " + compression);
		}
	}
}
