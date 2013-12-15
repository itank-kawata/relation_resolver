package jp.mytools.disassemble.opcode.beans;

import jp.mytools.disassemble.opcode.beans.ReferenceOpecode;

public class InvokeDynamicOpecode extends ReferenceOpecode {

	private static final long serialVersionUID = 7700943901380478276L;
	private int thirdOperand;
	private int fourthOperand;
	public int getThirdOperand() {
		return thirdOperand;
	}
	public void setThirdOperand(int thirdOperand) {
		this.thirdOperand = thirdOperand;
	}
	public int getFourthOperand() {
		return fourthOperand;
	}
	public void setFourthOperand(int fourthOperand) {
		this.fourthOperand = fourthOperand;
	}
	
}
