package com.github.kneelawk.nbtcoder.hexlanguage;

import com.github.kneelawk.nbtcoder.hexlanguage.HexLanguageSystemParser.DataContext;
import com.github.kneelawk.nbtcoder.utils.InternalParseException;
import com.google.common.primitives.UnsignedBytes;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

public class HexLanguageBuilderListener extends HexLanguageSystemBaseListener {
	private byte[] data;

	public byte[] getData() {
		return data;
	}

	@Override
	public void exitData(DataContext ctx) {
		List<TerminalNode> byteNodes = ctx.BYTE();
		int dataLen = byteNodes.size();
		data = new byte[dataLen];

		for (int i = 0; i < dataLen; i++) {
			TerminalNode byteNode = byteNodes.get(i);
			try {
				data[i] = UnsignedBytes.parseUnsignedByte(byteNode.getText(), 16);
			} catch (NumberFormatException e) {
				throw new InternalParseException(e, byteNode);
			}
		}
	}

	@Override
	public void visitErrorNode(ErrorNode node) {
		throw new InternalParseException("Error node", node);
	}
}
