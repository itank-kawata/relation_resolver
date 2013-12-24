package jp.mytools.relations.beans;

import java.util.List;
import java.util.Set;

import jp.mytools.relations.enums.FieldAccessFlag;

public class FieldRelationInfoBean {
	private Set<FieldAccessFlag> fieldAccessFlags;
	
	private String fieldName;
	// 呼び出し元メソッドのリスト
	private List<MethodRelationInfoBean> invokers;
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public List<MethodRelationInfoBean> getInvokers() {
		return invokers;
	}
	public void setInvokers(List<MethodRelationInfoBean> invokers) {
		this.invokers = invokers;
	}
	public Set<FieldAccessFlag> getFieldAccessFlags() {
		return fieldAccessFlags;
	}
	public void setFieldAccessFlags(Set<FieldAccessFlag> fieldAccessFlags) {
		this.fieldAccessFlags = fieldAccessFlags;
	}
	
	
}
