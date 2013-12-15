package jp.mytools.disassemble.attributes.beans;

import java.io.Serializable;

public class InnerClassesAttributeInfo extends AttributeInfo implements Serializable  {

	private static final long serialVersionUID = -5493347592304819382L;
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
