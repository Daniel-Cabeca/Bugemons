package ulb.message.request.gameInfo;

import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.request.Request;
import ulb.server.ServerMessageHandler;

public class GetLevelUpInfoRequest implements Request {
    @Override
    public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
        handler.getLevelUpInfo();
    }
}
