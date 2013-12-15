package jp.mytools.disassemble.attributes.beans;

import java.io.Serializable;

public class FullFrame extends StackMapFrame implements Serializable  {

	private static final long serialVersionUID = 8022954161139102623L;
	private int offsetDelta;
	private int numberOfLocals;
	private VerificationTypeInfo[] locals;
	private int numberOfStackItems;
	private VerificationTypeInfo[] stack;
	public int getOffsetDelta() {
		return offsetDelta;
	}
	public void setOffsetDelta(int offsetDelta) {
		this.offsetDelta = offsetDelta;
	}
	public int getNumberOfLocals() {
		return numberOfLocals;
	}
	public void setNumberOfLocals(int numberOfLocals) {
		this.numberOfLocals = numberOfLocals;
	}
	public VerificationTypeInfo[] getLocals() {
		return locals;
	}
	public void setLocals(VerificationTypeInfo[] locals) {
		this.locals = locals;
	}
	public int getNumberOfStackItems() {
		return numberOfStackItems;
	}
	public void setNumberOfStackItems(int numberOfStackItems) {
		this.numberOfStackItems = numberOfStackItems;
	}
	public VerificationTypeInfo[] getStack() {
		return stack;
	}
	public void setStack(VerificationTypeInfo[] stack) {
		this.stack = stack;
	}
	
}
