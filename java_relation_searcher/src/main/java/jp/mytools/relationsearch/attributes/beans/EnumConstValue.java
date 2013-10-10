package jp.mytools.relationsearch.attributes.beans;

public class EnumConstValue extends DefaultElementValue implements ElementValue {
	private int typeNameIndex;	// u2
	private int constNameIndex;	// u2
	public int getTypeNameIndex() {
		return typeNameIndex;
	}
	public void setTypeNameIndex(int typeNameIndex) {
		this.typeNameIndex = typeNameIndex;
	}
	public int getConstNameIndex() {
		return constNameIndex;
	}
	public void setConstNameIndex(int constNameIndex) {
		this.constNameIndex = constNameIndex;
	}
	
}
