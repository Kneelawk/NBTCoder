package com.github.kneelawk.nbtcoder.nbt;

import java.io.IOException;

public interface TagFactory {
	public Tag createTag(byte type, String name) throws IOException;
}
