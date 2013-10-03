package jp.mytools.relationsearch.constantpool.utils;

import java.util.Map;

import jp.mytools.relationsearch.constantpool.beans.ConstantPool;
import jp.mytools.relationsearch.constantpool.beans.ReferenceConstantPool;

public class ReferenceConstantPoolResolver implements ConstantPoolResolver {
	public ReferenceConstantPoolResolver(ReferenceConstantPool target , Map<Integer, ConstantPool> cpMap) {
		this.target = target;
		this.cpMap = cpMap;
		target.getClassIndex();
	}
	
	private ReferenceConstantPool target;
	private Map<Integer, ConstantPool> cpMap;
	
}
