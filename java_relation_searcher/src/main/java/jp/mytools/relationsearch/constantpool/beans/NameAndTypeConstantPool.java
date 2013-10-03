package jp.mytools.relationsearch.constantpool.beans;

import jp.mytools.relationsearch.constantpool.enums.ConstantPoolType;


public class NameAndTypeConstantPool extends AbstractConstantPool {
	private static final int STRUCTURE_LENGTH = 5;
	public NameAndTypeConstantPool() {
		super(ConstantPoolType.NAMEANDTYPE.getTag());
	}
	
	private int nameIndex;
	private int descriptorIndex;
	
	public int getNameIndex() {
		return nameIndex;
	}
	public void setNameIndex(int nameIndex) {
		this.nameIndex = nameIndex;
	}
	public int getDescriptorIndex() {
		return descriptorIndex;
	}
	public void setDescriptorIndex(int descriptorIndex) {
		this.descriptorIndex = descriptorIndex;
	}

	@Override
	public int getStructureLength() {
		return STRUCTURE_LENGTH;
	}
	
	@Override
	public String toString() {
		return super.toString() + " , nameIndex = " + nameIndex + " , descriptorIndex = " + descriptorIndex;
	}
}
