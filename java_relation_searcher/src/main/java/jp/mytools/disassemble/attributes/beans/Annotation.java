package jp.mytools.disassemble.attributes.beans;

import java.io.Serializable;

public class Annotation implements ElementValue ,Serializable  {

	private static final long serialVersionUID = 4174607586689740089L;
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
