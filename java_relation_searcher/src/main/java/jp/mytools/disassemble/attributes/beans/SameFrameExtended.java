package jp.mytools.disassemble.attributes.beans;

public class SameFrameExtended extends SameFrame {

	private static final long serialVersionUID = -1333596525421466030L;
	private int offsetDelta;

	public int getOffsetDelta() {
		return offsetDelta;
	}

	public void setOffsetDelta(int offsetDelta) {
		this.offsetDelta = offsetDelta;
	}
}
