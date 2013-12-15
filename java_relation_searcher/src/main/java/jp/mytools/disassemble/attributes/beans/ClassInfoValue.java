package jp.mytools.disassemble.attributes.beans;

import java.io.Serializable;

public class ClassInfoValue extends DefaultElementValue  implements Serializable , ElementValue {

	private static final long serialVersionUID = 8396249131571000911L;
	private int classInfoIndex;	// u2

	public int getClassInfoIndex() {
		return classInfoIndex;
	}

	public void setClassInfoIndex(int classInfoIndex) {
		this.classInfoIndex = classInfoIndex;
	}
	
}
