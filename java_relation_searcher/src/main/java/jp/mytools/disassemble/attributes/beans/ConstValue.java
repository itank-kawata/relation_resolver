package jp.mytools.disassemble.attributes.beans;

public class ConstValue extends DefaultElementValue implements ElementValue {
	private int constValueIndex;	// u2

	public int getConstValueIndex() {
		return constValueIndex;
	}

	public void setConstValueIndex(int constValueIndex) {
		this.constValueIndex = constValueIndex;
	}
	
}
