package com.github.kneelawk.filelanguage;

import java.io.IOException;
import java.util.List;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.io.output.StringBuilderWriter;

import com.github.kneelawk.file.NBTFile;
import com.github.kneelawk.file.RegionFile;
import com.github.kneelawk.file.SimpleFile;
import com.github.kneelawk.hexlanguage.HexLanguagePrinter;
import com.github.kneelawk.nbt.Tag;
import com.github.kneelawk.nbt.TagFactory;
import com.github.kneelawk.nbtlanguage.NBTLanguagePrinter;
import com.github.kneelawk.region.ChunkPartition;
import com.github.kneelawk.region.EmptyPartition;
import com.github.kneelawk.region.Partition;
import com.github.kneelawk.region.RegionValues;

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
		printProperties(fileProps, sb);

		sb.append('\n');

		if (file instanceof SimpleFile) {
			SimpleFile sf = (SimpleFile) file;
			printData(sf.getData(), sb);
			sb.append('\n');
		} else if (file instanceof RegionFile) {
			RegionFile rf = (RegionFile) file;
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
