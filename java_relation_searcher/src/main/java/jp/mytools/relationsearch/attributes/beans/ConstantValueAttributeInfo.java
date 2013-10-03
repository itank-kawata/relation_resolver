package jp.mytools.relationsearch.attributes.beans;

public class ConstantValueAttributeInfo extends AttributeInfo {

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
