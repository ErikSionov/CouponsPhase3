package app.core.exceptions;

public class AdminServiceException extends CouponSystemException {

	private static final long serialVersionUID = 1L;

	public AdminServiceException() {
		super();
	}

	public AdminServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AdminServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public AdminServiceException(String message) {
		super(message);
	}

	public AdminServiceException(Throwable cause) {
		super(cause);
	}
	
	

}
