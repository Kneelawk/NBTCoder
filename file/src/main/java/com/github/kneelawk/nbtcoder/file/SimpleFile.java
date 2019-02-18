package com.github.kneelawk.nbtcoder.file;

import com.github.kneelawk.nbtcoder.nbt.Tag;

public interface SimpleFile extends NBTFile {
	Tag getData();

	boolean isCompressed();
}
