package com.github.kneelawk.nbt;

import java.io.IOException;

public interface TagFactory {
	public Tag createTag(byte type, String name) throws IOException;
}
