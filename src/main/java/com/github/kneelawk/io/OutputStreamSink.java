package com.github.kneelawk.io;

import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamSink implements DataSink {

	private OutputStream stream;

	public OutputStreamSink(OutputStream stream) {
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
	public OutputStream openStream() {
		return stream;
	}

	@Override
	public DataSink getChild(String path) throws IOException {
		throw new IOException("getChild is not supported on OutputStreamSink");
	}

}
