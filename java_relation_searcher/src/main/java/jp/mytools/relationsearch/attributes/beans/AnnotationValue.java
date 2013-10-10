package jp.mytools.relationsearch.attributes.beans;

public class AnnotationValue extends DefaultElementValue implements ElementValue {
	private Annotation annotation;

	public Annotation getAnnotation() {
		return annotation;
	}

	public void setAnnotation(Annotation annotation) {
		this.annotation = annotation;
	}
	
}
