package com.github.kneelawk.io;

import java.io.IOException;
import java.io.OutputStream;

public interface DataSink {
	public boolean isStream();

	public boolean isFile();

	public boolean isDirectory();

	public OutputStream openStream() throws IOException;

	public DataSink getChild(String path) throws IOException;
}
