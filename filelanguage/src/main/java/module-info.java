module com.github.kneelawk.nbtcoder.filelanguage {
	exports com.github.kneelawk.nbtcoder.filelanguage;

	requires org.antlr.antlr4.runtime;
	requires transitive com.github.kneelawk.nbtcoder.file;
	requires com.github.kneelawk.nbtcoder.hexlanguage;
	requires com.github.kneelawk.nbtcoder.nbtlanguage;
	requires com.github.kneelawk.nbtcoder.utils;
	requires com.google.common;
	requires org.apache.commons.configuration2;
	requires org.apache.commons.io;
}
