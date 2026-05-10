package ulb.message.request.playerInfo;

import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.request.Request;
import ulb.server.ServerMessageHandler;

public class GetPlayerRequest implements Request {
    private final String username;

    public GetPlayerRequest(String username){
        this.username = username;
    }

    @Override
    public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException { handler.getPlayerInfo(username); }
}
