package jp.mytools.disassemble.constantpool.beans;

public class ReferenceConstantPool extends AbstractConstantPool {

	private static final long serialVersionUID = -1193316675735021805L;

	private static final int STRUCTURE_LENGTH = 5;
	
	protected ReferenceConstantPool(int tag) {
		super(tag);
	}

	private int classIndex;
	private int nameAndTypeIndex;

	public int getClassIndex() {
		return classIndex;
	}
	public void setClassIndex(int classIndex) {
		this.classIndex = classIndex;
	}
	public int getNameAndTypeIndex() {
		return nameAndTypeIndex;
	}
	public void setNameAndTypeIndex(int nameAndTypeIndex) {
		this.nameAndTypeIndex = nameAndTypeIndex;
	}
	@Override
	public int getStructureLength() {
		return STRUCTURE_LENGTH;
	}
	
	@Override
	public String toString() {
		return super.toString() + " , classIndex = " + classIndex + " , nameAndTypeIndex = " + nameAndTypeIndex;
	}
}
