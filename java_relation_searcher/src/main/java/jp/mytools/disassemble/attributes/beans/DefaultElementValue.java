package jp.mytools.disassemble.attributes.beans;

import java.io.Serializable;

public class DefaultElementValue implements Serializable  {

	private static final long serialVersionUID = -8066789468064309493L;
	private String tag;	// u1

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
	
}
