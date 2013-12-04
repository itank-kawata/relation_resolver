package jp.mytools.relations.beans;

import java.util.List;
import java.util.Set;

public class MethodRelationInfoBean {

	private String methodName;
	// 呼び出し先のリスト
	private Set<String> callTargetNames;
	// 呼び出し先メソッドのリスト
	private List<MethodRelationInfoBean> callMethods;
	// 呼び出し元メソッドのリスト
	private List<MethodRelationInfoBean> invokerNames;
	// 呼び出し元メソッドのリスト
	private List<MethodRelationInfoBean> invokers;
	// インターフェースによる呼び出し元メソッドのリスト
	private List<MethodRelationInfoBean> interfaceInvokers;

	public Set<String> getCallTargetNames() {
		return callTargetNames;
	}
	public void setCallTargetNames(Set<String> callTargetNames) {
		this.callTargetNames = callTargetNames;
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
	public List<MethodRelationInfoBean> getInterfaceInvokers() {
		return interfaceInvokers;
	}
	public void setInterfaceInvokers(List<MethodRelationInfoBean> interfaceInvokers) {
		this.interfaceInvokers = interfaceInvokers;
	}

}
