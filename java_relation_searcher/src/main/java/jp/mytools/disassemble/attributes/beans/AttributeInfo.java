package jp.mytools.disassemble.attributes.beans;

import java.io.Serializable;


public class AttributeInfo  implements Serializable , Attribute {
	private static final long serialVersionUID = 5792083671966350616L;
	private int attributeNameIndex;
	private int attributeLength;

	public int getAttributeNameIndex() {
		return attributeNameIndex;
	}
	public void setAttributeNameIndex(int attributeNameIndex) {
		this.attributeNameIndex = attributeNameIndex;
	}
	public int getAttributeLength() {
		return attributeLength;
	}
	public void setAttributeLength(int attributeLength) {
		this.attributeLength = attributeLength;
	}

	
	@Override
	public String toString() {
		return "attributeNameIndex = " + attributeNameIndex + " , attributeLength = " + attributeLength;
	}
}
