package com.github.kneelawk.nbtcoder.nbt;

import java.io.IOException;

public interface TagFactory {
	Tag createTag(byte type, String name) throws IOException;
}
