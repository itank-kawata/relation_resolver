package jp.mytools.relations.resolver;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.mytools.disassemble.classfile.beans.ClassFileInfo;
import jp.mytools.disassemble.service.DisassembleService;
import jp.mytools.relations.beans.ClassRelationInfoBean;
import jp.mytools.relations.beans.MethodRelationInfoBean;
import jp.mytools.relations.config.ConfigMaster;
import jp.mytools.relations.dao.ClassInfoDao;
import jp.mytools.relations.dto.RelationResolverServiceResultDto;
import jp.mytools.relations.service.RelationResolveService;
import jp.mytools.relations.utils.FileUtil;

public class RootClassInvokerResolver implements RelationResolver {

	private static String rootClassListFilePath;
	private static Logger logger = LoggerFactory.getLogger(RootClassInvokerResolver.class);
	private static Logger resultLogger = LoggerFactory.getLogger("RESULT");
	private static Logger relationalClassLogger = LoggerFactory.getLogger("RELATIONAL_CLASS");
	
	
	private static Set<String> relationClassSet = new HashSet<>();
	
	static {
		ResourceBundle rb = ResourceBundle.getBundle("rootClassInvokerResolver");
		rootClassListFilePath = rb.getString("rootClassListFilePath");
	}
	
	@Override
	public void resolve() {

		// ファイルの存在チェック
		File file = new File(rootClassListFilePath);
		if (file.exists() == false) {
			logger.error("file not exist:" + rootClassListFilePath);
			return;
		}

		// 一時格納用のlist
		List<String> list = FileUtil.readLines(file);

		if (list == null || list.size() < 1) {
			logger.warn("[No Root Class Settings] " + rootClassListFilePath);
			return;
		}

		DisassembleService disassembler = new DisassembleService();
		RelationResolveService relationResolveService = new RelationResolveService();
		ClassInfoDao dao = new ClassInfoDao();
		
		try {
			List<ClassFileInfo> disassembleResults = null;
			if ("stored".equals(ConfigMaster.getDataRef())) {
				disassembleResults = dao.getPackageClassList(ConfigMaster.getProjectName());
			} else {
				disassembleResults = disassembler.readFolder(new File(ConfigMaster.getTargetApplicationDir()));
				dao.deletePackageClassList(ConfigMaster.getProjectName());
				dao.savePackageClassList(ConfigMaster.getProjectName(), disassembleResults);
			}
			RelationResolverServiceResultDto resultDto = relationResolveService.resolve(disassembleResults);
			Map<String ,ClassRelationInfoBean> relationResolveResults = resultDto.getPackageClassMap();
			
			for (String rootClassName : list) {
				ClassRelationInfoBean rootClass = relationResolveResults.get(rootClassName);
				if (rootClass == null) {
					logger.warn("[Not Found RootClass : " + rootClassName);
					continue;
				}

				if (rootClass.getMethods() == null || rootClass.getMethods().size() < 1) {
					resultLogger.info("[NO METHOD] " + rootClass.getClassName());
					return;
				}
				
				for (MethodRelationInfoBean method : rootClass.getMethods()) {
					traceInvokerMethod(method, 0 , resultDto);
				}
				
			}
			
			for (String relationClasse : relationClassSet) {
				relationalClassLogger.info(relationClasse);
			}
			
		} catch (Exception e) {
			logger.error("",e);
		}
	}

	
	private void traceInvokerMethod(MethodRelationInfoBean target,int indentCnt,RelationResolverServiceResultDto result) throws Exception {

		String[] classAndMethodName = target.getMethodName().split("#");
		if (classAndMethodName.length != 2) {
			throw new Exception("Illegal Method Name" + target.getMethodName());
		}
		// 呼び出しのあるクラス一覧にセット
		relationClassSet.add(classAndMethodName[0]);
		
		if (indentCnt == 0) {
			resultLogger.info("--------------------------------------------------------------");
		}
		
		String indentStr = getIndentStr(indentCnt);

		// メソッドのクラス名とを出力
		resultLogger.info(indentStr + "|_" + classAndMethodName[0]);
		resultLogger.info(indentStr + "\t#" + classAndMethodName[1]);
		if (target.getInvokers() != null) {
			for (MethodRelationInfoBean method : target.getInvokers()) {
				traceInvokerMethod(method,indentCnt+ 1,result);
			}
		}
	}
	
	private static String getIndentStr(int indentCnt) {
		String indentStr = "";
		for (int i = 0 ; i < indentCnt ; i++) {
			indentStr += "\t";
		}
		
		return indentStr;
	}	
	
	
	
}
