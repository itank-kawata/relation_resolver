package jp.mytools.relations.resolver;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;

import jp.mytools.disassemble.classfile.beans.ClassFileInfo;
import jp.mytools.disassemble.service.DisassembleService;
import jp.mytools.relations.beans.ClassRelationInfoBean;
import jp.mytools.relations.beans.FieldRelationInfoBean;
import jp.mytools.relations.beans.MethodRelationInfoBean;
import jp.mytools.relations.config.ConfigMaster;
import jp.mytools.relations.dao.ClassInfoDao;
import jp.mytools.relations.dto.RelationResolverServiceResultDto;
import jp.mytools.relations.enums.FieldAccessFlag;
import jp.mytools.relations.service.RelationResolveService;
import jp.mytools.relations.utils.FileUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FollowRootClassRelationResolver implements RelationResolver {

	static {
		ResourceBundle rb = ResourceBundle.getBundle("followRootClassRelationResolver");
		rootClassListFilePath = rb.getString("rootClassListFilePath");
	}

	private static Logger logger = LoggerFactory.getLogger(FollowRootClassRelationResolver.class);
	private static Logger resultLogger = LoggerFactory.getLogger("RESULT");
	private static Logger noInvokeClassLogger = LoggerFactory.getLogger("NO_INVOKER_CLASS");
	private static Logger publicFieldLogger = LoggerFactory.getLogger("PUBLIC_FIELD");
	
	private static String rootClassListFilePath;

	private static Set<String> invokedClassSet;
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
			invokedClassSet = new HashSet<>();
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
					traceCallMethod(method, 0 , resultDto);
				}
				
			}
			
			for (Entry<String, ClassRelationInfoBean> classEntry : relationResolveResults.entrySet()) {
				String invokeCheckClass = classEntry.getKey();
				if (invokeCheckClass.indexOf("AmePonApiClient")> -1) {
					System.out.println("");
				}
				ClassRelationInfoBean cl = classEntry.getValue();
				boolean isExistPublicField = false;
				if (cl.getFields() != null) {
					for (FieldRelationInfoBean field : cl.getFields()) {
						// TODO 暫定仕様:publicなフィールドを持ってたら使ってるクラスとしてみなす
						if (field.getFieldAccessFlags() != null && field.getFieldAccessFlags().contains(FieldAccessFlag.ACC_PUBLIC)) {
							publicFieldLogger.info(invokeCheckClass);
							isExistPublicField = true;
							break;
						}
					}
				}
				
				if (isExistPublicField) continue;
				
				if (invokedClassSet.contains(invokeCheckClass) == false) {
					noInvokeClassLogger.info(invokeCheckClass);
				}
			}
			
			
		} catch (Exception e) {
			logger.error("", e);
			return;
		}
	}
	
	private void traceCallMethod(MethodRelationInfoBean target,int indentCnt,RelationResolverServiceResultDto result) throws Exception {

		String[] classAndMethodName = target.getMethodName().split("#");
		if (classAndMethodName.length != 2) {
			throw new Exception("Illegal Method Name" + target.getMethodName());
		}
		// 呼び出しのあるクラス一覧にセット
		invokedClassSet.add(classAndMethodName[0]);
		
		if (indentCnt == 0) {
			resultLogger.info("--------------------------------------------------------------");
		}
		
		String indentStr = getIndentStr(indentCnt);

		Set<String> impleClassSet = result.getInterfaceImpMap().get(classAndMethodName[0]);
		if (impleClassSet == null) {
			resultLogger.info(indentStr + "|_" + classAndMethodName[0]);
			resultLogger.info(indentStr + "\t#" + classAndMethodName[1]);

			if (target.getCallMethods() != null) {
				for (MethodRelationInfoBean method : target.getCallMethods()) {
					traceCallMethod(method,indentCnt+ 1,result);
				}
			}
		} else {
			// メソッドのクラス名とを出力
			resultLogger.info(indentStr + "|_ [interface]" + classAndMethodName[0]);
			// インターフェースクラスの場合はインターフェースでの呼び出しも対象に含める
			for (String implClassName : impleClassSet) {
				ClassRelationInfoBean impleClass = result.getPackageClassMap().get(implClassName);
				if (impleClass.getMethods() == null) {
					continue;
				}
				
				for (MethodRelationInfoBean impleMethod : impleClass.getMethods()) {
					String[] impleMethodClassAndMethodName = impleMethod.getMethodName().split("#");
					if (impleMethodClassAndMethodName.length != 2) {
						throw new Exception("Illegal Method Name" + impleMethod.getMethodName());
					}
					// 実装クラスの該当メソッドを検索
					if (impleMethodClassAndMethodName[1].equals(classAndMethodName[1])) {
						resultLogger.info(indentStr + "|_ [impleClass]" + impleMethodClassAndMethodName[0]);
						resultLogger.info(indentStr + "\t#" + impleMethodClassAndMethodName[1]);
						if (impleMethod.getCallMethods() != null) {
							for (MethodRelationInfoBean impleCallMethod : impleMethod.getCallMethods()) {
								traceCallMethod(impleCallMethod,indentCnt+ 1,result);
							}
						}
						break;
					}
				}
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
