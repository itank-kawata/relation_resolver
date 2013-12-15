package jp.mytools.disassemble.attributes.beans;

import java.io.Serializable;

public class AppendFrame extends StackMapFrame implements Serializable  {

	private static final long serialVersionUID = -3361120449072897411L;
	private int offsetDelta;
	private VerificationTypeInfo[] locals;
	public int getOffsetDelta() {
		return offsetDelta;
	}
	public void setOffsetDelta(int offsetDelta) {
		this.offsetDelta = offsetDelta;
	}
	public VerificationTypeInfo[] getLocals() {
		return locals;
	}
	public void setLocals(VerificationTypeInfo[] locals) {
		this.locals = locals;
	}
	
}
