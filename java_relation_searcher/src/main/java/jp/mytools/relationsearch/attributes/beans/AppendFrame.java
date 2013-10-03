package jp.mytools.relationsearch.attributes.beans;

public class AppendFrame extends StackMapFrame {
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
