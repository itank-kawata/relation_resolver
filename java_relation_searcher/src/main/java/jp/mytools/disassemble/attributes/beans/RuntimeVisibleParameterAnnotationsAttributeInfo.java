package jp.mytools.disassemble.attributes.beans;

public class RuntimeVisibleParameterAnnotationsAttributeInfo extends AttributeInfo {
	private int numParameters;
	private ParameterAnnotation[] parameterAnnotations;
	
	public int getNumParameters() {
		return numParameters;
	}
	public void setNumParameters(int numParameters) {
		this.numParameters = numParameters;
	}
	public ParameterAnnotation[] getParameterAnnotations() {
		return parameterAnnotations;
	}
	public void setParameterAnnotations(ParameterAnnotation[] parameterAnnotations) {
		this.parameterAnnotations = parameterAnnotations;
	}

}
