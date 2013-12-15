package jp.mytools.disassemble.attributes.beans;

public class SameLocals1StackItemFrame extends StackMapFrame {

	private static final long serialVersionUID = -9050833095752051836L;
	private VerificationTypeInfo stack;

	public VerificationTypeInfo getStack() {
		return stack;
	}

	public void setStack(VerificationTypeInfo stack) {
		this.stack = stack;
	}
}
