package ulb.message.response;

import java.io.Serializable;

public class Response implements Serializable {
	public boolean isSuccess() { return true; }
	public boolean isFailure() { return !this.isSuccess(); }
}
