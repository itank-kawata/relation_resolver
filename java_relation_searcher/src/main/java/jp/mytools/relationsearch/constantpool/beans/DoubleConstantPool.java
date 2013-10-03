package jp.mytools.relationsearch.constantpool.beans;

import jp.mytools.relationsearch.constantpool.enums.ConstantPoolType;


public class DoubleConstantPool extends LongDoubleConstantPool {

	public DoubleConstantPool() {
		super(ConstantPoolType.DOUBLE.getTag());
	}
	
}
