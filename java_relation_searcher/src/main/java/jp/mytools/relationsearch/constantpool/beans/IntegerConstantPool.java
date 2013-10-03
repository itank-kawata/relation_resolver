package jp.mytools.relationsearch.constantpool.beans;

import jp.mytools.relationsearch.constantpool.enums.ConstantPoolType;


public class IntegerConstantPool extends IntegerFloatConstantPool {

	public IntegerConstantPool() {
		super(ConstantPoolType.INTEGER.getTag());
	}
	
}
