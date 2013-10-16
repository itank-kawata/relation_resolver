package jp.mytools.relationsearch;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import jp.mytools.relationsearch.classfile.beans.ClassFileInfo;
import jp.mytools.relationsearch.classfile.logic.ClassFileLogic;

public class DisassemblerSourceAnalyzer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String dirPath = "/Users/kawata_yusuke/Development/eclipse_workspace/ameba_pr_admin-4857/build/classes";
		 DisassemblerSourceAnalyzer executor = new DisassemblerSourceAnalyzer();
		 try {
			executor.readFolder(new File(dirPath));
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			else if (file.isFile())
				if ("class".equals(getSuffix(file.getName()))) {
					System.out.println("[START] " + file.getAbsolutePath());
					execute(file.getAbsolutePath());
				} else {
					System.out.println("[ignore] " + file.getAbsolutePath());
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

	private void execute(String targetPath) throws Exception {
		FileSystem fs = FileSystems.getDefault();
		Path path = fs.getPath(targetPath);
		byte[] bytes = Files.readAllBytes(path);
		ClassFileLogic logic = new ClassFileLogic();
		ClassFileInfo dto = logic.convert(bytes);

	}

}
