package jp.mytools.disassemble.attributes.beans;

import java.io.Serializable;

public class VerificationTypeInfo implements Serializable  {

	private static final long serialVersionUID = 2841083916523454846L;
	private int tag;

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}
	
}
