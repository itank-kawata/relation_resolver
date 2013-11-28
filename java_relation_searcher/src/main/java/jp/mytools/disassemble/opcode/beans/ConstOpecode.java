package jp.mytools.disassemble.opcode.beans;

public class ConstOpecode extends GeneralOpecode {
	private int index;
	private int constValue;
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getConstValue() {
		return constValue;
	}
	public void setConstValue(int constValue) {
		this.constValue = constValue;
	}
	
}
