package jp.mytools.disassemble.constantpool.beans;

import jp.mytools.disassemble.constantpool.enums.ConstantPoolType;


public class FloatConstantPool extends IntegerFloatConstantPool {

	public FloatConstantPool() {
		super(ConstantPoolType.FLOAT.getTag());
	}
	
}
