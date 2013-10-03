package jp.mytools.relationsearch.classfile.exceptions;

public class ClassFileFormatException extends Exception {

	private static final long serialVersionUID = 1L;

	public ClassFileFormatException() {
		super();
	}

	public ClassFileFormatException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ClassFileFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	public ClassFileFormatException(String message) {
		super(message);
	}

	public ClassFileFormatException(Throwable cause) {
		super(cause);
	}

}
