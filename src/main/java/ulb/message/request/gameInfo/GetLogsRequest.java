package ulb.message.request.gameInfo;

import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.request.Request;
import ulb.server.ServerMessageHandler;

public class GetLogsRequest implements Request {
	private final boolean clearLogs;

	public GetLogsRequest(boolean clearLogs) {
		this.clearLogs = clearLogs;
	}

	@Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.getLogs(clearLogs);
	}
}
