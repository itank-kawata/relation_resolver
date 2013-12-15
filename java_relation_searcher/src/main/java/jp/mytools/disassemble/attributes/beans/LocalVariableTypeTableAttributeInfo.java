package jp.mytools.disassemble.attributes.beans;

public class LocalVariableTypeTableAttributeInfo extends AttributeInfo {

	private static final long serialVersionUID = 6238962709759645032L;
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
