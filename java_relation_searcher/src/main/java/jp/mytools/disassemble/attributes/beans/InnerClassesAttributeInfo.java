package jp.mytools.disassemble.attributes.beans;

public class InnerClassesAttributeInfo extends AttributeInfo {
	private int numberOfClasses;
	private InnerClasses[] classes;
	public int getNumberOfClasses() {
		return numberOfClasses;
	}
	public void setNumberOfClasses(int numberOfClasses) {
		this.numberOfClasses = numberOfClasses;
	}
	public InnerClasses[] getClasses() {
		return classes;
	}
	public void setClasses(InnerClasses[] classes) {
		this.classes = classes;
	}
	
}
