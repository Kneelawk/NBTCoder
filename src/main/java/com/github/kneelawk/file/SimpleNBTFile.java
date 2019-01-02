package com.github.kneelawk.file;

import com.github.kneelawk.nbt.Tag;

public class SimpleNBTFile implements NBTFile, SimpleFile {
	public static final String COMPRESSED_FILE_TYPE_STRING = "compressed";
	public static final String UNCOMPRESSED_FILE_TYPE_STRING = "uncompressed";

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
		return compressed ? COMPRESSED_FILE_TYPE_STRING : UNCOMPRESSED_FILE_TYPE_STRING;
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
