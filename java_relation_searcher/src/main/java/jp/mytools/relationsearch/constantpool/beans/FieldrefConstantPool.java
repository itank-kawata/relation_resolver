package jp.mytools.relationsearch.constantpool.beans;

import jp.mytools.relationsearch.constantpool.enums.ConstantPoolType;


public class FieldrefConstantPool extends ReferenceConstantPool {

	public FieldrefConstantPool() {
		super(ConstantPoolType.FIELDREF.getTag());
	}

}
