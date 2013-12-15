package jp.mytools.disassemble.attributes.beans;

public class UninitializedVariableInfo extends VerificationTypeInfo {

	private static final long serialVersionUID = -3162674701078032020L;
	private int offset;

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}
	
}
