package jp.mytools.relationsearch.attributes.beans;

public class LineNumberTableAttributeInfo extends AttributeInfo {
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
