package jp.mytools.disassemble.constantpool.beans;

import jp.mytools.disassemble.constantpool.enums.ConstantPoolType;


public class MethodrefConstantPool extends ReferenceConstantPool {

	public MethodrefConstantPool() {
		super(ConstantPoolType.METHODREF.getTag());
	}

}
