package ulb.message.serverToClient;

import java.io.Serializable;

public class UserIdMessage implements Serializable {
	private final int id;

	public UserIdMessage(int id) {
		this.id = id;
	}

	public int getId() { return this.id; }
}
