package jp.mytools.disassemble.attributes.beans;

import java.io.Serializable;

public class EnumConstValue extends DefaultElementValue  implements Serializable , ElementValue {

	private static final long serialVersionUID = 4573667433173189040L;
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
