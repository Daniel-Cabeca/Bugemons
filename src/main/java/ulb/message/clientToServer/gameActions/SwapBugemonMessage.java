package ulb.message.clientToServer.gameActions;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.clientToServer.ClientToServerMessage;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.server.ServerMessageHandler;

public class SwapBugemonMessage implements ClientToServerMessage{
    private final BugemonDTO bugemonToSwap;

    public SwapBugemonMessage(BugemonDTO bugemonToSwap){
        this.bugemonToSwap = bugemonToSwap;
    }

    @Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.chooseSwapBugemonAction(bugemonToSwap);
	}
}
