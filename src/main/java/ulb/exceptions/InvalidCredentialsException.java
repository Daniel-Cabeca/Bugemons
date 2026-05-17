package ulb.exceptions;

public class InvalidCredentialsException extends Exception{
    public InvalidCredentialsException() {
		super("Wrong password");
	}
}
