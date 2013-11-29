package jp.mytools.relations.beans;

import java.util.List;

public class FieldRelationInfoBean {
	
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
	
	
}
