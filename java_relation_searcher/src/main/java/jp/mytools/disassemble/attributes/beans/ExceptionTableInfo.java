package jp.mytools.disassemble.attributes.beans;

import java.io.Serializable;

public class ExceptionTableInfo implements Serializable {

	private static final long serialVersionUID = 7643524781623290008L;
	private int startPc;
	private int endPc;
	private int handlerPc;
	private int catchType;
	public int getStartPc() {
		return startPc;
	}
	public void setStartPc(int startPc) {
		this.startPc = startPc;
	}
	public int getEndPc() {
		return endPc;
	}
	public void setEndPc(int endPc) {
		this.endPc = endPc;
	}
	public int getHandlerPc() {
		return handlerPc;
	}
	public void setHandlerPc(int handlerPc) {
		this.handlerPc = handlerPc;
	}
	public int getCatchType() {
		return catchType;
	}
	public void setCatchType(int catchType) {
		this.catchType = catchType;
	}
	@Override
	public String toString() {
		return "ExceptionTableInfo [startPc=" + startPc + ", endPc=" + endPc
				+ ", handlerPc=" + handlerPc + ", catchType=" + catchType + "]";
	}
	
}
