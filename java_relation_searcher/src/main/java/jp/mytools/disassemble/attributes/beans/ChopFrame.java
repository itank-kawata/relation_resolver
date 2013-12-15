package jp.mytools.disassemble.attributes.beans;

import java.io.Serializable;

public class ChopFrame extends StackMapFrame  implements Serializable {

	private static final long serialVersionUID = 9038938103051701512L;
	private int offsetDelta;

	public int getOffsetDelta() {
		return offsetDelta;
	}

	public void setOffsetDelta(int offsetDelta) {
		this.offsetDelta = offsetDelta;
	}
	
}
