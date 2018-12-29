package com.github.kneelawk.file;

import com.github.kneelawk.nbt.Tag;

public class SimpleNBTFile implements NBTFile {
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

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Tag getData() {
		return data;
	}

	public void setData(Tag data) {
		this.data = data;
	}

	public boolean isCompressed() {
		return compressed;
	}

	public void setCompressed(boolean compressed) {
		this.compressed = compressed;
	}
}
