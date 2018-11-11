package com.github.kneelawk.nbt;

public abstract class AbstractTag implements Tag {

	private String name;

	public AbstractTag() {
		name = "";
	}

	public AbstractTag(String name) {
		if (name == null) {
			this.name = "";
		} else {
			this.name = name;
		}
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name == null) {
			this.name = "";
		} else {
			this.name = name;
		}
	}
}
