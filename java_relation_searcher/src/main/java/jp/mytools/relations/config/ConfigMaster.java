package jp.mytools.relations.config;

import java.util.ResourceBundle;

public class ConfigMaster {

	static {
		ResourceBundle rb = ResourceBundle.getBundle("config");
		targetPackage = rb.getString("targetPackage");
	}

	private static String targetPackage;

	public static String getTargetPackage() {
		return targetPackage;
	}

}
