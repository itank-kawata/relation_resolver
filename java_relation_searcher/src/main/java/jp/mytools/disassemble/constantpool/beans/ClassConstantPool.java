package jp.mytools.disassemble.constantpool.beans;

import jp.mytools.disassemble.constantpool.enums.ConstantPoolType;


public class ClassConstantPool extends AbstractConstantPool {

	private static final long serialVersionUID = 8028553562152106735L;
	private static final int STRUCTURE_LENGTH = 3;

	public ClassConstantPool() {
		super(ConstantPoolType.CLASS.getTag());
	}
	
	private int nameIndex;

	public int getNameIndex() {
		return nameIndex;
	}

	public void setNameIndex(int nameIndex) {
		this.nameIndex = nameIndex;
	}

	@Override
	public int getStructureLength() {
		return STRUCTURE_LENGTH;
	}
	
	@Override
	public String toString() {
		return super.toString() + ", nameIndex = " + nameIndex;
	}
}
