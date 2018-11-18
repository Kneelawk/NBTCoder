package com.github.kneelawk.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class TagList<E extends Tag> extends AbstractTag implements List<E> {

	private List<E> elements = new ArrayList<>();

	public TagList() {
	}

	public TagList(String name) {
		super(name);
	}

	public TagList(String name, Collection<? extends E> value) {
		super(name);
		checkTagTypes(value);
		this.elements.addAll(value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void read(DataInput in, TagFactory factory) throws IOException {
		byte elementType = in.readByte();
		int size = in.readInt();

		elements.clear();
		for (int i = 0; i < size; i++) {
			Tag tag = factory.createTag(elementType, null);
			tag.read(in, factory);
			elements.add((E) tag);
		}
	}

	@Override
	public void write(DataOutput out) throws IOException {
		byte elementType;
		if (elements.size() == 0) {
			elementType = 1;
		} else {
			elementType = elements.get(0).getId();
		}

		out.writeByte(elementType);
		out.writeInt(elements.size());
		for (Tag tag : elements) {
			tag.write(out);
		}
	}

	@Override
	public byte getId() {
		return NBTValues.TAG_LIST;
	}

	@SuppressWarnings("unchecked")
	@Override
	public TagList<E> copy() {
		TagList<E> nlist = new TagList<>(getName());
		for (Tag tag : elements) {
			E ntag = (E) tag.copy();
			nlist.add(ntag);
		}
		return nlist;
	}

	@Override
	public String toString() {
		return "TagList(" + getName() + ", " + elements + ")";
	}

	@Override
	public void forEach(Consumer<? super E> action) {
		elements.forEach(action);
	}

	@Override
	public int size() {
		return elements.size();
	}

	@Override
	public boolean isEmpty() {
		return elements.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return elements.contains(o);
	}

	@Override
	public Iterator<E> iterator() {
		return elements.iterator();
	}

	@Override
	public Object[] toArray() {
		return elements.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return elements.toArray(a);
	}

	@Override
	public boolean add(E e) {
		checkNewTagType(e);
		return elements.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return elements.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return elements.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		checkNewTagTypes(c);
		return elements.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		checkNewTagTypes(c);
		return elements.addAll(index, c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return elements.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return elements.retainAll(c);
	}

	@Override
	public void replaceAll(UnaryOperator<E> operator) {
		List<E> nelements = new ArrayList<>(elements);
		nelements.replaceAll(operator);
		checkTagTypes(nelements);
		elements.clear();
		elements.addAll(nelements);
	}

	@Override
	public boolean removeIf(Predicate<? super E> filter) {
		return elements.removeIf(filter);
	}

	@Override
	public void sort(Comparator<? super E> c) {
		elements.sort(c);
	}

	@Override
	public void clear() {
		elements.clear();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("rawtypes")
		TagList other = (TagList) obj;
		if (elements == null) {
			if (other.elements != null)
				return false;
		} else if (!elements.equals(other.elements))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((elements == null) ? 0 : elements.hashCode());
		return result;
	}

	@Override
	public E get(int index) {
		return elements.get(index);
	}

	@Override
	public E set(int index, E element) {
		checkNewTagType(element);
		return elements.set(index, element);
	}

	@Override
	public void add(int index, E element) {
		checkNewTagType(element);
		elements.add(index, element);
	}

	@Override
	public Stream<E> stream() {
		return elements.stream();
	}

	@Override
	public E remove(int index) {
		return elements.remove(index);
	}

	@Override
	public Stream<E> parallelStream() {
		return elements.parallelStream();
	}

	@Override
	public int indexOf(Object o) {
		return elements.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return elements.lastIndexOf(o);
	}

	@Override
	public ListIterator<E> listIterator() {
		return elements.listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return elements.listIterator(index);
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		return elements.subList(fromIndex, toIndex);
	}

	@Override
	public Spliterator<E> spliterator() {
		return elements.spliterator();
	}

	private void checkNewTagType(Tag tag) {
		if (!elements.isEmpty()) {
			if (elements.get(0).getId() != tag.getId()) {
				throw new IllegalArgumentException("Trying to insert an incompatible type of tag");
			}
		}
	}

	private void checkNewTagTypes(Collection<? extends E> tags) {
		byte type;

		if (elements.isEmpty()) {
			type = 0;
		} else {
			type = elements.get(0).getId();
		}

		for (Tag tag : tags) {
			if (type == 0) {
				type = tag.getId();
			}

			if (tag.getId() != type) {
				throw new IllegalArgumentException("Incompatible types of tags detected");
			}
		}
	}

	private void checkTagTypes(Collection<? extends E> tags) {
		byte type = 0;
		for (Tag tag : tags) {
			if (type == 0) {
				type = tag.getId();
			}

			if (tag.getId() != type) {
				throw new IllegalArgumentException("Different types of tags detected");
			}
		}
	}
}
