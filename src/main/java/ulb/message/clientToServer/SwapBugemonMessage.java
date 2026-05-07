package ulb.message.clientToServer;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class SwapBugemonMessage implements ClientToServerMessage{
    private BugemonDTO bugemonToSwap;

    public SwapBugemonMessage(BugemonDTO bugemonToSwap){
        this.bugemonToSwap = bugemonToSwap;
    }

    @Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.handle(this);
	}

    public BugemonDTO getBugemonToSwap(){return this.bugemonToSwap;}
}
