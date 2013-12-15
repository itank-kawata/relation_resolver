package jp.mytools.relations.resolver;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.mytools.disassemble.classfile.beans.ClassFileInfo;
import jp.mytools.disassemble.service.DisassembleService;
import jp.mytools.relations.beans.ClassRelationInfoBean;
import jp.mytools.relations.beans.MethodRelationInfoBean;
import jp.mytools.relations.config.ConfigMaster;
import jp.mytools.relations.dao.ClassInfoDao;
import jp.mytools.relations.service.RelationResolveService;
/**
 * 呼び出しの無いクラスとメソッドを洗い出します
 * @author kawata_yusuke
 *
 */
public class NoInvokeResolver implements RelationResolver {

	private static final Logger logger = LoggerFactory.getLogger(NoInvokeResolver.class);
	private static Logger resultLogger = LoggerFactory.getLogger("RESULT");
	private static Logger noInvokerLogger = LoggerFactory.getLogger("NO_INVOKER");
	private static Logger noInvokerClassLogger = LoggerFactory.getLogger("NO_INVOKER_CLASS");
	
	@Override
	public void resolve() {
		DisassembleService disassembler = new DisassembleService();
		RelationResolveService relationResolveService = new RelationResolveService();
		try {

			List<ClassFileInfo> disassembleResults = null;
			ClassInfoDao dao = new ClassInfoDao();
			if ("stored".equals(ConfigMaster.getDataRef())) {
				disassembleResults = dao.getPackageClassList(ConfigMaster.getProjectName());
			} else {
				disassembleResults = disassembler.readFolder(new File(ConfigMaster.getTargetApplicationDir()));
				dao.deletePackageClassList(ConfigMaster.getProjectName());
				dao.savePackageClassList(ConfigMaster.getProjectName(), disassembleResults);
			}

			Map<String ,ClassRelationInfoBean> relationResolveResults = relationResolveService.resolve(disassembleResults).getPackageClassMap();
			for (Entry<String, ClassRelationInfoBean> result : relationResolveResults.entrySet()) {
//				// TODO for DEBUG
//				if (result.getValue().getClassName().indexOf("DaoImp") < 0) {
//					continue;
//				}
				resultLogger.info("----------------------------------------");
				resultLogger.info("ClassName = " + result.getValue().getClassName());
				resultLogger.info("SuperClassName = " + result.getValue().getSuperClassName());
				if (result.getValue().getInterfaceNameList() != null) {
					for (String interfaceName : result.getValue().getInterfaceNameList()) {
						resultLogger.info("InterfaceName = " + interfaceName);
					}
				}
				
				// TODO フィールドの使用有無の解決がまだ未実装だから、フィールドが使われてたら誤検知する
				boolean isUsedClass = false;
				if (result.getValue().getMethods() != null) {
					for (MethodRelationInfoBean method : result.getValue().getMethods()) {
						resultLogger.info("MethodName = " + method.getMethodName());
						if (method.getCallMethods() != null) {
							for (MethodRelationInfoBean callMethod : method.getCallMethods()) {
								resultLogger.info("\tCall : " + callMethod.getMethodName() );
							}
						}
						
						// インターフェースでの呼び出し以外
						if (method.getInvokers() != null) {
							for (MethodRelationInfoBean invoker : method.getInvokers()) {
								isUsedClass = true;
								resultLogger.info("\tInvoker : " + invoker.getMethodName() );
							}
						}
						
						// インターフェースでの呼び出し
						if (method.getInterfaceInvokers() != null) {
							for (MethodRelationInfoBean interfaceInvoker : method.getInterfaceInvokers()) {
								isUsedClass = true;
								resultLogger.info("\tInterfaceInvoker : " + interfaceInvoker.getMethodName() );
							}
						}
						
						// どこからも呼び出されてなメソッドをログに記録
						if (method.getInvokers() == null && method.getInterfaceInvokers() == null) {
							noInvokerLogger.info(method.getMethodName());
						}
					}
				}
				
				if (isUsedClass == false) {
					noInvokerClassLogger.info(result.getValue().getClassName());
				}
			}
		} catch (Exception e) {
			logger.error("",e);
		}
	}

}
