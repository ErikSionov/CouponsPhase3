package app.core.exceptions;

public class CompanyServiceException extends CouponSystemException {
	
	private static final long serialVersionUID = 1L;

	public CompanyServiceException() {
		super();
	}

	public CompanyServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CompanyServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public CompanyServiceException(String message) {
		super(message);
	}

	public CompanyServiceException(Throwable cause) {
		super(cause);
	}
	
	
	
}
