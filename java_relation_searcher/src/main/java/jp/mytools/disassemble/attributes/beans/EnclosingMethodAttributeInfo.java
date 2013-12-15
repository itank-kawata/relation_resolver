package jp.mytools.disassemble.attributes.beans;

import java.io.Serializable;

public class EnclosingMethodAttributeInfo extends AttributeInfo implements Serializable  {

	private static final long serialVersionUID = 4680607550659536776L;
	private int classIndex;
	private int methodIndex;
	public int getClassIndex() {
		return classIndex;
	}
	public void setClassIndex(int classIndex) {
		this.classIndex = classIndex;
	}
	public int getMethodIndex() {
		return methodIndex;
	}
	public void setMethodIndex(int methodIndex) {
		this.methodIndex = methodIndex;
	}
}
