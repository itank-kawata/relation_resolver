package jp.mytools.disassemble.constantpool.beans;

import java.io.Serializable;

import jp.mytools.disassemble.constantpool.enums.ConstantPoolType;

public abstract class AbstractConstantPool implements ConstantPool ,Serializable  {
	
	private static final long serialVersionUID = 1L;

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
