package jp.mytools.disassemble.fields.beans;

import java.io.Serializable;

import jp.mytools.disassemble.attributes.beans.Attribute;

public class FieldInfo implements Serializable  {

	private static final long serialVersionUID = 6540807402608507478L;


	private int accessFlags;
	private int nameIndex;
	private int descriptorIndex;
	private int attributeCount;
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
	public int getAttributeCount() {
		return attributeCount;
	}
	public void setAttributeCount(int attributeCount) {
		this.attributeCount = attributeCount;
	}
	public Attribute[] getAttributes() {
		return attributes;
	}
	public void setAttributes(Attribute[] attributes) {
		this.attributes = attributes;
	}
	
	@Override
	public String toString() {
		String result = "accessFlags = " + accessFlags + " , nameIndex = " + nameIndex + " , descriptorIndex = " + descriptorIndex +  " , attributeCount = " +  attributeCount + " , attributes = [";
		if (attributes != null) {
			for (Attribute attribute : attributes) {
				result += "\r\n\t" + attribute.toString();
			}
		}
		result += "]";

		return result;
	}
}
