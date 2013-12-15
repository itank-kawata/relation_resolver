package jp.mytools.relations.config;

import java.util.ResourceBundle;

public class ConfigMaster {

	static {
		ResourceBundle rb = ResourceBundle.getBundle("config");
		targetPackage = rb.getString("targetPackage");
		targetApplicationDir = rb.getString("targetApplicationDir");
		resolverClass = rb.getString("resolverClass");
		dataRef = rb.getString("dataRef");
		projectName = rb.getString("projectName");
	}

	private static String targetPackage;
	public static String getTargetPackage() {
		return targetPackage;
	}

	private static String targetApplicationDir;
	public static String getTargetApplicationDir() {
		return targetApplicationDir;
	}

	private static String resolverClass;
	public static String getResolverClass() {
		return resolverClass;
	}

	private static String dataRef;
	public static String getDataRef() {
		return dataRef;
	}
	
	private static String projectName;
	public static String getProjectName() {
		return projectName;
	}

	
}
