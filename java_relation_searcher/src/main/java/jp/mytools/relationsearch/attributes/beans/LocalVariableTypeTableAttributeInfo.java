package jp.mytools.relationsearch.attributes.beans;

public class LocalVariableTypeTableAttributeInfo extends AttributeInfo {
	private int localVariableTypeTableLength;
	private LocalVariableTypeTable[] localVariableTypeTables;
	public int getLocalVariableTypeTableLength() {
		return localVariableTypeTableLength;
	}
	public void setLocalVariableTypeTableLength(int localVariableTypeTableLength) {
		this.localVariableTypeTableLength = localVariableTypeTableLength;
	}
	public LocalVariableTypeTable[] getLocalVariableTypeTables() {
		return localVariableTypeTables;
	}
	public void setLocalVariableTypeTables(LocalVariableTypeTable[] localVariableTypeTables) {
		this.localVariableTypeTables = localVariableTypeTables;
	}

}
