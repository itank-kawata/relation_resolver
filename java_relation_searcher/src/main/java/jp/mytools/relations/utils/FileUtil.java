package jp.mytools.relations.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {

	private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
	
	public static List<String> readLines(File file) {

		// 一時格納用のlist
		List<String> list = new ArrayList<String>();

		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));) {

			String text;
			while ((text = br.readLine()) != null) {
				text = text.trim();

				// 空文字の時には次へ
				if ("".equals(text)) {
					continue;
				}

				// コメントアウトの時は次へ
				if (text.startsWith("#")) {
					continue;
				}

				// listにセット
				list.add(text);
			}


		} catch (Exception e) {
			logger.error("", e);
			return null;
		}
		
		return list;
	}
	
}
