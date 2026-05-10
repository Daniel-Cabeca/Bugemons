package ulb.message.clientToServer.teamSave;
import ulb.server.ServerMessageHandler;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.clientToServer.ClientToServerMessage;

public class GetSavedTeamsMessage implements ClientToServerMessage {
    @Override
    public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
        handler.getSavedTeams();
    }
}
