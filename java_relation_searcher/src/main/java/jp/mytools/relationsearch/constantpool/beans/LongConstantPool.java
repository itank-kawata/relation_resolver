package jp.mytools.relationsearch.constantpool.beans;

import jp.mytools.relationsearch.constantpool.enums.ConstantPoolType;


public class LongConstantPool extends LongDoubleConstantPool {

	public LongConstantPool() {
		super(ConstantPoolType.LONG.getTag());
	}
	
}
