package jp.mytools.disassemble.attributes.beans;

public class StackMapTableAttributeInfo extends AttributeInfo {
	private int numberOfEntries;
	private StackMapFrame[] entries;
	public int getNumberOfEntries() {
		return numberOfEntries;
	}
	public void setNumberOfEntries(int numberOfEntries) {
		this.numberOfEntries = numberOfEntries;
	}
	public StackMapFrame[] getEntries() {
		return entries;
	}
	public void setEntries(StackMapFrame[] entries) {
		this.entries = entries;
	}
	
}
