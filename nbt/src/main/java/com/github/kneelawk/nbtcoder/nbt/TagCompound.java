package com.github.kneelawk.nbtcoder.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import com.google.common.collect.Maps;

public class TagCompound extends AbstractTag {

	private Map<String, Tag> elements = Maps.newLinkedHashMap();

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

	public Map<String, Tag> getElements() {
		return elements;
	}

	@Override
	public void read(DataInput in, TagFactory factory) throws IOException {
		elements.clear();
		Tag tag;
		while ((tag = NBTInternalUtils.readNamedTag(in, factory)).getId() != NBTValues.TAG_END) {
			elements.put(tag.getName(), tag);
		}
	}

	@Override
	public void write(DataOutput out) throws IOException {
		for (Tag tag : elements.values()) {
			NBTInternalUtils.writeNamedTag(tag, out);
		}
		NBTInternalUtils.writeNamedTag(new TagEnd(), out);
	}

	@Override
	public byte getId() {
		return NBTValues.TAG_COMPOUND;
	}

	@Override
	public TagCompound copy() {
		TagCompound nmap = new TagCompound(getName());
		for (Tag tag : elements.values()) {
			nmap.put(tag.copy());
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

	public Tag put(Tag value) {
		return elements.put(value.getName(), value);
	}

	public Tag remove(String key) {
		return elements.remove(key);
	}

	public void putAll(Collection<? extends Tag> m) {
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
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((elements == null) ? 0 : elements.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		TagCompound other = (TagCompound) obj;
		if (elements == null) {
			if (other.elements != null)
				return false;
		} else if (!elements.equals(other.elements))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TagCompound(" + getName() + ", " + elements + ")";
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

	public Tag putIfAbsent(Tag value) {
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

	public void putByte(String name, byte value) {
		put(new TagByte(name, value));
	}

	public void putShort(String name, short value) {
		put(new TagShort(name, value));
	}

	public void putInt(String name, int value) {
		put(new TagInt(name, value));
	}

	public void putLong(String name, long value) {
		put(new TagLong(name, value));
	}

	public void putFloat(String name, float value) {
		put(new TagFloat(name, value));
	}

	public void putDouble(String name, double value) {
		put(new TagDouble(name, value));
	}

	public void putByteArray(String name, byte[] value) {
		put(new TagByteArray(name, value));
	}

	public void putString(String name, String value) {
		put(new TagString(name, value));
	}

	public void putIntArray(String name, int[] value) {
		put(new TagIntArray(name, value));
	}

	public void putLongArray(String name, long[] value) {
		put(new TagLongArray(name, value));
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
		}
		return (TagList<TagLongArray>) list;
	}
}
