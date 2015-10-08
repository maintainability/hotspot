package hotareadetector.exception;

/**
 * This exception is thrown in case of parameter error.
 */
public class ParameterException extends Exception {
	private static final long serialVersionUID = 806472805020566509L;

	public ParameterException(String message) {
		super(message);
	}
}
