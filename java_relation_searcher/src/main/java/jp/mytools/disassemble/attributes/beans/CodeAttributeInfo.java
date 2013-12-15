package jp.mytools.disassemble.attributes.beans;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import jp.mytools.disassemble.opcode.beans.Opcode;

public class CodeAttributeInfo extends AttributeInfo implements Serializable {

	private static final long serialVersionUID = 7796739478948420748L;
	private int maxStack;
	private int maxLocals;
	private int codeLength;
	private List<Opcode> opcodes;
	private int exceptionTableLength;
	private ExceptionTableInfo[] exceptionTables;
	private int attributesCount;
	private AttributeInfo[] attributes;
	public int getMaxStack() {
		return maxStack;
	}
	public void setMaxStack(int maxStack) {
		this.maxStack = maxStack;
	}
	public int getMaxLocals() {
		return maxLocals;
	}
	public void setMaxLocals(int maxLocals) {
		this.maxLocals = maxLocals;
	}
	public int getCodeLength() {
		return codeLength;
	}
	public void setCodeLength(int codeLength) {
		this.codeLength = codeLength;
	}
	public int getExceptionTableLength() {
		return exceptionTableLength;
	}
	public void setExceptionTableLength(int exceptionTableLength) {
		this.exceptionTableLength = exceptionTableLength;
	}
	public ExceptionTableInfo[] getExceptionTables() {
		return exceptionTables;
	}
	public void setExceptionTables(ExceptionTableInfo[] exceptionTables) {
		this.exceptionTables = exceptionTables;
	}
	public int getAttributesCount() {
		return attributesCount;
	}
	public void setAttributesCount(int attributesCount) {
		this.attributesCount = attributesCount;
	}
	public AttributeInfo[] getAttributes() {
		return attributes;
	}
	public void setAttributes(AttributeInfo[] attributes) {
		this.attributes = attributes;
	}
	public List<Opcode> getOpcodes() {
		return opcodes;
	}
	public void setOpcodes(List<Opcode> opcodes) {
		this.opcodes = opcodes;
	}
	@Override
	public String toString() {
		return super.toString() + " CodeAttributeInfo [maxStack=" + maxStack + ", maxLocals="
				+ maxLocals + ", codeLength=" + codeLength + ", opcodes="
				+ opcodes + ", exceptionTableLength=" + exceptionTableLength
				+ ", exceptionTables=" + Arrays.toString(exceptionTables)
				+ ", attributesCount=" + attributesCount + ", attributes="
				+ Arrays.toString(attributes) + "]";
	}

	
}
