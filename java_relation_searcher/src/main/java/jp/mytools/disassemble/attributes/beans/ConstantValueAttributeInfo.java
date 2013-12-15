package jp.mytools.disassemble.attributes.beans;

import java.io.Serializable;

public class ConstantValueAttributeInfo extends AttributeInfo implements Serializable  {

	private static final long serialVersionUID = 4018933414573530833L;
	private int constantvalueIndex;
	public int getConstantvalueIndex() {
		return constantvalueIndex;
	}
	public void setConstantvalueIndex(int constantvalueIndex) {
		this.constantvalueIndex = constantvalueIndex;
	}
	@Override
	public String toString() {
		return super.toString() + " ConstantValueAttributeInfo [constantvalueIndex="
				+ constantvalueIndex + "]";
	}
	
	
}
