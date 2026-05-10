package ulb.message.response;

public class StatusResponse extends Response {
	private final boolean isSuccess;
	private final String message;

	public StatusResponse(boolean isSuccess) {
		this(isSuccess, "");
	}

	public StatusResponse(boolean isSuccess, String message) {
		this.isSuccess = isSuccess;
		this.message = message;
	}

	public boolean isSuccess() { return this.isSuccess; }
	public boolean isFailure() { return !this.isSuccess; }
	public String getMessage() { return this.message; }
}
