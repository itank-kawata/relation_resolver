package jp.mytools.disassemble.attributes.beans;

public class RuntimeInvisibleAnnotationsAttributeInfo extends AttributeInfo {

	private static final long serialVersionUID = 4945761661502677043L;
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
