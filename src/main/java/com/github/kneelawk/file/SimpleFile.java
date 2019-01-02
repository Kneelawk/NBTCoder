package com.github.kneelawk.file;

import com.github.kneelawk.nbt.Tag;

public interface SimpleFile extends NBTFile {
	public Tag getData();

	public boolean isCompressed();
}
