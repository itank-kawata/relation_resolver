package jp.mytools.relationsearch.constantpool.beans;

import jp.mytools.relationsearch.constantpool.enums.ConstantPoolType;


public class FloatConstantPool extends IntegerFloatConstantPool {

	public FloatConstantPool() {
		super(ConstantPoolType.FLOAT.getTag());
	}
	
}
