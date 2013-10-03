package jp.mytools.relationsearch.constantpool.beans;

import jp.mytools.relationsearch.constantpool.enums.ConstantPoolType;


public class InterfaceMethodrefConstantPool extends ReferenceConstantPool {

	public InterfaceMethodrefConstantPool() {
		super(ConstantPoolType.INTERFACEMETHODREF.getTag());
	}

}
