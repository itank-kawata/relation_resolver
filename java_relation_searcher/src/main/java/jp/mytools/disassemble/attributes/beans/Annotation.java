package jp.mytools.disassemble.attributes.beans;

public class Annotation implements ElementValue {
	private int typeIndex;
	private int numElementValuePairs;
	private ElementValuePair[] elementValuePairs;
	public int getTypeIndex() {
		return typeIndex;
	}
	public void setTypeIndex(int typeIndex) {
		this.typeIndex = typeIndex;
	}
	public int getNumElementValuePairs() {
		return numElementValuePairs;
	}
	public void setNumElementValuePairs(int numElementValuePairs) {
		this.numElementValuePairs = numElementValuePairs;
	}
	public ElementValuePair[] getElementValuePairs() {
		return elementValuePairs;
	}
	public void setElementValuePairs(ElementValuePair[] elementValuePairs) {
		this.elementValuePairs = elementValuePairs;
	}
	
}
