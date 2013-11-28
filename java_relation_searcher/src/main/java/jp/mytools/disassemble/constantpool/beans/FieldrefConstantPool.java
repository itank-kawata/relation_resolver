package jp.mytools.disassemble.constantpool.beans;

import jp.mytools.disassemble.constantpool.enums.ConstantPoolType;


public class FieldrefConstantPool extends ReferenceConstantPool {

	public FieldrefConstantPool() {
		super(ConstantPoolType.FIELDREF.getTag());
	}

}
