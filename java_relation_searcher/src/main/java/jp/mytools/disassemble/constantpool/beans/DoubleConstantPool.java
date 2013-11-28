package jp.mytools.disassemble.constantpool.beans;

import jp.mytools.disassemble.constantpool.enums.ConstantPoolType;


public class DoubleConstantPool extends LongDoubleConstantPool {

	public DoubleConstantPool() {
		super(ConstantPoolType.DOUBLE.getTag());
	}
	
}
