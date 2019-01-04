package com.github.kneelawk.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

public class FileSource implements DataSource {

	private Path base;
	private Path path;

	public FileSource(Path base, String path) {
		this.base = base;
		this.path = base.resolve(path);
	}

	public FileSource(Path base, Path path) {
		this.base = base;
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
	public boolean exists() {
		return Files.exists(path);
	}

	@Override
	public String getName() {
		return base.relativize(path).toString();
	}

	@Override
	public InputStream openStream() throws IOException {
		return Files.newInputStream(path, StandardOpenOption.READ);
	}

	@Override
	public Collection<DataSource> getChildren() throws IOException {
		return Files.list(path).map(p -> new FileSource(base, p)).collect(Collectors.toCollection(Lists::newArrayList));
	}

	@Override
	public DataSource getChild(String path) throws IOException {
		return new FileSource(base, this.path.resolve(path));
	}

}
