package jp.mytools.disassemble.attributes.beans;

public class ClassInfoValue extends DefaultElementValue implements ElementValue {
	private int classInfoIndex;	// u2

	public int getClassInfoIndex() {
		return classInfoIndex;
	}

	public void setClassInfoIndex(int classInfoIndex) {
		this.classInfoIndex = classInfoIndex;
	}
	
}
