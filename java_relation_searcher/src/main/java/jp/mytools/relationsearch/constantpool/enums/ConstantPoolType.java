package jp.mytools.relationsearch.constantpool.enums;

public enum ConstantPoolType {
	UTF8(1,"Utf8"),
	METHODREF(10,"Methodref"),
	NAMEANDTYPE(12,"NameAndType"),
	CLASS(7,"Class"),
	FIELDREF(9,"Fieldref"),
	STRING(8,"String"),
	INTEGER(3,"Integer"),
	FLOAT(4,"Float"),
	LONG(5,"Long"),
	DOUBLE(6,"Double"),
	INTERFACEMETHODREF(11,"InterfaceMethodref");
	
	private ConstantPoolType(int tag,String typeName) {
		this.tag = tag;
		this.typeName = typeName;
	}
	
	private int tag;
	public int getTag() {
		return tag;
	}
	
	private int structureLength;
	public int getStructureLength() {
		return structureLength;
	}

	private String typeName;
	public String getTypeName() {
		return typeName;
	}
	
	public static ConstantPoolType getByTag(int tag) {
		for (ConstantPoolType target :ConstantPoolType.values()) {
			if (target.getTag() == tag) return target;
		}
		return null;
	}
	
}
