package jp.mytools.disassemble.constantpool.beans;

import jp.mytools.disassemble.constantpool.enums.ConstantPoolType;


public class DoubleConstantPool extends LongDoubleConstantPool {

	private static final long serialVersionUID = -8987920522366464706L;

	public DoubleConstantPool() {
		super(ConstantPoolType.DOUBLE.getTag());
	}
	
}
