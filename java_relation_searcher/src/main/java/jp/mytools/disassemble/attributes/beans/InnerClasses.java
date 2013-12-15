package jp.mytools.disassemble.attributes.beans;

import java.io.Serializable;

public class InnerClasses implements Serializable  {

	private static final long serialVersionUID = -7155967772426248898L;
	private int innerClassInfoIndex;
	private int outerClassInfoIndex;
	private int innerNameIndex;
	private int innerClassAccessFlags;
	public int getInnerClassInfoIndex() {
		return innerClassInfoIndex;
	}
	public void setInnerClassInfoIndex(int innerClassInfoIndex) {
		this.innerClassInfoIndex = innerClassInfoIndex;
	}
	public int getOuterClassInfoIndex() {
		return outerClassInfoIndex;
	}
	public void setOuterClassInfoIndex(int outerClassInfoIndex) {
		this.outerClassInfoIndex = outerClassInfoIndex;
	}
	public int getInnerNameIndex() {
		return innerNameIndex;
	}
	public void setInnerNameIndex(int innerNameIndex) {
		this.innerNameIndex = innerNameIndex;
	}
	public int getInnerClassAccessFlags() {
		return innerClassAccessFlags;
	}
	public void setInnerClassAccessFlags(int innerClassAccessFlags) {
		this.innerClassAccessFlags = innerClassAccessFlags;
	}
	
}
