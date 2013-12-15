package jp.mytools.relations.dao;

import java.util.ArrayList;
import java.util.List;

import jp.mytools.datastore.okuyama.OkuyamaManager;
import jp.mytools.disassemble.classfile.beans.ClassFileInfo;

public class ClassInfoDao implements RelationResolverDao {

	@Override
	public int savePackageClassList(String packageName, List<ClassFileInfo> classInfoList) throws Exception {
		String[] tagList = new String[1];
		tagList[0] = packageName;
		for (ClassFileInfo classFileInfo : classInfoList) {
			String key = OkuyamaManager.getKey();
			OkuyamaManager.add(key, classFileInfo, tagList);
		}
		
		return classInfoList.size();
	}

	@Override
	public List<ClassFileInfo> getPackageClassList(String packageName) throws Exception {
		String[] tagList = new String[1];
		tagList[0] = packageName;
		
		List<Object> result = OkuyamaManager.getByTag(tagList);
		List<ClassFileInfo> castedResult = new ArrayList<>();
		for (Object obj : result) {
			castedResult.add((ClassFileInfo) obj);
		}
		
		return castedResult;
	}
	
	public int deletePackageClassList(String packageName) throws Exception {
		String[] tagList = new String[1];
		tagList[0] = packageName;
		String[] keys = OkuyamaManager.getKeysByTag(tagList);
		if (keys == null) {
			return 0;
		}
		
		for (String key : keys) {
			OkuyamaManager.del(key);
		}
		
		return keys.length;
	}

}
