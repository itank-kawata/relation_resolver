package jp.mytools.disassemble.attributes.beans;


public class AttributeInfo implements Attribute {
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
