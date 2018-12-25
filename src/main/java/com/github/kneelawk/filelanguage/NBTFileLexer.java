package com.github.kneelawk.filelanguage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class NBTFileLexer {
	private BufferedReader reader;

	public NBTFileLexer(InputStream is) {
		this(new InputStreamReader(is));
	}

	public NBTFileLexer(Reader reader) {
		this(new BufferedReader(reader));
	}

	public NBTFileLexer(BufferedReader reader) {
		this.reader = reader;
	}

	public FileStartToken readFileStart() throws IOException {
		String line = reader.readLine().trim();
		return new FileStartToken(line.equals("(file"), line);
	}

	public PropertiesStartToken readPropertiesStart() throws IOException {
		String line = reader.readLine().trim();
		return new PropertiesStartToken(line.equals("(properties"), line);
	}

	public PartitionStartToken readPartitionStart() throws IOException {
		String line = reader.readLine().trim();
		return new PartitionStartToken(line.equals("(partition"), line.equals(")"), line);
	}

	public DataStartToken readDataStart() throws IOException {
		String line = reader.readLine().trim();
		return new DataStartToken(line.equals("(data"), line);
	}

	public ParenthesisCloseToken readParenthesisClose() throws IOException {
		String line = reader.readLine().trim();

		return new ParenthesisCloseToken(line.equals(")"), line);
	}

	public String readLine() throws IOException {
		return reader.readLine();
	}

	public BufferedReader getReader() {
		return reader;
	}

	public static class FileStartToken {
		private boolean fileStart;
		private String string;

		public FileStartToken(boolean fileStart, String string) {
			this.fileStart = fileStart;
			this.string = string;
		}

		public boolean isFileStart() {
			return fileStart;
		}

		public String getString() {
			return string;
		}
	}

	public static class PropertiesStartToken {
		private boolean propertiesStart;
		private String string;

		public PropertiesStartToken(boolean propertiesStart, String string) {
			this.propertiesStart = propertiesStart;
			this.string = string;
		}

		public boolean isPropertiesStart() {
			return propertiesStart;
		}

		public String getString() {
			return string;
		}
	}

	public static class PartitionStartToken {
		private boolean partitionStart;
		private boolean parenthesisClose;
		private String string;

		public PartitionStartToken(boolean partitionStart, boolean parenthesisClose, String string) {
			this.partitionStart = partitionStart;
			this.parenthesisClose = parenthesisClose;
			this.string = string;
		}

		public boolean isPartitionStart() {
			return partitionStart;
		}

		public boolean isParenthesisClose() {
			return parenthesisClose;
		}

		public String getString() {
			return string;
		}
	}

	public static class DataStartToken {
		private boolean dataStart;
		private String string;

		public DataStartToken(boolean dataStart, String string) {
			this.dataStart = dataStart;
			this.string = string;
		}

		public boolean isDataStart() {
			return dataStart;
		}

		public String getString() {
			return string;
		}
	}

	public static class ParenthesisCloseToken {
		private boolean parenthesisClose;
		private String string;

		public ParenthesisCloseToken(boolean parenthesisClose, String string) {
			this.parenthesisClose = parenthesisClose;
			this.string = string;
		}

		public boolean isParenthesisClose() {
			return parenthesisClose;
		}

		public String getString() {
			return string;
		}
	}
}
