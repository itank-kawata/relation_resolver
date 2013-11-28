package jp.mytools.relationsearch.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import jp.mytools.relationsearch.classfile.beans.ClassFileInfo;
import jp.mytools.relationsearch.classfile.exceptions.ClassFileFormatException;
import jp.mytools.relationsearch.classfile.logic.ClassFileLogic;
import jp.mytools.relationsearch.constantpool.exceptions.ConstantPoolException;

public class DisassembleService {

	/**
	 * 指定したディレクトリ配下にあるクラスファイルを読み込む
	 * @param dir
	 * @return
	 * @throws Exception
	 */
	public List<ClassFileInfo> readFolder(File dir) throws Exception {
		File[] files = dir.listFiles();
		if (files == null) throw new NoSuchFileException(dir.getAbsolutePath());
		
		List<ClassFileInfo> classInfoList = new ArrayList<>();
		for (File file : files) {
			if (!file.exists()) {
				continue;
			} else if (file.isDirectory()) {
				// サブディレクトリの情報を読み込む
				List<ClassFileInfo> subDirInfoList = readFolder(file);
				classInfoList.addAll(subDirInfoList);
			} else if (file.isFile()) {
				// 拡張子が.classのものだけが対象
				if ("class".equals(getSuffix(file.getName()))) {
					classInfoList.add(loadClassInfo(file));
				}
			}
		}
		return classInfoList;
	}
	
	private ClassFileInfo loadClassInfo(File file) throws IOException, ConstantPoolException, ClassFileFormatException {
		FileSystem fs = FileSystems.getDefault();
		Path path = fs.getPath(file.getAbsolutePath());
		byte[] bytes = Files.readAllBytes(path);
		ClassFileLogic logic = new ClassFileLogic();
		return logic.convert(bytes);
	}
	
	/**
	 * ファイル名から拡張子を返します。
	 * 
	 * @param fileName
	 *            ファイル名
	 * @return ファイルの拡張子
	 */
	private String getSuffix(String fileName) {
		if (fileName == null)
			return null;
		int point = fileName.lastIndexOf(".");
		if (point != -1) {
			return fileName.substring(point + 1);
		}
		return fileName;
	}
	
	
}
