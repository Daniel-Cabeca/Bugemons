package ulb.message.clientToServer;

import ulb.DTO.bugemon.BugemonDTO;

import ulb.Server.ServerMessageHandler;
import ulb.message.ClientToServerMessage;

public class SwapBugemonMessage implements ClientToServerMessage{
    private BugemonDTO bugemonToSwap;

    public SwapBugemonMessage(BugemonDTO bugemonToSwap){
        this.bugemonToSwap = bugemonToSwap;
    }

    @Override
	public void dispatch(ServerMessageHandler handler) {
		handler.handle(this);
	}

    public BugemonDTO getBugemonToSwap(){return this.bugemonToSwap;}
}
