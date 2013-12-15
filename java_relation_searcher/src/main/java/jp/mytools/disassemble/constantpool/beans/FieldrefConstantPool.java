package jp.mytools.disassemble.constantpool.beans;

import jp.mytools.disassemble.constantpool.enums.ConstantPoolType;


public class FieldrefConstantPool extends ReferenceConstantPool {

	private static final long serialVersionUID = 2445186294731793315L;

	public FieldrefConstantPool() {
		super(ConstantPoolType.FIELDREF.getTag());
	}

}
