package ulb.message.serverToClient;

import java.io.Serializable;

public class StatusMessage implements Serializable {
	private final boolean isSuccess;
	private final String message;

	public StatusMessage(boolean isSuccess) {
		this(isSuccess, "");
	}

	public StatusMessage(boolean isSuccess, String message) {
		this.isSuccess = isSuccess;
		this.message = message;
	}

	public boolean isSuccess() { return this.isSuccess; }
	public boolean isFailure() { return !this.isSuccess; }
	public String getMessage() { return this.message; }
}
