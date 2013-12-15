package jp.mytools.disassemble.classfile.beans;

import java.io.Serializable;
import java.util.Map;

import jp.mytools.disassemble.attributes.beans.Attribute;
import jp.mytools.disassemble.constantpool.beans.ConstantPool;
import jp.mytools.disassemble.fields.beans.FieldInfo;
import jp.mytools.disassemble.methods.beans.MethodInfo;

public class ClassFileInfo implements Serializable {
	private static final long serialVersionUID = 6678884892440191604L;

	private int magic;
	private int minorVersion;
	private int majorVersion;
	private int constantPoolCount;
	private Map<Integer,ConstantPool> constantPoolMap;
	private int accessFlag;
	private int thisClass;
	private int superClass;
	private int interfacesCount;
	private int[] interfaces;
	private int fieldsCount;
	private FieldInfo[] fields;
	private int methodsCount;
	private MethodInfo[] methods;
	private int attributesCount;
	private Attribute[] attributes;
	
	public int getMagic() {
		return magic;
	}
	public void setMagic(int magic) {
		this.magic = magic;
	}
	public int getMinorVersion() {
		return minorVersion;
	}
	public void setMinorVersion(int minorVersion) {
		this.minorVersion = minorVersion;
	}
	public int getMajorVersion() {
		return majorVersion;
	}
	public void setMajorVersion(int majorVersion) {
		this.majorVersion = majorVersion;
	}
	public int getConstantPoolCount() {
		return constantPoolCount;
	}
	public void setConstantPoolCount(int constantPoolCount) {
		this.constantPoolCount = constantPoolCount;
	}
	public Map<Integer, ConstantPool> getConstantPoolMap() {
		return constantPoolMap;
	}
	public void setConstantPoolMap(Map<Integer, ConstantPool> constantPoolMap) {
		this.constantPoolMap = constantPoolMap;
	}
	public int getAccessFlag() {
		return accessFlag;
	}
	public void setAccessFlag(int accessFlag) {
		this.accessFlag = accessFlag;
	}
	public int getThisClass() {
		return thisClass;
	}
	public void setThisClass(int thisClass) {
		this.thisClass = thisClass;
	}
	public int getSuperClass() {
		return superClass;
	}
	public void setSuperClass(int superClass) {
		this.superClass = superClass;
	}
	public int getInterfacesCount() {
		return interfacesCount;
	}
	public void setInterfacesCount(int interfacesCount) {
		this.interfacesCount = interfacesCount;
	}
	public int[] getInterfaces() {
		return interfaces;
	}
	public void setInterfaces(int[] interfaces) {
		this.interfaces = interfaces;
	}
	public int getFieldsCount() {
		return fieldsCount;
	}
	public void setFieldsCount(int fieldsCount) {
		this.fieldsCount = fieldsCount;
	}
	public FieldInfo[] getFields() {
		return fields;
	}
	public void setFields(FieldInfo[] fields) {
		this.fields = fields;
	}
	public int getMethodsCount() {
		return methodsCount;
	}
	public void setMethodsCount(int methodsCount) {
		this.methodsCount = methodsCount;
	}
	public MethodInfo[] getMethods() {
		return methods;
	}
	public void setMethods(MethodInfo[] methods) {
		this.methods = methods;
	}
	public int getAttributesCount() {
		return attributesCount;
	}
	public void setAttributesCount(int attributesCount) {
		this.attributesCount = attributesCount;
	}
	public Attribute[] getAttributes() {
		return attributes;
	}
	public void setAttributes(Attribute[] attributes) {
		this.attributes = attributes;
	}

}
