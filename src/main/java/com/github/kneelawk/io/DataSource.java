package com.github.kneelawk.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public interface DataSource {
	public boolean isStream();

	public boolean isFile();

	public boolean isDirectory();

	public boolean exists();

	public String getName();

	public InputStream openStream() throws IOException;

	public Collection<DataSource> getChildren() throws IOException;

	public DataSource getChild(String path) throws IOException;
}
