package jp.mytools.relations.config;

import java.util.ResourceBundle;

public class ConfigMaster {

	static {
		ResourceBundle rb = ResourceBundle.getBundle("config");
		targetPackage = rb.getString("targetPackage");
		targetApplicationDir = rb.getString("targetApplicationDir");
	}

	private static String targetPackage;
	public static String getTargetPackage() {
		return targetPackage;
	}

	private static String targetApplicationDir;
	public static String getTargetApplicationDir() {
		return targetApplicationDir;
	}

	
}
