package jp.mytools.relationsearch.constantpool.exceptions;

public class ConstantPoolException extends Exception {

	private static final long serialVersionUID = 1L;

	public ConstantPoolException() {
		super();
	}

	public ConstantPoolException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ConstantPoolException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConstantPoolException(String message) {
		super(message);
	}

	public ConstantPoolException(Throwable cause) {
		super(cause);
	}

}
