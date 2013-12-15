package jp.mytools.disassemble.attributes.beans;

import java.io.Serializable;

public class AnnotationValue extends DefaultElementValue  implements Serializable, ElementValue {

	private static final long serialVersionUID = 4249349073573573677L;
	private Annotation annotation;

	public Annotation getAnnotation() {
		return annotation;
	}

	public void setAnnotation(Annotation annotation) {
		this.annotation = annotation;
	}
	
}
