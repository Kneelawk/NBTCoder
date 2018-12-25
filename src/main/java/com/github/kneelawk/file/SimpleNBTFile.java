package com.github.kneelawk.file;

import com.github.kneelawk.nbt.Tag;

public class SimpleNBTFile implements NBTFile {
	private Tag data;
	private boolean compressed;

	public SimpleNBTFile(Tag data, boolean compressed) {
		this.data = data;
		this.compressed = compressed;
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
