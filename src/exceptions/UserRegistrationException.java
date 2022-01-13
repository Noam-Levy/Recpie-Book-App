package exceptions;

@SuppressWarnings("serial")
public class UserRegistrationException extends Exception {
	
	private String detailMessage;
	
	public UserRegistrationException(String message) {
		this.detailMessage = message;
	}
	
	@Override
	public String getMessage() {
		return detailMessage;
	}

}
