package jp.mytools.disassemble.attributes.beans;

import java.io.Serializable;

public class ArrayValue extends DefaultElementValue  implements Serializable , ElementValue {

	private static final long serialVersionUID = 5364440952761694738L;
	private int numValues;	// u2
	private ElementValue[] elementValues;
	public int getNumValues() {
		return numValues;
	}
	public void setNumValues(int numValues) {
		this.numValues = numValues;
	}
	public ElementValue[] getElementValues() {
		return elementValues;
	}
	public void setElementValues(ElementValue[] elementValues) {
		this.elementValues = elementValues;
	}
}
