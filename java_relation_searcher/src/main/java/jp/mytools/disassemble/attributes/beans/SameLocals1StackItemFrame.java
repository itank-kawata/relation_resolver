package jp.mytools.disassemble.attributes.beans;

public class SameLocals1StackItemFrame extends StackMapFrame {
	private VerificationTypeInfo stack;

	public VerificationTypeInfo getStack() {
		return stack;
	}

	public void setStack(VerificationTypeInfo stack) {
		this.stack = stack;
	}
}
