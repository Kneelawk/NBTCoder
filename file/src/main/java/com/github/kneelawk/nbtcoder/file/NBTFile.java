package com.github.kneelawk.nbtcoder.file;

import java.io.IOException;
import java.io.OutputStream;

public interface NBTFile {
	String getFilename();

	String getFileType();

	void writeToStream(OutputStream os) throws IOException;
}
