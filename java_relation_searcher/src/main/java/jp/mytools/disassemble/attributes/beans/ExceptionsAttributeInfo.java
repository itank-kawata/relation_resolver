package jp.mytools.disassemble.attributes.beans;

public class ExceptionsAttributeInfo extends AttributeInfo {
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
