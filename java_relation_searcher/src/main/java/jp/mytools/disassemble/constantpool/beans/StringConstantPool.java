package jp.mytools.disassemble.constantpool.beans;

import jp.mytools.disassemble.constantpool.enums.ConstantPoolType;


public class StringConstantPool extends AbstractConstantPool {

	private static final int STRUCTURE_LENGTH = 3;

	public StringConstantPool() {
		super(ConstantPoolType.STRING.getTag());
	}
	
	private int stringIndex;
	public int getStringIndex() {
		return stringIndex;
	}
	public void setStringIndex(int stringIndex) {
		this.stringIndex = stringIndex;
	}

	@Override
	public int getStructureLength() {
		return STRUCTURE_LENGTH;
	}

}
