package jp.mytools.relationsearch.attributes.beans;

public class EnclosingMethodAttributeInfo extends AttributeInfo {
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
