package ulb.message.clientToServer;

import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class GetLogsMessage implements ClientToServerMessage {
    private boolean clearLogs;

    public GetLogsMessage(boolean clearLogs){
        this.clearLogs = clearLogs;
    }

    @Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.getLogs(clearLogs);
	}
}
