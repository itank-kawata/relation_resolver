package jp.mytools.disassemble.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.mytools.disassemble.classfile.beans.ClassFileInfo;
import jp.mytools.disassemble.classfile.exceptions.ClassFileFormatException;
import jp.mytools.disassemble.classfile.logic.ClassFileLogic;
import jp.mytools.disassemble.constantpool.exceptions.ConstantPoolException;

public class DisassembleService {

	private static final Logger logger = LoggerFactory.getLogger(DisassembleService.class);
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
					logger.info("[START] " + file.getPath());
					classInfoList.add(loadClassInfo(file));
					logger.info("[END] " + file.getPath());
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
