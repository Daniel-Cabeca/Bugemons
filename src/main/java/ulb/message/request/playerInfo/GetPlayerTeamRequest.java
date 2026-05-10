package ulb.message.request.playerInfo;

import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.request.Request;
import ulb.server.ServerMessageHandler;

public class GetPlayerTeamRequest implements Request {
    @Override
    public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
        handler.getPlayerTeam();
    }
}
