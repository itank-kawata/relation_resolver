package jp.mytools.disassemble.constantpool.utils;

import java.util.Map;

import jp.mytools.disassemble.constantpool.beans.ConstantPool;
import jp.mytools.disassemble.constantpool.beans.ReferenceConstantPool;

public class ReferenceConstantPoolResolver implements ConstantPoolResolver {
	public ReferenceConstantPoolResolver(ReferenceConstantPool target , Map<Integer, ConstantPool> cpMap) {
		this.target = target;
		this.cpMap = cpMap;
		target.getClassIndex();
	}
	
	private ReferenceConstantPool target;
	private Map<Integer, ConstantPool> cpMap;
	
}
