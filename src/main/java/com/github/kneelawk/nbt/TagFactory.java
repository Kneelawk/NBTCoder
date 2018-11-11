package com.github.kneelawk.nbt;

public interface TagFactory {
	public Tag createTag(byte type, String name);
}
