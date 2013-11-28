package jp.mytools.disassemble.constantpool.beans;

import jp.mytools.disassemble.constantpool.enums.ConstantPoolType;


public class InterfaceMethodrefConstantPool extends ReferenceConstantPool {

	public InterfaceMethodrefConstantPool() {
		super(ConstantPoolType.INTERFACEMETHODREF.getTag());
	}

}
