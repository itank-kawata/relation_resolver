package jp.mytools.relations.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.mytools.disassemble.CallMethod;
import jp.mytools.disassemble.attributes.beans.Attribute;
import jp.mytools.disassemble.attributes.beans.CodeAttributeInfo;
import jp.mytools.disassemble.classfile.beans.ClassFileInfo;
import jp.mytools.disassemble.constantpool.beans.ClassConstantPool;
import jp.mytools.disassemble.constantpool.beans.ConstantPool;
import jp.mytools.disassemble.constantpool.beans.FieldrefConstantPool;
import jp.mytools.disassemble.constantpool.beans.InterfaceMethodrefConstantPool;
import jp.mytools.disassemble.constantpool.beans.MethodrefConstantPool;
import jp.mytools.disassemble.constantpool.beans.NameAndTypeConstantPool;
import jp.mytools.disassemble.constantpool.beans.Utf8ConstantPool;
import jp.mytools.disassemble.methods.beans.MethodInfo;
import jp.mytools.disassemble.opcode.beans.InvokeInterfaceOpecode;
import jp.mytools.disassemble.opcode.beans.Opcode;
import jp.mytools.disassemble.opcode.beans.ReferenceOpecode;
import jp.mytools.disassemble.opcode.enums.OpcodeType;
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
				methodRelationInfoBean.setCallTargets(new ArrayList<String>());
				String methodName = ((Utf8ConstantPool) cpMap.get(methodInfo.getNameIndex())).getValue() + ((Utf8ConstantPool) cpMap.get(methodInfo.getDescriptorIndex())).getValue();
				methodRelationInfoBean.setMethodName(methodName);
				// メソッドが呼び出しているメソッドを抽出
				if (methodInfo.getAttributes() != null) {

					for (Attribute attribute : methodInfo.getAttributes()) {
						// CodeAttributeのみ精査する
						if (attribute instanceof CodeAttributeInfo == false) continue;

						CodeAttributeInfo code = (CodeAttributeInfo) attribute;
						for (Opcode opCode : code.getOpcodes()) {
							// staicフィールドまたはメソッドの呼び出しの場合
							if (opCode.getOpcodeType() == OpcodeType.GETSTATIC) {
								ReferenceOpecode referenceOpecode = (ReferenceOpecode) opCode;
								if (cpMap.get(referenceOpecode.getIndex()) instanceof FieldrefConstantPool) {
									// フィールド
									FieldrefConstantPool fieldrefCp = (FieldrefConstantPool) cpMap.get(referenceOpecode.getIndex());
									ClassConstantPool callClassCp = (ClassConstantPool) cpMap.get(fieldrefCp.getClassIndex());
									String callClassName = ((Utf8ConstantPool) cpMap.get(callClassCp.getNameIndex())).getValue();
									NameAndTypeConstantPool calledMethodNameAndTypeCp = (NameAndTypeConstantPool) cpMap.get(fieldrefCp.getNameAndTypeIndex());
									String callMethodName = ((Utf8ConstantPool) cpMap.get(calledMethodNameAndTypeCp.getNameIndex())).getValue();
									String callMethodDescriptorName = ((Utf8ConstantPool) cpMap.get(calledMethodNameAndTypeCp.getDescriptorIndex())).getValue();
									methodRelationInfoBean.getCallTargets().add(callClassName + "#" + callMethodName + callMethodDescriptorName);
									continue;
								} else {
									// メソッド
									MethodrefConstantPool methodrefCp = (MethodrefConstantPool) cpMap.get(referenceOpecode.getIndex());
									ClassConstantPool callClassCp = (ClassConstantPool) cpMap.get(methodrefCp.getClassIndex());
									String callClassName = ((Utf8ConstantPool) cpMap.get(callClassCp.getNameIndex())).getValue();
									NameAndTypeConstantPool callMethodNameAndTypeCp = (NameAndTypeConstantPool) cpMap.get(methodrefCp.getNameAndTypeIndex());
									String callMethodName = ((Utf8ConstantPool) cpMap.get(callMethodNameAndTypeCp.getNameIndex())).getValue();
									String callMethodDescriptorName = ((Utf8ConstantPool) cpMap.get(callMethodNameAndTypeCp.getDescriptorIndex())).getValue();
									methodRelationInfoBean.getCallTargets().add(callClassName + "#" + callMethodName + callMethodDescriptorName);
									continue;
								}
							}
							
							
							if (opCode.getOpcodeType() == OpcodeType.INVOKEVIRTUAL || opCode.getOpcodeType() == OpcodeType.INVOKEINTERFACE || opCode.getOpcodeType() == OpcodeType.INVOKESPECIAL || opCode.getOpcodeType() == OpcodeType.INVOKESTATIC) {
								ClassConstantPool callClassCp = null;
								int nameAndTypeIndex = 0;
								if (opCode.getOpcodeType() == OpcodeType.INVOKEINTERFACE) {
									// インターフェースの呼び出し
									InvokeInterfaceOpecode interfaceOpecode = (InvokeInterfaceOpecode) opCode;
									InterfaceMethodrefConstantPool interfaceMethodrefCp = (InterfaceMethodrefConstantPool) cpMap.get(interfaceOpecode.getIndex());
									callClassCp = (ClassConstantPool) cpMap.get(interfaceMethodrefCp.getClassIndex());
									nameAndTypeIndex = interfaceMethodrefCp.getNameAndTypeIndex();
								} else {
									// インターフェース以外
									ReferenceOpecode referenceOpecode = (ReferenceOpecode) opCode;
									MethodrefConstantPool methodrefCp = (MethodrefConstantPool) cpMap.get(referenceOpecode.getIndex());
									callClassCp = (ClassConstantPool) cpMap.get(methodrefCp.getClassIndex());
									nameAndTypeIndex = methodrefCp.getNameAndTypeIndex();
								}

								String callClassName = ((Utf8ConstantPool) cpMap.get(callClassCp.getNameIndex())).getValue();
								if (callClassName.startsWith(targetPackage) == false) {
									System.out.println("[ignore] out of package. calledClassName = " + calledClassName);
									continue;
								}
								callMethod.setClassName(calledClassName);
								// 呼び出し先のメソッド
								NameAndTypeConstantPool calledMethodNameAndTypeCp = (NameAndTypeConstantPool) cpMap.get(nameAndTypeIndex);
								String calledMethodName = ((Utf8ConstantPool) cpMap.get(calledMethodNameAndTypeCp.getNameIndex())).getValue();
								callMethod.setMethodName(calledMethodName);
								String calledMethodDescriptorName = ((Utf8ConstantPool) cpMap.get(calledMethodNameAndTypeCp.getDescriptorIndex())).getValue();
								callMethod.setMethodDescriptor(calledMethodDescriptorName);

								Set<CallMethod> callMethods = methodMap.get(targetMethod.getFullMethodName());
								if (callMethods == null) {
									callMethods = new HashSet<>();
								}

								callMethods.add(callMethod);
								methodMap.put(targetMethod.getFullMethodName(), callMethods);

								// 呼び出され元のmapにも追加
								Set<CallMethod> calledMethods = calledMethodMap.get(callMethod.getFullMethodName());
								if (calledMethods == null) {
									calledMethods = new HashSet<>();
								}
								calledMethods.add(targetMethod);
								calledMethodMap.put(callMethod.getFullMethodName(), calledMethods);
							}
							
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
