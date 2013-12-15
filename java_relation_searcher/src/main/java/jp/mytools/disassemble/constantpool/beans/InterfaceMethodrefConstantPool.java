package jp.mytools.disassemble.constantpool.beans;

import jp.mytools.disassemble.constantpool.enums.ConstantPoolType;


public class InterfaceMethodrefConstantPool extends ReferenceConstantPool {

	private static final long serialVersionUID = 8392332053394450860L;

	public InterfaceMethodrefConstantPool() {
		super(ConstantPoolType.INTERFACEMETHODREF.getTag());
	}

}
