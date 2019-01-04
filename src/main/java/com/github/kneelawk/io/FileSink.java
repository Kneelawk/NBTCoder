package com.github.kneelawk.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileSink implements DataSink {

	private Path path;

	public FileSink(Path path) {
		this.path = path;
	}

	@Override
	public boolean isStream() {
		return false;
	}

	@Override
	public boolean isFile() {
		return Files.isRegularFile(path);
	}

	@Override
	public boolean isDirectory() {
		return Files.isDirectory(path);
	}

	@Override
	public OutputStream openStream() throws IOException {
		return Files.newOutputStream(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
	}

	@Override
	public DataSink getChild(String path) throws IOException {
		return new FileSink(this.path.resolve(path));
	}

}
