package jp.mytools.disassemble.attributes.beans;

import java.io.Serializable;

public class ElementValuePair implements Serializable  {

	private static final long serialVersionUID = 6813110656972020311L;
	private int elementNameIndex;
	private ElementValue elementNameValue;
	public int getElementNameIndex() {
		return elementNameIndex;
	}
	public void setElementNameIndex(int elementNameIndex) {
		this.elementNameIndex = elementNameIndex;
	}
	public ElementValue getElementNameValue() {
		return elementNameValue;
	}
	public void setElementNameValue(ElementValue elementNameValue) {
		this.elementNameValue = elementNameValue;
	}
	
}
