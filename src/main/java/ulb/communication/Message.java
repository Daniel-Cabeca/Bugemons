package ulb.communication;

import ulb.communication.types.MessageType;
import ulb.controller.GameController;

import java.io.Serializable;

public interface Message extends Serializable{
    MessageType getMessageType();

    Message handle(GameController controller);

}
