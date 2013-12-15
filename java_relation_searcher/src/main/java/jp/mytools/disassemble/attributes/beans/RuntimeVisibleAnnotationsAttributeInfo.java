package jp.mytools.disassemble.attributes.beans;

public class RuntimeVisibleAnnotationsAttributeInfo extends AttributeInfo {

	private static final long serialVersionUID = 8521808465467793710L;
	private int numAnnotations;
	private Annotation[] annotations;
	public int getNumAnnotations() {
		return numAnnotations;
	}
	public void setNumAnnotations(int numAnnotations) {
		this.numAnnotations = numAnnotations;
	}
	public Annotation[] getAnnotations() {
		return annotations;
	}
	public void setAnnotations(Annotation[] annotations) {
		this.annotations = annotations;
	}
	
}
