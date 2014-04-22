package jp.mytools.relations.service;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.mytools.disassemble.attributes.beans.Annotation;
import jp.mytools.disassemble.attributes.beans.Attribute;
import jp.mytools.disassemble.attributes.beans.CodeAttributeInfo;
import jp.mytools.disassemble.attributes.beans.ConstValue;
import jp.mytools.disassemble.attributes.beans.ElementValue;
import jp.mytools.disassemble.attributes.beans.ElementValuePair;
import jp.mytools.disassemble.attributes.beans.RuntimeVisibleAnnotationsAttributeInfo;
import jp.mytools.disassemble.classfile.beans.ClassFileInfo;
import jp.mytools.disassemble.constantpool.beans.ClassConstantPool;
import jp.mytools.disassemble.constantpool.beans.ConstantPool;
import jp.mytools.disassemble.constantpool.beans.FieldrefConstantPool;
import jp.mytools.disassemble.constantpool.beans.InterfaceMethodrefConstantPool;
import jp.mytools.disassemble.constantpool.beans.MethodrefConstantPool;
import jp.mytools.disassemble.constantpool.beans.NameAndTypeConstantPool;
import jp.mytools.disassemble.constantpool.beans.Utf8ConstantPool;
import jp.mytools.disassemble.fields.beans.FieldInfo;
import jp.mytools.disassemble.methods.beans.MethodInfo;
import jp.mytools.disassemble.opcode.beans.InvokeInterfaceOpecode;
import jp.mytools.disassemble.opcode.beans.Opcode;
import jp.mytools.disassemble.opcode.beans.ReferenceOpecode;
import jp.mytools.disassemble.opcode.enums.OpcodeType;
import jp.mytools.relations.beans.ClassRelationInfoBean;
import jp.mytools.relations.beans.FieldRelationInfoBean;
import jp.mytools.relations.beans.MethodRelationInfoBean;
import jp.mytools.relations.config.ConfigMaster;
import jp.mytools.relations.dto.RelationResolverServiceResultDto;
import jp.mytools.relations.enums.FieldAccessFlag;

public class RelationResolveService {

	private static final Logger logger = LoggerFactory.getLogger(RelationResolveService.class);

	public RelationResolverServiceResultDto resolve(List<ClassFileInfo> classFileInfoList) throws Exception {
		RelationResolverServiceResultDto result = new RelationResolverServiceResultDto();
		Map<String, ClassRelationInfoBean> packageClassMap = new HashMap<String, ClassRelationInfoBean>();
		Map<String, Set<String>> interfaceMap = new HashMap<>();
		// 全て関連付け用の型にコンバートする
		for (ClassFileInfo classFileInfo : classFileInfoList) {
			ClassRelationInfoBean classRelationInfoBean = convert(classFileInfo);

			packageClassMap.put(classRelationInfoBean.getClassName(), classRelationInfoBean);

			// インターフェースと実装クラスのマッピングを作成する
			if (classRelationInfoBean.getInterfaceNameList() != null) {
				for (String interfaceName : classRelationInfoBean.getInterfaceNameList()) {
					if (interfaceName.indexOf(ConfigMaster.getTargetPackage()) < 0) {
						continue;
					}

					Set<String> implClassSet = interfaceMap.get(interfaceName);
					if (implClassSet == null) {
						implClassSet = new HashSet<>();
					}

					implClassSet.add(classRelationInfoBean.getClassName());
					interfaceMap.put(interfaceName, implClassSet);
				}
			}
		}
		result.setInterfaceImpMap(interfaceMap);

		// 呼び出し先と呼び出され元を解決する
		for (Entry<String, ClassRelationInfoBean> entry : packageClassMap.entrySet()) {
			ClassRelationInfoBean target = entry.getValue();

			if (target.getMethods() == null) {
				logger.info("[No methods] " + target.getClassName());
				continue;
			}

			for (MethodRelationInfoBean invokerMethod : target.getMethods()) {

				Set<String> callMethodNames = invokerMethod.getCallTargetNames();
				for (String callMethodName : callMethodNames) {
					boolean isMatch = false;

					String[] classNameAndMethodName = callMethodName.split("#");
					if (classNameAndMethodName.length != 2) {
						throw new Exception("[Illegal callMethodName] callMethodName = " + callMethodName);
					}
					ClassRelationInfoBean callClassInfo = packageClassMap.get(classNameAndMethodName[0]);

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
					
					if (isMatch) {
						continue;
					}
					
					// 親クラスに対象のメソッドが存在するか調査
					String superClassName = callClassInfo.getSuperClassName();
					while (true) {
						// 親クラスが対象パーケッジ内のクラスの場合
						if (StringUtils.isBlank(superClassName) == false && superClassName.indexOf(ConfigMaster.getTargetPackage()) > -1) {
							ClassRelationInfoBean superClassInfo = packageClassMap.get(superClassName);
							

							for (MethodRelationInfoBean superClassCallMethodInfo : superClassInfo.getMethods()) {
								String[] callClassMethodName = superClassCallMethodInfo.getMethodName().split("#");

								if (callClassMethodName.length != 2) {
									throw new Exception("[Illegal callMethodName] callMethodName = " + superClassCallMethodInfo.getMethodName());
								}

								if (callClassMethodName[1].equals(classNameAndMethodName[1])) {
									if (superClassCallMethodInfo.getInvokers() == null) {
										superClassCallMethodInfo.setInvokers(new ArrayList<MethodRelationInfoBean>());
									}
									// 呼び出されてるリストに追加
									superClassCallMethodInfo.getInvokers().add(invokerMethod);

									if (invokerMethod.getCallMethods() == null) {
										invokerMethod.setCallMethods(new ArrayList<MethodRelationInfoBean>());
									}
									// 呼び出してる先リストに追加
									invokerMethod.getCallMethods().add(superClassCallMethodInfo);

									isMatch = true;
									break;
								}
							}
							
							if (isMatch) {
								logger.info("[Find in SuperClass] superClass = superClassName , method = " + callMethodName);
								break;
							}
							// さらに上位の親クラスを検索
							superClassName = superClassInfo.getSuperClassName();
							continue;
						}
						
						break;
					}
					
					if (isMatch == false) {
						logger.warn("[Not found] invokerMethod = " + invokerMethod.getMethodName());
					}
				}
			}
		}

		// インターフェースでの呼び出しを解決する
		for (Entry<String, ClassRelationInfoBean> entry : packageClassMap.entrySet()) {

			if (entry.getValue().getMethods() == null) {
				logger.info("[No methods] " + entry.getValue().getClassName());
				continue;
			}

			for (MethodRelationInfoBean method : entry.getValue().getMethods()) {
				// インターフェースでの呼び出しを探す
				if (entry.getValue().getInterfaceNameList() != null) {
					for (String interfaceName : entry.getValue().getInterfaceNameList()) {

						// インターフェース
						ClassRelationInfoBean interfaceInfo = packageClassMap.get(interfaceName);
						if (interfaceInfo == null) {
							logger.warn("[Not found Interface] " + interfaceName);
							continue;
						}

						if (interfaceInfo.getMethods() != null) {
							for (MethodRelationInfoBean interfaceMethod : interfaceInfo.getMethods()) {

								String[] interfaceClassMethodName = interfaceMethod.getMethodName().split("#");
								if (interfaceClassMethodName.length != 2) {
									throw new Exception("Illegal methodName : " + interfaceMethod.getMethodName());
								}

								String[] classAndMethodName = method.getMethodName().split("#");
								if (classAndMethodName.length != 2) {
									throw new Exception("Illegal methodName : " + method.getMethodName());
								}

								if (interfaceClassMethodName[1].equals(classAndMethodName[1])) {
									if (interfaceMethod.getInvokers() != null) {
										for (MethodRelationInfoBean interfaceInvoker : interfaceMethod.getInvokers()) {
											if (method.getInterfaceInvokers() == null) {
												method.setInterfaceInvokers(new ArrayList<MethodRelationInfoBean>());
											}

											method.getInterfaceInvokers().add(interfaceInvoker);
										}
									}
								}
							}
						}
					}
				}
			}

		}

		result.setPackageClassMap(packageClassMap);
		return result;
	}

	private ClassRelationInfoBean convert(ClassFileInfo classFileInfo) {
		Map<Integer, ConstantPool> cpMap = classFileInfo.getConstantPoolMap();


		// クラス情報
		ClassConstantPool classInfo = (ClassConstantPool) cpMap.get(classFileInfo.getThisClass());
		String className = ((Utf8ConstantPool) cpMap.get(classInfo.getNameIndex())).getValue();

		// TODO アノテーションの解析 しかるべき場所に移す
		if (classFileInfo.getAttributes() != null) {

			for (Attribute attr : classFileInfo.getAttributes()) {
				if (attr instanceof RuntimeVisibleAnnotationsAttributeInfo) {
					RuntimeVisibleAnnotationsAttributeInfo annotationsAttributeInfo = (RuntimeVisibleAnnotationsAttributeInfo) attr;
					if (annotationsAttributeInfo.getAnnotations() != null) {
						for (Annotation annotation : annotationsAttributeInfo.getAnnotations()) {
							
							ConstantPool cpTypeIndexValue = classFileInfo.getConstantPoolMap().get(annotation.getTypeIndex());
							String type = null;
							if (cpTypeIndexValue instanceof Utf8ConstantPool) {
								type = ((Utf8ConstantPool)cpTypeIndexValue).getValue();
							}
							boolean isTargetAnnnotation = false;
							if ("Lorg/springframework/stereotype/Controller;".equals(type)) {
								isTargetAnnnotation = true;
							} else if ("Lorg/springframework/web/bind/annotation/RequestMapping;".equals(type)) {
								// TODO @RequestMappingのvalueがnullになってて取れない.....
								isTargetAnnnotation = true;
							}
								
								
							if (isTargetAnnnotation == false) continue;

							if (annotation.getElementValuePairs() == null) {
								continue;
							}

							for (ElementValuePair elementValuePair : annotation.getElementValuePairs()) {
								int nameIndex = elementValuePair.getElementNameIndex();
								ConstantPool cpIndex = classFileInfo.getConstantPoolMap().get(nameIndex);
								if (cpIndex instanceof Utf8ConstantPool) {
									Utf8ConstantPool utfIndex = (Utf8ConstantPool) cpIndex;
									logger.info(className + " Index :" + utfIndex.getValue());
								}
								ElementValue elementValue = elementValuePair.getElementNameValue();
								if (elementValue instanceof ConstValue) {
									ConstValue constValue = (ConstValue) elementValue;
									int constValueIndex = constValue.getConstValueIndex();
									ConstantPool cpValue = classFileInfo.getConstantPoolMap().get(constValueIndex);
									if (cpValue instanceof Utf8ConstantPool) {
										Utf8ConstantPool utfValue = (Utf8ConstantPool) cpValue;
										logger.info("[AnnotaionInfo]\t" + className + "\t" + utfValue.getValue() + ".do");
									}
								}
							}
						}
					}
				}
			}
		}
		
		
		
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
		
		// フィールド情報
		FieldInfo fieldInfoList[] = classFileInfo.getFields();
		List<FieldRelationInfoBean> fields = null;
		if (fieldInfoList != null) {
			fields = new ArrayList<>();
			for (FieldInfo fieldInfo : fieldInfoList) {
				FieldRelationInfoBean fieldRelationInfoBean = new FieldRelationInfoBean();
				String fieldName = ((Utf8ConstantPool) cpMap.get(fieldInfo.getNameIndex())).getValue() + ":" + ((Utf8ConstantPool) cpMap.get(fieldInfo.getDescriptorIndex())).getValue();
				fieldRelationInfoBean.setFieldName(fieldName);
				Set<FieldAccessFlag> fieldAccessFlags = getFieldAccessFlag(fieldInfo.getAccessFlags());
				fieldRelationInfoBean.setFieldAccessFlags(fieldAccessFlags);
				fields.add(fieldRelationInfoBean);
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
						if (attribute instanceof CodeAttributeInfo == false)
							continue;

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
							} else if (opCode.getOpcodeType() == OpcodeType.INVOKEVIRTUAL || opCode.getOpcodeType() == OpcodeType.INVOKEINTERFACE || opCode.getOpcodeType() == OpcodeType.INVOKESPECIAL
									|| opCode.getOpcodeType() == OpcodeType.INVOKESTATIC) {
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
		result.setFields(fields);
		result.setMethods(methods);
		return result;
	}


	private Set<FieldAccessFlag> getFieldAccessFlag(int accessFlags) {
		String bitsStr = Integer.toBinaryString(accessFlags);
		char beforeReverseBits[] = bitsStr.toCharArray();
		char[] bits = new char[beforeReverseBits.length];
		int index = 0;
		// toCharArray()で少ない桁数が前に来るようにしたいのでreverseしておく
		for(int i = beforeReverseBits.length -1 ; i>=0; i--){
			bits[index] = beforeReverseBits[i];
			index++;
		}
		Set<FieldAccessFlag> accessFlagSet = new HashSet<>();
		char bitOn = '1';
		
		if (bits.length < 1) return accessFlagSet;
		if (bits[0] == bitOn) {
			accessFlagSet.add(FieldAccessFlag.ACC_PUBLIC);
		}
		
		if (bits.length < 2) return accessFlagSet;
		if (bits[1] == bitOn) {
			accessFlagSet.add(FieldAccessFlag.ACC_PRIVATE);
		}
		
		if (bits.length < 3) return accessFlagSet;
		if (bits[2] == bitOn) {
			accessFlagSet.add(FieldAccessFlag.ACC_PROTECTED);
		}
		
		if (bits.length < 4) return accessFlagSet;
		if (bits[3] == bitOn) {
			accessFlagSet.add(FieldAccessFlag.ACC_STATIC);
		}
		
		if (bits.length < 5) return accessFlagSet;
		if (bits[4] == bitOn) {
			accessFlagSet.add(FieldAccessFlag.ACC_FINAL);
		}
		
		if (bits.length < 7) return accessFlagSet;
		if (bits[6] == bitOn) {
			accessFlagSet.add(FieldAccessFlag.ACC_VOLATILE);
		}
		
		if (bits.length < 8) return accessFlagSet;
		if (bits[7] == bitOn) {
			accessFlagSet.add(FieldAccessFlag.ACC_TRANSIENT);
		}
		
		if (bits.length < 13) return accessFlagSet;
		if (bits[12] == bitOn) {
			accessFlagSet.add(FieldAccessFlag.ACC_SYNTHETIC);
		}
		
		if (bits.length < 15) return accessFlagSet;
		if (bits[14] == bitOn) {
			accessFlagSet.add(FieldAccessFlag.ACC_ENUM);
		}
		
		return accessFlagSet;
	}
	
}
