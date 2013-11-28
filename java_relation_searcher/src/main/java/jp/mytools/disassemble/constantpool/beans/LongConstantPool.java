package jp.mytools.disassemble.constantpool.beans;

import jp.mytools.disassemble.constantpool.enums.ConstantPoolType;


public class LongConstantPool extends LongDoubleConstantPool {

	public LongConstantPool() {
		super(ConstantPoolType.LONG.getTag());
	}
	
}
