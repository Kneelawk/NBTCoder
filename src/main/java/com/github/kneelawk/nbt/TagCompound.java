package com.github.kneelawk.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class TagCompound extends AbstractTag {

	private Map<String, Tag> elements = new HashMap<>();

	public TagCompound() {
	}

	public TagCompound(String name) {
		super(name);
	}

	public TagCompound(String name, Collection<? extends Tag> elements) {
		super(name);
		for (Tag tag : elements) {
			this.elements.put(tag.getName(), tag);
		}
	}

	@Override
	public void read(DataInput in, TagFactory factory) throws IOException {
		elements.clear();
		Tag tag;
		while ((tag = NBTUtils.readNamedTag(in, factory)).getId() != NBTValues.TAG_END) {
			elements.put(tag.getName(), tag);
		}
	}

	@Override
	public void write(DataOutput out) throws IOException {
		for (Tag tag : elements.values()) {
			NBTUtils.writeNamedTag(tag, out);
		}
		NBTUtils.writeNamedTag(new TagEnd(), out);
	}

	@Override
	public byte getId() {
		return NBTValues.TAG_COMPOUND;
	}

	@Override
	public TagCompound copy() {
		TagCompound nmap = new TagCompound(getName());
		for (Tag tag : elements.values()) {
			nmap.add(tag.copy());
		}
		return nmap;
	}

	public int size() {
		return elements.size();
	}

	public boolean isEmpty() {
		return elements.isEmpty();
	}

	public boolean containsName(String key) {
		return elements.containsKey(key);
	}

	public boolean containsTag(Tag value) {
		return elements.containsValue(value);
	}

	public Tag get(String key) {
		return elements.get(key);
	}

	public Tag add(Tag value) {
		return elements.put(value.getName(), value);
	}

	public Tag remove(String key) {
		return elements.remove(key);
	}

	public void addAll(Collection<? extends Tag> m) {
		for (Tag tag : m) {
			elements.put(tag.getName(), tag);
		}
	}

	public void clear() {
		elements.clear();
	}

	public Set<String> nameSet() {
		return elements.keySet();
	}

	public Collection<Tag> tags() {
		return elements.values();
	}

	@Override
	public boolean equals(Object o) {
		return elements.equals(o);
	}

	@Override
	public int hashCode() {
		return elements.hashCode();
	}

	public Tag getOrDefault(String key, Tag defaultValue) {
		return elements.getOrDefault(key, defaultValue);
	}

	public void forEach(Consumer<? super Tag> action) {
		elements.forEach((name, tag) -> action.accept(tag));
	}

	public void replaceAll(UnaryOperator<Tag> function) {
		elements.replaceAll((name, tag) -> function.apply(tag));
	}

	public Tag addIfAbsent(Tag value) {
		return elements.putIfAbsent(value.getName(), value);
	}

	public boolean remove(Tag value) {
		return elements.remove(value.getName(), value);
	}

	public Tag replace(String key, Tag value) {
		Tag oldTag = elements.remove(key);
		elements.put(value.getName(), value);
		return oldTag;
	}

	public Tag computeIfAbsent(String key, Function<? super String, ? extends Tag> mappingFunction) {
		return elements.computeIfAbsent(key, mappingFunction);
	}

	public Tag computeIfPresent(String key, UnaryOperator<Tag> remappingFunction) {
		return elements.computeIfPresent(key, (name, tag) -> {
			Tag ntag = remappingFunction.apply(tag);
			if (ntag != null && !ntag.getName().equals(key)) {
				throw new IllegalArgumentException(
						"The new tag's name does not match the name of the tag it is replacing");
			}
			return ntag;
		});
	}

	public Tag compute(String key, UnaryOperator<Tag> remappingFunction) {
		return elements.compute(key, (name, tag) -> {
			Tag ntag = remappingFunction.apply(tag);
			if (ntag != null && !ntag.getName().equals(key)) {
				throw new IllegalArgumentException(
						"The new tag's name does not match the name of the tag it is replacing");
			}
			return ntag;
		});
	}

	public Tag merge(String key, Tag value, BiFunction<? super Tag, ? super Tag, ? extends Tag> remappingFunction) {
		return elements.merge(key, value, (tag1, tag2) -> {
			Tag ntag = remappingFunction.apply(tag1, tag2);
			if (ntag != null && !ntag.getName().equals(key)) {
				throw new IllegalArgumentException(
						"The new tag's name does not match the name of the tag it is replacing");
			}
			return ntag;
		});
	}

	public void addByte(String name, byte value) {
		add(new TagByte(name, value));
	}

	public void addShort(String name, short value) {
		add(new TagShort(name, value));
	}

	public void addInt(String name, int value) {
		add(new TagInt(name, value));
	}

	public void addLong(String name, long value) {
		add(new TagLong(name, value));
	}

	public void addFloat(String name, float value) {
		add(new TagFloat(name, value));
	}

	public void addDouble(String name, double value) {
		add(new TagDouble(name, value));
	}

	public void addByteArray(String name, byte[] value) {
		add(new TagByteArray(name, value));
	}

	public void addString(String name, String value) {
		add(new TagString(name, value));
	}

	public void addIntArray(String name, int[] value) {
		add(new TagIntArray(name, value));
	}

	public void addLongArray(String name, long[] value) {
		add(new TagLongArray(name, value));
	}

	public byte getByte(String key) {
		return ((TagByte) elements.getOrDefault(key, new TagByte())).getValue();
	}

	public short getShort(String key) {
		return ((TagShort) elements.getOrDefault(key, new TagShort())).getValue();
	}

	public int getInt(String key) {
		return ((TagInt) elements.getOrDefault(key, new TagInt())).getValue();
	}

	public long getLong(String key) {
		return ((TagLong) elements.getOrDefault(key, new TagLong())).getValue();
	}

	public float getFloat(String key) {
		return ((TagFloat) elements.getOrDefault(key, new TagFloat())).getValue();
	}

	public double getDouble(String key) {
		return ((TagDouble) elements.getOrDefault(key, new TagDouble())).getValue();
	}

	public byte[] getByteArray(String key) {
		return ((TagByteArray) elements.getOrDefault(key, new TagByteArray())).getValue();
	}

	public String getString(String key) {
		return ((TagString) elements.getOrDefault(key, new TagString())).getValue();
	}

	@SuppressWarnings("unchecked")
	public TagList<? extends Tag> getList(String key) {
		return ((TagList<? extends Tag>) elements.getOrDefault(key, new TagList<Tag>()));
	}

	public TagCompound getCompound(String key) {
		return ((TagCompound) elements.getOrDefault(key, new TagCompound()));
	}

	public int[] getIntArray(String key) {
		return ((TagIntArray) elements.getOrDefault(key, new TagIntArray())).getValue();
	}

	public long[] getLongArray(String key) {
		return ((TagLongArray) elements.getOrDefault(key, new TagLongArray())).getValue();
	}

	public boolean getBoolean(String key) {
		return getByte(key) != 0;
	}

	@SuppressWarnings("unchecked")
	public TagList<TagByte> getByteList(String key) {
		TagList<? extends Tag> list = getList(key);
		if (list.size() != 0) {
			if (list.get(0).getId() != NBTValues.TAG_BYTE) {
				throw new ClassCastException("This TagList does not contain bytes");
			}
		}
		return (TagList<TagByte>) list;
	}

	@SuppressWarnings("unchecked")
	public TagList<TagShort> getShortList(String key) {
		TagList<? extends Tag> list = getList(key);
		if (list.size() != 0) {
			if (list.get(0).getId() != NBTValues.TAG_SHORT) {
				throw new ClassCastException("This TagList does not contain shorts");
			}
		} else {
			throw new ClassCastException("This TagList does not contain shorts");
		}
		return (TagList<TagShort>) list;
	}

	@SuppressWarnings("unchecked")
	public TagList<TagInt> getIntList(String key) {
		TagList<? extends Tag> list = getList(key);
		if (list.size() != 0) {
			if (list.get(0).getId() != NBTValues.TAG_INT) {
				throw new ClassCastException("This TagList does not contain ints");
			}
		} else {
			throw new ClassCastException("This TagList does not contain ints");
		}
		return (TagList<TagInt>) list;
	}

	@SuppressWarnings("unchecked")
	public TagList<TagLong> getLongList(String key) {
		TagList<? extends Tag> list = getList(key);
		if (list.size() != 0) {
			if (list.get(0).getId() != NBTValues.TAG_LONG) {
				throw new ClassCastException("This TagList does not contain longs");
			}
		} else {
			throw new ClassCastException("This TagList does not contain longs");
		}
		return (TagList<TagLong>) list;
	}

	@SuppressWarnings("unchecked")
	public TagList<TagFloat> getFloatList(String key) {
		TagList<? extends Tag> list = getList(key);
		if (list.size() != 0) {
			if (list.get(0).getId() != NBTValues.TAG_FLOAT) {
				throw new ClassCastException("This TagList does not contain floats");
			}
		} else {
			throw new ClassCastException("This TagList does not contain floats");
		}
		return (TagList<TagFloat>) list;
	}

	@SuppressWarnings("unchecked")
	public TagList<TagDouble> getDoubleList(String key) {
		TagList<? extends Tag> list = getList(key);
		if (list.size() != 0) {
			if (list.get(0).getId() != NBTValues.TAG_DOUBLE) {
				throw new ClassCastException("This TagList does not contain dobules");
			}
		} else {
			throw new ClassCastException("This TagList does not contain doubles");
		}
		return (TagList<TagDouble>) list;
	}

	@SuppressWarnings("unchecked")
	public TagList<TagByteArray> getByteArrayList(String key) {
		TagList<? extends Tag> list = getList(key);
		if (list.size() != 0) {
			if (list.get(0).getId() != NBTValues.TAG_BYTE_ARRAY) {
				throw new ClassCastException("This TagList does not contain byte arrays");
			}
		} else {
			throw new ClassCastException("This TagList does not contain byte arrays");
		}
		return (TagList<TagByteArray>) list;
	}

	@SuppressWarnings("unchecked")
	public TagList<TagString> getStringList(String key) {
		TagList<? extends Tag> list = getList(key);
		if (list.size() != 0) {
			if (list.get(0).getId() != NBTValues.TAG_STRING) {
				throw new ClassCastException("This TagList does not contain strings");
			}
		} else {
			throw new ClassCastException("This TagList does not contain strings");
		}
		return (TagList<TagString>) list;
	}

	@SuppressWarnings("unchecked")
	public TagList<TagList<? extends Tag>> getListList(String key) {
		TagList<? extends Tag> list = getList(key);
		if (list.size() != 0) {
			if (list.get(0).getId() != NBTValues.TAG_LIST) {
				throw new ClassCastException("This TagList does not contain lists");
			}
		} else {
			throw new ClassCastException("This TagList does not contain lists");
		}
		return (TagList<TagList<? extends Tag>>) list;
	}

	@SuppressWarnings("unchecked")
	public TagList<TagCompound> getCompoundList(String key) {
		TagList<? extends Tag> list = getList(key);
		if (list.size() != 0) {
			if (list.get(0).getId() != NBTValues.TAG_COMPOUND) {
				throw new ClassCastException("This TagList does not contain compound tags");
			}
		} else {
			throw new ClassCastException("This TagList does not contain compound tags");
		}
		return (TagList<TagCompound>) list;
	}

	@SuppressWarnings("unchecked")
	public TagList<TagIntArray> getIntArrayList(String key) {
		TagList<? extends Tag> list = getList(key);
		if (list.size() != 0) {
			if (list.get(0).getId() != NBTValues.TAG_INT_ARRAY) {
				throw new ClassCastException("This TagList does not contain int arrays");
			}
		} else {
			throw new ClassCastException("This TagList does not contain int arrays");
		}
		return (TagList<TagIntArray>) list;
	}

	@SuppressWarnings("unchecked")
	public TagList<TagLongArray> getLongArrayList(String key) {
		TagList<? extends Tag> list = getList(key);
		if (list.size() != 0) {
			if (list.get(0).getId() != NBTValues.TAG_LONG_ARRAY) {
				throw new ClassCastException("This TagList does not contain long arrays");
			}
		} else {
			throw new ClassCastException("This TagList does not contain long arrays");
		}
		return (TagList<TagLongArray>) list;
	}
}
