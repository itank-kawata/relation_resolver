package jp.mytools.disassemble.attributes.beans;

public class SameLocals1StackItemFrameExtended extends
		SameLocals1StackItemFrame {


	private static final long serialVersionUID = -8386658519182426680L;
	private int offsetDelta;

	public int getOffsetDelta() {
		return offsetDelta;
	}

	public void setOffsetDelta(int offsetDelta) {
		this.offsetDelta = offsetDelta;
	}
}
