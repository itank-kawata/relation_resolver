package jp.mytools.disassemble.opcode.beans;

public class InvokeInterfaceOpecode extends ReferenceOpecode {

	private static final long serialVersionUID = 976194729680081521L;
	private int count;
	private int thirdIndex;
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getThirdIndex() {
		return thirdIndex;
	}
	public void setThirdIndex(int thirdIndex) {
		this.thirdIndex = thirdIndex;
	}
}
