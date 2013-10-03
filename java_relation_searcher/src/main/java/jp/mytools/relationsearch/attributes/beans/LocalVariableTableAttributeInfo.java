package jp.mytools.relationsearch.attributes.beans;

public class LocalVariableTableAttributeInfo extends AttributeInfo {
	private int localVariableTableLength;
	private LocalVariableTable[] localVariableTables;
	public int getLocalVariableTableLength() {
		return localVariableTableLength;
	}
	public void setLocalVariableTableLength(int localVariableTableLength) {
		this.localVariableTableLength = localVariableTableLength;
	}
	public LocalVariableTable[] getLocalVariableTables() {
		return localVariableTables;
	}
	public void setLocalVariableTables(LocalVariableTable[] localVariableTables) {
		this.localVariableTables = localVariableTables;
	}
	
}
