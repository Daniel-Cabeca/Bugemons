package ulb.message.response;

import java.io.Serializable;

public class Response implements Serializable {
	public boolean isFailure() { return !this.isSuccess(); }

	public boolean isSuccess() { return true; }
}
