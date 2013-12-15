package jp.mytools.disassemble.constantpool.beans;

import jp.mytools.disassemble.constantpool.enums.ConstantPoolType;


public class IntegerConstantPool extends IntegerFloatConstantPool {

	private static final long serialVersionUID = -8835248063220397710L;

	public IntegerConstantPool() {
		super(ConstantPoolType.INTEGER.getTag());
	}
	
}
