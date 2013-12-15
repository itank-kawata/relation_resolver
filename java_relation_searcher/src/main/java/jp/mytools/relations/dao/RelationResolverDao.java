package jp.mytools.relations.dao;

import java.util.List;

import jp.mytools.disassemble.classfile.beans.ClassFileInfo;

public interface RelationResolverDao {

	public int savePackageClassList(String packageName,List<ClassFileInfo> classInfoList) throws Exception;
	public List<ClassFileInfo> getPackageClassList(String packageName) throws Exception;
	
}
