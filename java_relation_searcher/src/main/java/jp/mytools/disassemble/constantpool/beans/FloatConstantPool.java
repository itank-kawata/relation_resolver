package jp.mytools.disassemble.constantpool.beans;

import jp.mytools.disassemble.constantpool.enums.ConstantPoolType;


public class FloatConstantPool extends IntegerFloatConstantPool {

	private static final long serialVersionUID = 401411006919759357L;

	public FloatConstantPool() {
		super(ConstantPoolType.FLOAT.getTag());
	}
	
}
