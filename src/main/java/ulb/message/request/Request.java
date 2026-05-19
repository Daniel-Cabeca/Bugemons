package ulb.message.request;

import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.server.ServerMessageHandler;

import java.io.Serializable;

/**
 * Serializable message send from the client to the server.
 */
public interface Request extends Serializable {
	/**
	 * Makes the handle react to the message.
	 *
	 * @param handler The message handler on the server side
	 */
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException;
}
