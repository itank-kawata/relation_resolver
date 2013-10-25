package jp.mytools.relationsearch;

import jp.mytools.relationsearch.opcode.enums.OpcodeType;

public class CallMethod {
	private OpcodeType opType;
	private String className;
	private String methodName;
	private String methodDescriptor;
	private String implClassName;
	
	public OpcodeType getOpType() {
		return opType;
	}
	public void setOpType(OpcodeType opType) {
		this.opType = opType;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getMethodDescriptor() {
		return methodDescriptor;
	}
	public void setMethodDescriptor(String methodDescriptor) {
		this.methodDescriptor = methodDescriptor;
	}
	
	public String getFullMethodName() {
		return className + "#" + methodName + methodDescriptor;
	}
	public String getImplClassName() {
		return implClassName;
	}
	public void setImplClassName(String implClassName) {
		this.implClassName = implClassName;
	}
	
}
