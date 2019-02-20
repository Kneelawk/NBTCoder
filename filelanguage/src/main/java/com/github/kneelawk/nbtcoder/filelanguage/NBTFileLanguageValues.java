package com.github.kneelawk.nbtcoder.filelanguage;

import java.util.regex.Pattern;

/**
 * Created by Kneelawk on 2/20/19.
 */
public class NBTFileLanguageValues {
	public static final String UNUSED_TIMESTAMP_KEY_FORMAT = "unusedTimestamp_x%s_z%s";
	public static final Pattern UNUSED_TIMESTAMP_KEY_PATTERN = Pattern.compile("^unusedTimestamp_x(?<x>[0-9]+)_z(?<z>[0-9]+)$");
}
