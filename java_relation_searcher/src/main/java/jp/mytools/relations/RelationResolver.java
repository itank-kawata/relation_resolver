package jp.mytools.relations;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.mytools.disassemble.classfile.beans.ClassFileInfo;
import jp.mytools.disassemble.service.DisassembleService;
import jp.mytools.relations.beans.ClassRelationInfoBean;
import jp.mytools.relations.beans.MethodRelationInfoBean;
import jp.mytools.relations.service.RelationResolveService;

public class RelationResolver {

	protected static Logger logger = LoggerFactory.getLogger(RelationResolver.class ); 
	
	public static void main(String[] args) {
		DisassembleService disassembler = new DisassembleService(); 
		RelationResolveService relationResolveService = new RelationResolveService();
		try {
			List<ClassFileInfo> disassembleResults = disassembler.readFolder(new File("/Users/kawata_yusuke/Documents/workspace/ameba_pr_admin/build/classes"));
			List<ClassRelationInfoBean> relationResolveResults = relationResolveService.resolve(disassembleResults);
			for (ClassRelationInfoBean result : relationResolveResults) {
				logger.info("----------------------------------------");
				logger.info("ClassName = " + result.getClassName());
				logger.info("SuperClassName = " + result.getSuperClassName());
				if (result.getInterfaceNameList() != null) {
					for (String interfaceName : result.getInterfaceNameList()) {
						logger.info("InterfaceName = " + interfaceName);
					}
				}
				
				if (result.getMethods() != null) {
					for (MethodRelationInfoBean method : result.getMethods()) {
						logger.info("MethodName = " + method.getMethodName());
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
