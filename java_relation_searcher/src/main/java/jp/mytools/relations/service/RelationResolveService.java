package jp.mytools.relations.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import jp.mytools.relations.config.ConfigMaster;

public class RelationResolveService {
	
	private static final Logger logger = LoggerFactory.getLogger(RelationResolveService.class);

	public Map<String, ClassRelationInfoBean> resolve(List<ClassFileInfo> classFileInfoList) throws Exception {

		Map<String, ClassRelationInfoBean> result = new HashMap<String, ClassRelationInfoBean>();
		
		// 全て関連付け用の型にコンバートする
		for (ClassFileInfo classFileInfo : classFileInfoList) {
			ClassRelationInfoBean classRelationInfoBean = convert(classFileInfo);
			result.put(classRelationInfoBean.getClassName(), classRelationInfoBean);
		}
		
		// 呼び出し先と呼び出され元を解決する
		for (Entry<String, ClassRelationInfoBean> entry : result.entrySet()) {
			ClassRelationInfoBean target = entry.getValue();
			if (target.getMethods() == null) {
				logger.info("[No methods] " + target.getClassName());
				continue;
			}
			
			for (MethodRelationInfoBean invokerMethod : target.getMethods()) {
				boolean isMatch = false;
				Set<String> callMethodNames = invokerMethod.getCallTargetNames();
				for (String callMethodName : callMethodNames) {
					String[] classNameAndMethodName = callMethodName.split("#");
					if (classNameAndMethodName.length != 2) {
						throw new Exception("[Illegal callMethodName] callMethodName = " + callMethodName);
					}
					ClassRelationInfoBean callClassInfo = result.get(classNameAndMethodName[0]);
					
					if (callClassInfo == null) {
						logger.warn("[Not found callClassInfo] " + classNameAndMethodName[0]);
						continue;
					}
					for (MethodRelationInfoBean callMethodInfo : callClassInfo.getMethods()) {
						String[] callClassMethodName = callMethodInfo.getMethodName().split("#");
	
						if (callClassMethodName.length != 2) {
							throw new Exception("[Illegal callMethodName] callMethodName = " + callMethodName);
						}

						if (callClassMethodName[1].equals(classNameAndMethodName[1])) {
							if (callMethodInfo.getInvokers() == null) {
								callMethodInfo.setInvokers(new ArrayList<MethodRelationInfoBean>());
							}
							// 呼び出されてるリストに追加
							callMethodInfo.getInvokers().add(invokerMethod);
							
							if (invokerMethod.getCallMethods() == null) {
								invokerMethod.setCallMethods(new ArrayList<MethodRelationInfoBean>());
							}
							// 呼び出してる先リストに追加
							invokerMethod.getCallMethods().add(callMethodInfo);

							isMatch = true;
							break;
						}
					}
					
					if (isMatch == false) {
						logger.warn("[Not found] invokerMethod = " + invokerMethod.getMethodName());
					}
				}
			}
		}
		
		
		return result;
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
				methodRelationInfoBean.setCallTargetNames(new HashSet<String>());
				String methodName = ((Utf8ConstantPool) cpMap.get(methodInfo.getNameIndex())).getValue() + ((Utf8ConstantPool) cpMap.get(methodInfo.getDescriptorIndex())).getValue();
				methodRelationInfoBean.setMethodName(className + "#" + methodName);
				// メソッドが呼び出しているメソッドを抽出
				if (methodInfo.getAttributes() != null) {

					for (Attribute attribute : methodInfo.getAttributes()) {
						// CodeAttributeのみ精査する
						if (attribute instanceof CodeAttributeInfo == false) continue;

						CodeAttributeInfo code = (CodeAttributeInfo) attribute;
						for (Opcode opCode : code.getOpcodes()) {

							String callClassName = null;
							String callMethodName = null;
							String callMethodDescriptorName = null;
							
							// staicフィールドまたはメソッドの呼び出しの場合
							if (opCode.getOpcodeType() == OpcodeType.GETSTATIC) {
								ReferenceOpecode referenceOpecode = (ReferenceOpecode) opCode;
								if (cpMap.get(referenceOpecode.getIndex()) instanceof FieldrefConstantPool) {
									// フィールド
									FieldrefConstantPool fieldrefCp = (FieldrefConstantPool) cpMap.get(referenceOpecode.getIndex());
									ClassConstantPool callClassCp = (ClassConstantPool) cpMap.get(fieldrefCp.getClassIndex());
									callClassName = ((Utf8ConstantPool) cpMap.get(callClassCp.getNameIndex())).getValue();
									NameAndTypeConstantPool calledMethodNameAndTypeCp = (NameAndTypeConstantPool) cpMap.get(fieldrefCp.getNameAndTypeIndex());
									callMethodName = ((Utf8ConstantPool) cpMap.get(calledMethodNameAndTypeCp.getNameIndex())).getValue();
									callMethodDescriptorName = ((Utf8ConstantPool) cpMap.get(calledMethodNameAndTypeCp.getDescriptorIndex())).getValue();
								} else {
									// メソッド
									MethodrefConstantPool methodrefCp = (MethodrefConstantPool) cpMap.get(referenceOpecode.getIndex());
									ClassConstantPool callClassCp = (ClassConstantPool) cpMap.get(methodrefCp.getClassIndex());
									callClassName = ((Utf8ConstantPool) cpMap.get(callClassCp.getNameIndex())).getValue();
									NameAndTypeConstantPool callMethodNameAndTypeCp = (NameAndTypeConstantPool) cpMap.get(methodrefCp.getNameAndTypeIndex());
									callMethodName = ((Utf8ConstantPool) cpMap.get(callMethodNameAndTypeCp.getNameIndex())).getValue();
									callMethodDescriptorName = ((Utf8ConstantPool) cpMap.get(callMethodNameAndTypeCp.getDescriptorIndex())).getValue();
								}
							} else if (opCode.getOpcodeType() == OpcodeType.INVOKEVIRTUAL || opCode.getOpcodeType() == OpcodeType.INVOKEINTERFACE || opCode.getOpcodeType() == OpcodeType.INVOKESPECIAL || opCode.getOpcodeType() == OpcodeType.INVOKESTATIC) {
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

								callClassName = ((Utf8ConstantPool) cpMap.get(callClassCp.getNameIndex())).getValue();
								// 呼び出し先のメソッド
								NameAndTypeConstantPool callMethodNameAndTypeCp = (NameAndTypeConstantPool) cpMap.get(nameAndTypeIndex);
								callMethodName = ((Utf8ConstantPool) cpMap.get(callMethodNameAndTypeCp.getNameIndex())).getValue();
								callMethodDescriptorName = ((Utf8ConstantPool) cpMap.get(callMethodNameAndTypeCp.getDescriptorIndex())).getValue();
							} else {
								continue;
							}
							
							// 精査対象外のパッケージは無視
							if (callClassName.startsWith(ConfigMaster.getTargetPackage()) == false) {
								logger.debug("[ignore] out of package. callClassName = " + callClassName);
								continue;
							}
							methodRelationInfoBean.getCallTargetNames().add(callClassName + "#" + callMethodName + callMethodDescriptorName);
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
