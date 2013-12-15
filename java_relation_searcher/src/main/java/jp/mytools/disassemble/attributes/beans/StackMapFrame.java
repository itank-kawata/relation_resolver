package jp.mytools.disassemble.attributes.beans;

import java.io.Serializable;

public class StackMapFrame implements Serializable {

	private static final long serialVersionUID = 5778039842739695795L;
	private int frameType;

	public int getFrameType() {
		return frameType;
	}

	public void setFrameType(int frameType) {
		this.frameType = frameType;
	}
}
