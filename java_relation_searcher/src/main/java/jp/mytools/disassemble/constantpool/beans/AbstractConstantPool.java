package jp.mytools.disassemble.constantpool.beans;

import jp.mytools.disassemble.constantpool.enums.ConstantPoolType;

public abstract class AbstractConstantPool implements ConstantPool {
	
	protected AbstractConstantPool(int tag) {
		this.tag = tag;
	}
	
	private int tag;
	public int getTag() {
		return tag;
	}
	
	public String toString() {
		return "tag = " + ConstantPoolType.getByTag(tag) + "(" + tag + ")";
	}
	
}
