package ulb.message.clientToServer;

import ulb.Server.ServerMessageHandler;
import ulb.message.ClientToServerMessage;

public class GetLogsMessage implements ClientToServerMessage {
    private boolean clearLogs;

    public GetLogsMessage(boolean clearLogs){
        this.clearLogs = clearLogs;
    }

    @Override
	public void dispatch(ServerMessageHandler handler) {
		handler.handle(this);
	}

    public boolean clearLogs(){return this.clearLogs;}
}
