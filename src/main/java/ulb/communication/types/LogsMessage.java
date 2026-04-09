package ulb.communication.types;

import java.util.List;

import ulb.communication.Message;
import ulb.communication.MessageType;
import ulb.controller.GameController;

public class LogsMessage implements Message {
    private List<Integer> HpsAfterFirstAction;
    private List<String> logs;

    public LogsMessage(List<Integer> HpsAfterFirstAction, List<String> logs){
        this.logs = logs;
    } 

    @Override
    public MessageType getMessageType() {
        return MessageType.LOGS;
    }

    @Override
    public Message handle(GameController controller) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<Integer> getHpsAfterFirstAction(){return this.HpsAfterFirstAction;}
    public List<String> getLogs(){return this.logs;}
}
