package ulb.message.clientToServer;

import java.io.Serializable;

import ulb.server.ServerMessageHandler;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;

/**
 * Serializable message send from the client to the server.
 */
public interface ClientToServerMessage extends Serializable {
	/**
	 * Makes the handle react to the message.
	 *
	 * @param handler The message handler on the server side
	 */
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException;
}
