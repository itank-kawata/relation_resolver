package jp.mytools.disassemble.attributes.beans;

public class ArrayValue extends DefaultElementValue implements ElementValue {
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
