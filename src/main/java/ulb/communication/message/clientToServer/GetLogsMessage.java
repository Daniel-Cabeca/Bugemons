package ulb.communication.message.clientToServer;

import ulb.communication.Message;
import ulb.communication.MessageType;
import ulb.controller.GameController;

public class GetLogsMessage implements Message {
    private boolean clearLogs;

    public GetLogsMessage(boolean clearLogs){
        this.clearLogs = clearLogs;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.GET_LOGS;
    }

    @Override
    public Message handle(GameController controller) {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean clearLogs(){return this.clearLogs;}
}
