package jp.mytools.relationsearch.constantpool.beans;

import jp.mytools.relationsearch.constantpool.enums.ConstantPoolType;

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
