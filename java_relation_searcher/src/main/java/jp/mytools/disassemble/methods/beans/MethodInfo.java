package jp.mytools.disassemble.methods.beans;

import jp.mytools.disassemble.attributes.beans.Attribute;

public class MethodInfo {
	private int accessFlags;
	private int nameIndex;
	private int descriptorIndex;
	private int attributesCount;
	private Attribute[] attributes;
	public int getAccessFlags() {
		return accessFlags;
	}
	public void setAccessFlags(int accessFlags) {
		this.accessFlags = accessFlags;
	}
	public int getNameIndex() {
		return nameIndex;
	}
	public void setNameIndex(int nameIndex) {
		this.nameIndex = nameIndex;
	}
	public int getDescriptorIndex() {
		return descriptorIndex;
	}
	public void setDescriptorIndex(int descriptorIndex) {
		this.descriptorIndex = descriptorIndex;
	}
	public int getAttributesCount() {
		return attributesCount;
	}
	public void setAttributesCount(int attributesCount) {
		this.attributesCount = attributesCount;
	}
	public Attribute[] getAttributes() {
		return attributes;
	}
	public void setAttributes(Attribute[] attributes) {
		this.attributes = attributes;
	}
	
	@Override
	public String toString() {
		String result = "accessFlags = " + accessFlags + " , nameIndex = " + nameIndex + " , descriptorIndex = " + descriptorIndex +  " , attributesCount = " +  attributesCount + " , attributes = [";
		if (attributes != null) {
			for (Attribute attribute : attributes) {
				result += "\r\n\t" + attribute.toString();
			}
		}
		result += "]";

		return result;
	}
}
