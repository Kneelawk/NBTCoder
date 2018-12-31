package com.github.kneelawk.hexlanguage;

import java.util.List;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.github.kneelawk.hexlanguage.HexLanguageSystemParser.DataContext;
import com.github.kneelawk.utils.InternalParseException;
import com.google.common.primitives.UnsignedBytes;

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
