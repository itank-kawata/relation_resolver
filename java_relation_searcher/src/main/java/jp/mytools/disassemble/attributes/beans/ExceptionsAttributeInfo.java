package jp.mytools.disassemble.attributes.beans;

import java.io.Serializable;

public class ExceptionsAttributeInfo extends AttributeInfo implements Serializable  {

	private static final long serialVersionUID = -2663433722528837335L;
	private int numberOfExceptions;
	private int[] exceptionIndexTable;
	public int getNumberOfExceptions() {
		return numberOfExceptions;
	}
	public void setNumberOfExceptions(int numberOfExceptions) {
		this.numberOfExceptions = numberOfExceptions;
	}
	public int[] getExceptionIndexTable() {
		return exceptionIndexTable;
	}
	public void setExceptionIndexTable(int[] exceptionIndexTable) {
		this.exceptionIndexTable = exceptionIndexTable;
	}
	
}
