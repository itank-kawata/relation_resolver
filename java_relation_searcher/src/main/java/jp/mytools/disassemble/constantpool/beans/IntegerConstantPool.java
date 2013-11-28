package jp.mytools.disassemble.constantpool.beans;

import jp.mytools.disassemble.constantpool.enums.ConstantPoolType;


public class IntegerConstantPool extends IntegerFloatConstantPool {

	public IntegerConstantPool() {
		super(ConstantPoolType.INTEGER.getTag());
	}
	
}
