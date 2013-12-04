package jp.mytools.relations;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jp.mytools.disassemble.classfile.beans.ClassFileInfo;
import jp.mytools.disassemble.service.DisassembleService;
import jp.mytools.relations.beans.ClassRelationInfoBean;
import jp.mytools.relations.beans.MethodRelationInfoBean;
import jp.mytools.relations.config.ConfigMaster;
import jp.mytools.relations.service.RelationResolveService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RelationResolver {

	protected static Logger logger = LoggerFactory.getLogger(RelationResolver.class );

	private static Logger resultLogger = LoggerFactory.getLogger("RESULT");
	
	public static void main(String[] args) {
		DisassembleService disassembler = new DisassembleService();
		RelationResolveService relationResolveService = new RelationResolveService();
		try {
			List<ClassFileInfo> disassembleResults = disassembler.readFolder(new File(ConfigMaster.getTargetApplicationDir()));
			Map<String ,ClassRelationInfoBean> relationResolveResults = relationResolveService.resolve(disassembleResults);
			for (Entry<String, ClassRelationInfoBean> result : relationResolveResults.entrySet()) {
				resultLogger.info("----------------------------------------");
				resultLogger.info("ClassName = " + result.getValue().getClassName());
				resultLogger.info("SuperClassName = " + result.getValue().getSuperClassName());
				if (result.getValue().getInterfaceNameList() != null) {
					for (String interfaceName : result.getValue().getInterfaceNameList()) {
						resultLogger.info("InterfaceName = " + interfaceName);
					}
				}

				if (result.getValue().getMethods() != null) {
					for (MethodRelationInfoBean method : result.getValue().getMethods()) {
						resultLogger.info("MethodName = " + method.getMethodName());
						if (method.getCallMethods() != null) {
							for (MethodRelationInfoBean callMethod : method.getCallMethods()) {
								resultLogger.info("\tCaller : " + callMethod.getMethodName() );
							}
						}
						
						// インターフェースでの呼び出し以外
						if (method.getInvokers() != null) {
							for (MethodRelationInfoBean invoker : method.getInvokers()) {
								resultLogger.info("\tInvoker : " + invoker.getMethodName() );
							}
						}
						
						// インターフェースでの呼び出しを探す
						if (result.getValue().getInterfaceNameList() != null) {
							for (String interfaceName : result.getValue().getInterfaceNameList()) {
								
								//インターフェース
								ClassRelationInfoBean interfaceInfo = relationResolveResults.get(interfaceName);
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
													resultLogger.info("\tInvoker : " + interfaceInvoker.getMethodName() + " << interfaceCall(" + interfaceName + ")" );
												}
											}
										}
									}
								}
							}
						}						
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
