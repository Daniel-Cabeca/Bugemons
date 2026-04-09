package ulb.communication.types;

import ulb.communication.Message;
import ulb.communication.MessageType;
import ulb.controller.GameController;

public class GameFinishedMessage implements Message{
    private boolean isGameFinished;

    public GameFinishedMessage(boolean isGameFinished){
        this.isGameFinished = isGameFinished;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.GAME_FINISHED;
    }

    @Override
    public Message handle(GameController controller) {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isGameFinished(){return this.isGameFinished;}
}
