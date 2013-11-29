package jp.mytools.relations.beans;

import java.util.List;

public class ClassRelationInfoBean {
	
	private String className;

	private String superClassName;
	
	private List<String> interfaceNameList;
	
	private List<FieldRelationInfoBean> fields;
	
	private List<MethodRelationInfoBean> methods;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	
	public String getSuperClassName() {
		return superClassName;
	}

	public void setSuperClassName(String superClassName) {
		this.superClassName = superClassName;
	}

	public List<String> getInterfaceNameList() {
		return interfaceNameList;
	}

	public void setInterfaceNameList(List<String> interfaceNameList) {
		this.interfaceNameList = interfaceNameList;
	}
	public List<FieldRelationInfoBean> getFields() {
		return fields;
	}

	public void setFields(List<FieldRelationInfoBean> fields) {
		this.fields = fields;
	}

	public List<MethodRelationInfoBean> getMethods() {
		return methods;
	}

	public void setMethods(List<MethodRelationInfoBean> methods) {
		this.methods = methods;
	}
	
}
