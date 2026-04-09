package ulb.communication.types;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.communication.Message;
import ulb.communication.MessageType;
import ulb.controller.GameController;

public class SwapBugemon implements Message{
    private BugemonDTO bugemonToSwap;

    public SwapBugemon(BugemonDTO bugemonToSwap){
        this.bugemonToSwap = bugemonToSwap;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.SWAP_BUGEMON;
    }

    @Override
    public Message handle(GameController controller) {
        // TODO Auto-generated method stub
        return null;
    }

    public BugemonDTO getBugemonToSwap(){return this.bugemonToSwap;}
}
