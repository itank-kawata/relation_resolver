package jp.mytools.relationsearch;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import jp.mytools.relationsearch.attributes.beans.Attribute;
import jp.mytools.relationsearch.attributes.beans.CodeAttributeInfo;
import jp.mytools.relationsearch.classfile.beans.ClassFileInfo;
import jp.mytools.relationsearch.classfile.logic.ClassFileLogic;
import jp.mytools.relationsearch.constantpool.beans.ClassConstantPool;
import jp.mytools.relationsearch.constantpool.beans.ConstantPool;
import jp.mytools.relationsearch.constantpool.beans.FieldrefConstantPool;
import jp.mytools.relationsearch.constantpool.beans.InterfaceMethodrefConstantPool;
import jp.mytools.relationsearch.constantpool.beans.MethodrefConstantPool;
import jp.mytools.relationsearch.constantpool.beans.NameAndTypeConstantPool;
import jp.mytools.relationsearch.constantpool.beans.Utf8ConstantPool;
import jp.mytools.relationsearch.methods.beans.MethodInfo;
import jp.mytools.relationsearch.opcode.beans.InvokeInterfaceOpecode;
import jp.mytools.relationsearch.opcode.beans.Opcode;
import jp.mytools.relationsearch.opcode.beans.ReferenceOpecode;
import jp.mytools.relationsearch.opcode.enums.OpcodeType;

public class DisassemblerSourceAnalyzer {
	private static String targetPackage = "jp/ameba/";
	
	/**
	 * key = クラス名 value = keyのクラスが保持しているMethodのSet
	 */
	private static Map<String, Set<CallMethod>> allMethodMap = new HashMap<>();
	/**
	 * key = 呼び出し元のクラス名#メソッド名 value = keyのメソッドから呼び出しているクラスのSet
	 */
	private static Map<String, Set<CallMethod>> methodMap = new HashMap<>();


	/**
	 * key = 呼び出されるクラス名#メソッド名 value = keyのメソッドを呼び出しているクラスのSet
	 */
	private static Map<String, Set<CallMethod>> calledMethodMap = new HashMap<>();

	/**
	 * key = インターフェースクラス名 value = 実装クラス名のSet
	 */
	private static Map<String, Set<String>> interfaceMapping = new HashMap<>();

	private static Set<String> usedMethodSet = new HashSet<>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String dirPath = "/Users/kawata_yusuke/Documents/workspace/14_update_status/target/classes";
//		String dirPath = "/Users/kawata_yusuke/Documents/workspace/16_automatic_rating/target/classes";
		// String dirPath =
		// "/Users/kawata_yusuke/Development/eclipse_workspace/ameba_pr_admin-4857/build/classes";
		DisassemblerSourceAnalyzer executor = new DisassemblerSourceAnalyzer();
		try {
			executor.readFolder(new File(dirPath));
		} catch (Exception e) {
			e.printStackTrace();
		}

		executor.resolveImpl();

		System.out.println("-----------------------------------------");
		for (Entry<String, Set<CallMethod>> target : allMethodMap.entrySet()) {
			// クラス名
			System.out.println(target.getKey());
			
			Set<CallMethod> methods = target.getValue();
			for (CallMethod method : methods) {
				System.out.println("\t" + method.getMethodName() + method.getMethodDescriptor());
				if (usedMethodSet.contains(method.getFullMethodName()) == false) {
					System.out.println("\t\t[No relation]");
					continue;
				}
				Set<CallMethod> callMethods = calledMethodMap.get(method.getFullMethodName());
				if (callMethods == null || callMethods.size() < 1) {
					System.out.println("\t\t[Call Interface]");
				} else {
					for (CallMethod callMethod : callMethods) {
						System.out.println("\t\t" + callMethod.getFullMethodName());
					}
				}
			}
		}
	}
	
	private void getCalledMethods(String targetMethod, int nestLevel) {
		// 呼び出されているクラス一覧
		for (Entry<String, Set<CallMethod>> entry : calledMethodMap.entrySet()) {
			if (entry.getKey().equals(targetMethod)) {
				for (CallMethod callMethod : entry.getValue()) {
					int i = 0;
					String nestedTab = "";
					while (i < (nestLevel + 1)) {
						nestedTab += "\t";
						i++;
					}
					System.out.println(nestedTab + callMethod.getFullMethodName());
					getCalledMethods(targetMethod, (nestLevel + 1));
				}
			}
		}
	}

	private void resolveImpl() {
		// インターフェースと実装クラスの紐付けを行う
		for (Entry<String, Set<CallMethod>> entry : methodMap.entrySet()) {
			System.out.println(entry.getKey());
			// 呼び出しているメソッド一覧
			for (CallMethod calledMethod : entry.getValue()) {
				usedMethodSet.add(calledMethod.getFullMethodName());
				// 呼び出し先がインターフェースの場合
				if (calledMethod.getOpType() == OpcodeType.INVOKEINTERFACE) {
					try {
						String implClassName = getImplClassName(calledMethod.getClassName());
						calledMethod.setImplClassName(implClassName);
						usedMethodSet.add(implClassName + "#" + calledMethod.getMethodName() + calledMethod.getMethodDescriptor());
					} catch (Exception e) {
						// インターフェースが決定出来なかった場合
						e.printStackTrace();
						return;
					}
				}

				System.out.println("\t" + calledMethod.getFullMethodName());
				if (calledMethod.getImplClassName() != null) {
					System.out.println("\t\t" + calledMethod.getImplClassName());
				}
			}
		}
	}

	private String getImplClassName(String interfaceClassName) throws Exception {
		String implClassName = null;
		// 実装クラスのリストを取得
		Set<String> implClasses = interfaceMapping.get(interfaceClassName);
		if (implClasses == null) {
			throw new Exception("can not determine implClass. implClasses = null" + " , interfaceClassName = " + interfaceClassName);
		}
		if (implClasses.size() == 1) {
			// 実装クラスが１つしかない場合は確定
			for (String implClass : implClasses) {
				implClassName = implClass;
			}
		} else {
			throw new Exception("can not determine implClass. implClasses.size() = " + implClasses.size() + " , interfaceClassName = " + interfaceClassName);
		}

		return implClassName;
	}

	/**
	 * ディレクトリを再帰的に読む
	 * 
	 * @param folderPath
	 * @throws Exception
	 */
	public void readFolder(File dir) throws Exception {
		File[] files = dir.listFiles();
		if (files == null)
			return;
		for (File file : files) {
			if (!file.exists())
				continue;
			else if (file.isDirectory())
				readFolder(file);
			else if (file.isFile()) {

				if ("class".equals(getSuffix(file.getName()))) {
					System.out.println("class : " + file.getName());
					System.out.println("[START] " + file.getAbsolutePath());

					ClassFileInfo result = execute(file.getAbsolutePath());

					Map<Integer, ConstantPool> cpMap = result.getConstantPoolMap();
					ClassConstantPool classInfo = (ClassConstantPool) cpMap.get(result.getThisClass());
					String className = ((Utf8ConstantPool) cpMap.get(classInfo.getNameIndex())).getValue();

					// インターフェースが存在する場合は実装クラスとのマッピングに追加
					if (result.getInterfacesCount() > 0) {
						for (int interfaceIndex : result.getInterfaces()) {
							String interfaceName = ((Utf8ConstantPool) cpMap.get(((ClassConstantPool) cpMap.get(interfaceIndex)).getNameIndex())).getValue();
							Set<String> implementClasses = interfaceMapping.get(interfaceName);
							if (implementClasses == null) {
								implementClasses = new HashSet<>();
							}
							implementClasses.add(className);
							interfaceMapping.put(interfaceName, implementClasses);
						}
					}

					// メソッド
					MethodInfo methods[] = result.getMethods();
					if (methods != null) {
						for (MethodInfo method : methods) {
							CallMethod targetMethod = new CallMethod();
							targetMethod.setClassName(className);
							targetMethod.setMethodName(((Utf8ConstantPool) cpMap.get(method.getNameIndex())).getValue());
							targetMethod.setMethodDescriptor(((Utf8ConstantPool) cpMap.get(method.getDescriptorIndex())).getValue());
							
							// 全メソッドマップ(クラス単位で保持)
							Set<CallMethod> allMethodsByClass = allMethodMap.get(targetMethod.getClassName());
							if (allMethodsByClass == null) {
								allMethodsByClass = new HashSet<>();
							}
							allMethodsByClass.add(targetMethod);
							allMethodMap.put(targetMethod.getClassName(), allMethodsByClass);
							
							
							if (method.getAttributes() != null) {
								for (Attribute attribute : method.getAttributes()) {
									if (attribute instanceof CodeAttributeInfo == false) {
										continue;
									}

									CodeAttributeInfo code = (CodeAttributeInfo) attribute;
									for (Opcode opCode : code.getOpcodes()) {
										
										// TODO
										if (targetMethod.getFullMethodName().indexOf("sendBatchReportMail") != -1) {
											System.out.println(opCode.getOpcodeType());
											System.out.println();
										}
										if (opCode.getOpcodeType() == OpcodeType.GETSTATIC) {
											System.out.println();
											ReferenceOpecode referenceOpecode = (ReferenceOpecode) opCode;
											if (cpMap.get(referenceOpecode.getIndex()) instanceof FieldrefConstantPool) {
												FieldrefConstantPool fieldrefCp = (FieldrefConstantPool) cpMap.get(referenceOpecode.getIndex());
												ClassConstantPool calledClassCp = (ClassConstantPool) cpMap.get(fieldrefCp.getClassIndex());
												String calledClassName = ((Utf8ConstantPool) cpMap.get(calledClassCp.getNameIndex())).getValue();
												System.out.println(calledClassName);
												int nameAndTypeIndex = fieldrefCp.getNameAndTypeIndex();
												NameAndTypeConstantPool calledMethodNameAndTypeCp = (NameAndTypeConstantPool) cpMap.get(nameAndTypeIndex);
												String calledMethodName = ((Utf8ConstantPool) cpMap.get(calledMethodNameAndTypeCp.getNameIndex())).getValue();
												System.out.println(calledMethodName);
//												callMethod.setMethodName(calledMethodName);
												String calledMethodDescriptorName = ((Utf8ConstantPool) cpMap.get(calledMethodNameAndTypeCp.getDescriptorIndex())).getValue();
												System.out.println(calledMethodDescriptorName);
//												callMethod.setMethodDescriptor(calledMethodDescriptorName);
											} else {
												MethodrefConstantPool methodrefCp = (MethodrefConstantPool) cpMap.get(referenceOpecode.getIndex());
												ClassConstantPool calledClassCp = (ClassConstantPool) cpMap.get(methodrefCp.getClassIndex());
												int nameAndTypeIndex = methodrefCp.getNameAndTypeIndex();
												NameAndTypeConstantPool calledMethodNameAndTypeCp = (NameAndTypeConstantPool) cpMap.get(nameAndTypeIndex);
												String calledMethodName = ((Utf8ConstantPool) cpMap.get(calledMethodNameAndTypeCp.getNameIndex())).getValue();
//												callMethod.setMethodName(calledMethodName);
												String calledMethodDescriptorName = ((Utf8ConstantPool) cpMap.get(calledMethodNameAndTypeCp.getDescriptorIndex())).getValue();
//												callMethod.setMethodDescriptor(calledMethodDescriptorName);
											}
										}
										

										if (opCode.getOpcodeType() == OpcodeType.INVOKEVIRTUAL || opCode.getOpcodeType() == OpcodeType.INVOKEINTERFACE || opCode.getOpcodeType() == OpcodeType.INVOKESPECIAL || opCode.getOpcodeType() == OpcodeType.INVOKESTATIC) {
											// targetMethodが呼び出すmethod
											CallMethod callMethod = new CallMethod();
											callMethod.setOpType(opCode.getOpcodeType());
											ClassConstantPool calledClassCp = null;
											int nameAndTypeIndex = 0;
											if (opCode.getOpcodeType() == OpcodeType.INVOKEINTERFACE) {
												InvokeInterfaceOpecode interfaceOpecode = (InvokeInterfaceOpecode) opCode;
												InterfaceMethodrefConstantPool interfaceMethodrefCp = (InterfaceMethodrefConstantPool) cpMap.get(interfaceOpecode.getIndex());
												calledClassCp = (ClassConstantPool) cpMap.get(interfaceMethodrefCp.getClassIndex());
												nameAndTypeIndex = interfaceMethodrefCp.getNameAndTypeIndex();
											} else {
												ReferenceOpecode referenceOpecode = (ReferenceOpecode) opCode;
												MethodrefConstantPool methodrefCp = (MethodrefConstantPool) cpMap.get(referenceOpecode.getIndex());
												calledClassCp = (ClassConstantPool) cpMap.get(methodrefCp.getClassIndex());
												nameAndTypeIndex = methodrefCp.getNameAndTypeIndex();
											}

											String calledClassName = ((Utf8ConstantPool) cpMap.get(calledClassCp.getNameIndex())).getValue();
											if (calledClassName.startsWith(targetPackage) == false) {
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
							} else {
								System.out.println("");
							}
						}
					}

				} else {
					System.out.println("[ignore] " + file.getAbsolutePath());
				}
			}

		}
	}

	/**
	 * ファイル名から拡張子を返します。
	 * 
	 * @param fileName
	 *            ファイル名
	 * @return ファイルの拡張子
	 */
	public static String getSuffix(String fileName) {
		if (fileName == null)
			return null;
		int point = fileName.lastIndexOf(".");
		if (point != -1) {
			return fileName.substring(point + 1);
		}
		return fileName;
	}

	private ClassFileInfo execute(String targetPath) throws Exception {
		FileSystem fs = FileSystems.getDefault();
		Path path = fs.getPath(targetPath);
		byte[] bytes = Files.readAllBytes(path);
		ClassFileLogic logic = new ClassFileLogic();
		ClassFileInfo dto = logic.convert(bytes);
		return dto;
	}

}
