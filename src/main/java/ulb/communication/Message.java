package ulb.communication;

import ulb.communication.types.MessageType;

import java.io.Serializable;

public interface Message extends Serializable{

    MessageType getMessageType();

}
