package ulb.message.clientToServer.playerInfo;

import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.clientToServer.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class GetPlayerMessage implements ClientToServerMessage{
    private final String username;

    public GetPlayerMessage(String username){
        this.username = username;
    }

    @Override
    public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException { handler.getPlayerInfo(username); }
}
