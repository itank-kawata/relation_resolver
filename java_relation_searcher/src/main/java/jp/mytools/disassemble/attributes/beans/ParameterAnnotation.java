package jp.mytools.disassemble.attributes.beans;

import java.io.Serializable;

public class ParameterAnnotation implements Serializable  {

	private static final long serialVersionUID = -3014201776620714076L;
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
