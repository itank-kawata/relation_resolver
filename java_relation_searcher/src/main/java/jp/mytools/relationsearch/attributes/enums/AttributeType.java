package jp.mytools.relationsearch.attributes.enums;

public enum AttributeType {

	CONSTANTVALUE("ConstantValue"),
	CODE("Code"),
	STACKMAPTABLE("StackMapTable"),
	EXCEPTIONS("Exceptions"),
	INNERCLASSES("InnerClasses"),
	ENCLOSINGMETHOD("EnclosingMethod"),
	SYNTHETIC("Synthetic"),
	SIGNATURE("Signature"),
	SOURCEFILE("SourceFile"),
	SOURCEDEBUGEXTENSION("SourceDebugExtension"),
	LINENUMBERTABLE("LineNumberTable"),
	LOCALVARIABLETABLE("LocalVariableTable"),
	LOCALVARIABLETYPETABLE("LocalVariableTypeTable"),
	DEPRECATED("Deprecated"),
	RUNTIMEVISIBLEANNOTATIONS("RuntimeVisibleAnnotations"),
	RUNTIMEINVISIBLEANNOTATIONS("RuntimeInvisibleAnnotations"),
	RUNTIMEVISIBLEPARAMETERANNOTATIONS("RuntimeVisibleParameterAnnotations"),
	RUNTIMEINVISIBLEPARAMETERANNOTATIONS("RuntimeInvisibleParameterAnnotations"),
	ANNOTATIONDEFAULT("AnnotationDefault"),
	BOOTSTRAPMETHODS("BootstrapMethods");
	
	private AttributeType(String name) {
		this.name = name;
	}
	
	private String name;
	public String getName() {
		return name;
	}

	public static AttributeType getAttributeTypeByName(String name) {
		for (AttributeType type : AttributeType.values()) {
			if (type.getName().equals(name)) return type;
		}
		
		return null;
	}
}
