package jp.mytools.relationsearch.constantpool.beans;

import jp.mytools.relationsearch.constantpool.enums.ConstantPoolType;


public class MethodrefConstantPool extends ReferenceConstantPool {

	public MethodrefConstantPool() {
		super(ConstantPoolType.METHODREF.getTag());
	}

}
