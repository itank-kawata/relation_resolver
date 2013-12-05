package jp.mytools.relations.dto;

import java.util.Map;
import java.util.Set;

import jp.mytools.relations.beans.ClassRelationInfoBean;

public class RelationResolverServiceResultDto {

	private Map<String, ClassRelationInfoBean> packageClassMap;
	private Map<String, Set<String>> interfaceImpMap;
	public Map<String, ClassRelationInfoBean> getPackageClassMap() {
		return packageClassMap;
	}
	public void setPackageClassMap(Map<String, ClassRelationInfoBean> packageClassMap) {
		this.packageClassMap = packageClassMap;
	}
	public Map<String, Set<String>> getInterfaceImpMap() {
		return interfaceImpMap;
	}
	public void setInterfaceImpMap(Map<String, Set<String>> interfaceImpMap) {
		this.interfaceImpMap = interfaceImpMap;
	}
}
