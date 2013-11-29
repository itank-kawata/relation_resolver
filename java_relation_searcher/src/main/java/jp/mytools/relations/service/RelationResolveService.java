package jp.mytools.relations.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.mytools.disassemble.attributes.beans.Attribute;
import jp.mytools.disassemble.attributes.beans.CodeAttributeInfo;
import jp.mytools.disassemble.classfile.beans.ClassFileInfo;
import jp.mytools.disassemble.constantpool.beans.ClassConstantPool;
import jp.mytools.disassemble.constantpool.beans.ConstantPool;
import jp.mytools.disassemble.constantpool.beans.Utf8ConstantPool;
import jp.mytools.disassemble.methods.beans.MethodInfo;
import jp.mytools.disassemble.opcode.beans.Opcode;
import jp.mytools.relations.beans.ClassRelationInfoBean;
import jp.mytools.relations.beans.MethodRelationInfoBean;

public class RelationResolveService {

	public List<ClassRelationInfoBean> resolve(List<ClassFileInfo> classFileInfoList) {
		
		List<ClassRelationInfoBean> results = new ArrayList<>();
		
		for (ClassFileInfo classFileInfo : classFileInfoList) {
			results.add(convert(classFileInfo));
		}
		
		return results;
	}
	
	
	private ClassRelationInfoBean convert(ClassFileInfo classFileInfo) {
		Map<Integer,ConstantPool> cpMap = classFileInfo.getConstantPoolMap();
		
		// クラス情報
		ClassConstantPool classInfo = (ClassConstantPool) cpMap.get(classFileInfo.getThisClass());
		String className = ((Utf8ConstantPool) cpMap.get(classInfo.getNameIndex())).getValue();
		// 親クラス情報
		ClassConstantPool superClassInfo = (ClassConstantPool) cpMap.get(classFileInfo.getSuperClass());
		String superClassName = ((Utf8ConstantPool) cpMap.get(superClassInfo.getNameIndex())).getValue();
		// インターフェース情報
		List<String> interfaceNameList = null;
		int[] interfaces = classFileInfo.getInterfaces();
		if (interfaces != null) {
			interfaceNameList = new ArrayList<>();
			for (int interfaceNo : interfaces) {
				ClassConstantPool interfaceInfo = (ClassConstantPool) cpMap.get(interfaceNo);
				String interfaceName = ((Utf8ConstantPool) cpMap.get(interfaceInfo.getNameIndex())).getValue();
				interfaceNameList.add(interfaceName);
			}
		}
		
		// メソッド情報
		MethodInfo methodInfoList[] = classFileInfo.getMethods();
		List<MethodRelationInfoBean> methods = null;
		if (methodInfoList != null) {
			methods = new ArrayList<>();
			// クラスの持つメソッド情報
			for (MethodInfo methodInfo : methodInfoList) {
				MethodRelationInfoBean methodRelationInfoBean = new MethodRelationInfoBean();
				String methodName = ((Utf8ConstantPool) cpMap.get(methodInfo.getNameIndex())).getValue() + ((Utf8ConstantPool) cpMap.get(methodInfo.getDescriptorIndex())).getValue();
				methodRelationInfoBean.setMethodName(methodName);
				// メソッドが呼び出しているメソッドを抽出
				if (methodInfo.getAttributes() != null) {
					
					for (Attribute attribute : methodInfo.getAttributes()) {
						// CodeAttributeのみ精査する
						if (attribute instanceof CodeAttributeInfo == false) continue;
						
						CodeAttributeInfo code = (CodeAttributeInfo) attribute;
						for (Opcode opCode : code.getOpcodes()) {
							
						}
					}
				}
				
				methods.add(methodRelationInfoBean);
			}
		}
		
		ClassRelationInfoBean result = new ClassRelationInfoBean();
		result.setClassName(className);
		result.setSuperClassName(superClassName);
		result.setInterfaceNameList(interfaceNameList);
		result.setMethods(methods);
		return result;
	}
	
}
