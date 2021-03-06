package com.github.kneelawk.nbtcoder.file;

import com.github.kneelawk.nbtcoder.nbt.NBTIO;
import com.github.kneelawk.nbtcoder.nbt.Tag;

import java.io.IOException;
import java.io.OutputStream;

public class SimpleNBTFile implements NBTFile, SimpleFile {
	private String filename;
	private Tag data;
	private boolean compressed;

	public SimpleNBTFile(String filename, Tag data, boolean compressed) {
		this.filename = filename;
		this.data = data;
		this.compressed = compressed;
	}

	@Override
	public String getFilename() {
		return filename;
	}

	@Override
	public String getFileType() {
		return compressed ? NBTFileValues.COMPRESSED_FILE_TYPE_STRING : NBTFileValues.UNCOMPRESSED_FILE_TYPE_STRING;
	}

	@Override
	public void writeToStream(OutputStream os) throws IOException {
		if (compressed) {
			NBTIO.writeCompressedStream(data, os);
		} else {
			NBTIO.writeStream(data, os);
		}
	}

	@Override
	public Tag getData() {
		return data;
	}

	@Override
	public boolean isCompressed() {
		return compressed;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setData(Tag data) {
		this.data = data;
	}

	public void setCompressed(boolean compressed) {
		this.compressed = compressed;
	}
}
