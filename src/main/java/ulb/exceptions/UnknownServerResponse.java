package ulb.exceptions;

public class UnknownServerResponse extends Exception {
	public UnknownServerResponse(String request) {
		super("Unkown server response for the request : " + request);
	}
}
