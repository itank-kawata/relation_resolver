package jp.mytools.relationsearch.attributes.beans;

public class RuntimeInvisibleAnnotationsAttributeInfo extends AttributeInfo {
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
