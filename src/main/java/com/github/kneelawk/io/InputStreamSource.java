package com.github.kneelawk.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public class InputStreamSource implements DataSource {

	private InputStream stream;

	public InputStreamSource(InputStream stream) {
		this.stream = stream;
	}

	@Override
	public boolean isStream() {
		return true;
	}

	@Override
	public boolean isFile() {
		return false;
	}

	@Override
	public boolean isDirectory() {
		return false;
	}

	@Override
	public boolean exists() {
		return true;
	}

	@Override
	public String getName() {
		return "-";
	}

	@Override
	public InputStream openStream() {
		return stream;
	}

	@Override
	public Collection<DataSource> getChildren() throws IOException {
		throw new IOException("getChildren is not supported on InputStreamSource");
	}

	@Override
	public DataSource getChild(String path) throws IOException {
		throw new IOException("getChild is not supported on InputStreamSource");
	}

}
