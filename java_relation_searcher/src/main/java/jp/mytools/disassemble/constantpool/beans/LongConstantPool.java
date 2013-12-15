package jp.mytools.disassemble.constantpool.beans;

import jp.mytools.disassemble.constantpool.enums.ConstantPoolType;


public class LongConstantPool extends LongDoubleConstantPool {

	private static final long serialVersionUID = -4080188362819630121L;

	public LongConstantPool() {
		super(ConstantPoolType.LONG.getTag());
	}
	
}
