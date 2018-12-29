package com.github.kneelawk.filelanguage;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.github.kneelawk.file.NBTFile;
import com.github.kneelawk.file.RegionNBTFile;
import com.github.kneelawk.file.SimpleNBTFile;
import com.github.kneelawk.filelanguage.NBTFileLexer.DataStartToken;
import com.github.kneelawk.filelanguage.NBTFileLexer.FileStartToken;
import com.github.kneelawk.filelanguage.NBTFileLexer.ParenthesisCloseToken;
import com.github.kneelawk.filelanguage.NBTFileLexer.PartitionStartToken;
import com.github.kneelawk.filelanguage.NBTFileLexer.PropertiesStartToken;
import com.github.kneelawk.nbt.Tag;
import com.github.kneelawk.nbtlanguage.NBTLanguageParser;
import com.github.kneelawk.region.ChunkPartition;
import com.github.kneelawk.region.EmptyPartition;
import com.github.kneelawk.region.Partition;
import com.github.kneelawk.region.RegionValues;

public class NBTFileParser {
	private NBTFileLexer lexer;
	private NBTLanguageParser nbtParser;

	public NBTFileParser(NBTFileLexer lexer, NBTLanguageParser nbtParser) {
		this.lexer = lexer;
		this.nbtParser = nbtParser;
	}

	public NBTFile parseNBTFile() throws IOException {
		FileStartToken fileStartTok = lexer.readFileStart();
		if (!fileStartTok.isFileStart()) {
			throw new IOException("Expected file start ('(file') but found: " + fileStartTok.getString());
		}

		Properties fileProps = parseProperties();
		String filename = fileProps.getProperty("name");
		String fileType = fileProps.getProperty("type");

		switch (fileType) {
		case "region": {
			List<Partition> partitions = new ArrayList<>();
			Partition part;
			while ((part = parsePartition()) != null) {
				partitions.add(part);
			}

			RegionNBTFile file = new RegionNBTFile(filename, partitions);

			ParenthesisCloseToken parenCloseTok = lexer.readParenthesisClose();
			if (!parenCloseTok.isParenthesisClose()) {
				throw new IOException("Expected parenthesis close (')') but found: " + parenCloseTok.getString());
			}

			return file;
		}
		case "uncompressed":
		case "compressed": {
			Tag data = parseData();

			SimpleNBTFile file = new SimpleNBTFile(filename, data, fileType.equals("compressed"));

			ParenthesisCloseToken parenCloseTok = lexer.readParenthesisClose();
			if (!parenCloseTok.isParenthesisClose()) {
				throw new IOException("Expected parenthesis close (')') but found: " + parenCloseTok.getString());
			}

			return file;
		}
		default:
			throw new IOException("Unknown file type: " + fileType);
		}
	}

	public Partition parsePartition() throws IOException {
		PartitionStartToken partStartTok = lexer.readPartitionStart();
		if (!partStartTok.isPartitionStart()) {
			if (partStartTok.isParenthesisClose()) {
				return null;
			} else {
				throw new IOException("Expected partition start ('(partition') or parenthesis close (')') but found: "
						+ partStartTok.getString());
			}
		}
		Properties partProps = parseProperties();
		String partType = partProps.getProperty("type");

		if (partType.equals("chunk")) {
			byte compression;
			String compressionProp = partProps.getProperty("compression");
			if (compressionProp.equals("deflate")) {
				compression = RegionValues.DEFLATE_COMPRESSION;
			} else if (compressionProp.equals("gzip")) {
				compression = RegionValues.GZIP_COMPRESSION;
			} else {
				throw new IOException("Unknown compression type: " + compressionProp);
			}

			int x, z, timestamp;
			try {
				x = Integer.parseInt(partProps.getProperty("x"));
				z = Integer.parseInt(partProps.getProperty("z"));
				timestamp = Integer.parseInt(partProps.getProperty("timestamp"));
			} catch (NumberFormatException e) {
				throw new IOException(e);
			}

			Tag data = parseData();

			ChunkPartition chunk = new ChunkPartition.Builder().setCompressionType(compression).setTimestamp(timestamp)
					.setX(x).setZ(z).build();
			chunk.writeTag(data);

			ParenthesisCloseToken parenCloseTok = lexer.readParenthesisClose();
			if (!parenCloseTok.isParenthesisClose()) {
				throw new IOException("Expected parenthesis close (')') but found: " + parenCloseTok.getString());
			}

			return chunk;
		} else if (partType.equals("empty")) {
			int size;
			try {
				size = Integer.parseInt(partProps.getProperty("size"));
			} catch (NumberFormatException e) {
				throw new IOException(e);
			}

			ParenthesisCloseToken parenCloseTok = lexer.readParenthesisClose();
			if (!parenCloseTok.isParenthesisClose()) {
				throw new IOException("Expected parenthesis close (')') but found: " + parenCloseTok.getString());
			}

			return new EmptyPartition(size);
		} else {
			throw new IOException("Unknown partition type: " + partType);
		}
	}

	public Properties parseProperties() throws IOException {
		PropertiesStartToken propsStartTok = lexer.readPropertiesStart();
		if (!propsStartTok.isPropertiesStart()) {
			throw new IOException("Expected properties start ('(properties') but found: " + propsStartTok.getString());
		}

		StringBuilder propertiesSource = new StringBuilder();
		ParenthesisCloseToken token;
		while (!(token = lexer.readParenthesisClose()).isParenthesisClose()) {
			propertiesSource.append(token.getString());
			propertiesSource.append("\n");
		}

		Properties props = new Properties();
		props.load(new StringReader(propertiesSource.toString()));

		return props;
	}

	public Tag parseData() throws IOException {
		DataStartToken dataStartTok = lexer.readDataStart();
		if (!dataStartTok.isDataStart()) {
			throw new IOException("Expected data start ('(data') but found: " + dataStartTok.getString());
		}

		Tag tag = nbtParser.parse(lexer.getReader());

		ParenthesisCloseToken parenCloseTok = lexer.readParenthesisClose();
		if (!parenCloseTok.isParenthesisClose()) {
			throw new IOException("Expected parenthesis close (')') but found: " + parenCloseTok.getString());
		}

		return tag;
	}
}
