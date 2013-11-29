package jp.mytools.relations.beans;

import java.util.List;

public class MethodRelationInfoBean {

	private String methodName;
	// 呼び出し先メソッド名のリスト
	private List<String> callMethodNames;
	// 呼び出し先メソッドのリスト
	private List<MethodRelationInfoBean> callMethods;
	// 呼び出し元メソッドのリスト
	private List<MethodRelationInfoBean> invokerNames;
	// 呼び出し元メソッドのリスト
	private List<MethodRelationInfoBean> invokers;

	
	public List<String> getCallMethodNames() {
		return callMethodNames;
	}
	public void setCallMethodNames(List<String> callMethodNames) {
		this.callMethodNames = callMethodNames;
	}
	public List<MethodRelationInfoBean> getInvokerNames() {
		return invokerNames;
	}
	public void setInvokerNames(List<MethodRelationInfoBean> invokerNames) {
		this.invokerNames = invokerNames;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public List<MethodRelationInfoBean> getCallMethods() {
		return callMethods;
	}
	public void setCallMethods(List<MethodRelationInfoBean> callMethods) {
		this.callMethods = callMethods;
	}
	public List<MethodRelationInfoBean> getInvokers() {
		return invokers;
	}
	public void setInvokers(List<MethodRelationInfoBean> invokers) {
		this.invokers = invokers;
	}

}
