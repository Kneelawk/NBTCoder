package com.github.kneelawk.nbtcoder.nbt;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractTag other = (AbstractTag) obj;
		if (name == null) {
			return other.name == null;
		} else return name.equals(other.name);
	}
}
