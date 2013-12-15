package jp.mytools.disassemble.attributes.beans;

import java.io.Serializable;

public class ConstValue extends DefaultElementValue  implements Serializable , ElementValue {

	private static final long serialVersionUID = -5437502981971566640L;
	private int constValueIndex;	// u2

	public int getConstValueIndex() {
		return constValueIndex;
	}

	public void setConstValueIndex(int constValueIndex) {
		this.constValueIndex = constValueIndex;
	}
	
}
