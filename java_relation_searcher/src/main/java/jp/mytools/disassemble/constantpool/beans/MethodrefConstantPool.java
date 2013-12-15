package jp.mytools.disassemble.constantpool.beans;

import jp.mytools.disassemble.constantpool.enums.ConstantPoolType;


public class MethodrefConstantPool extends ReferenceConstantPool {

	private static final long serialVersionUID = -928797137922296911L;

	public MethodrefConstantPool() {
		super(ConstantPoolType.METHODREF.getTag());
	}

}
