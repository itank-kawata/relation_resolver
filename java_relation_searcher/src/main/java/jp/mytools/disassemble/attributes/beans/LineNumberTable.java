package jp.mytools.disassemble.attributes.beans;

import java.io.Serializable;

public class LineNumberTable implements Serializable  {

	private static final long serialVersionUID = 9170403341016528225L;
	private int startPc;
	private int lineNumber;
	public int getStartPc() {
		return startPc;
	}
	public void setStartPc(int startPc) {
		this.startPc = startPc;
	}
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
}
