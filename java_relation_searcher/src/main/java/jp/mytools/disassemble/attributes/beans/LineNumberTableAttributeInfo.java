package jp.mytools.disassemble.attributes.beans;

public class LineNumberTableAttributeInfo extends AttributeInfo {
	private static final long serialVersionUID = 5163421913878670115L;
	private int lineNumberTableLength;
	private LineNumberTable[] lineNumberTables;
	public int getLineNumberTableLength() {
		return lineNumberTableLength;
	}
	public void setLineNumberTableLength(int lineNumberTableLength) {
		this.lineNumberTableLength = lineNumberTableLength;
	}
	public LineNumberTable[] getLineNumberTables() {
		return lineNumberTables;
	}
	public void setLineNumberTables(LineNumberTable[] lineNumberTables) {
		this.lineNumberTables = lineNumberTables;
	}
	
}
