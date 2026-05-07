package ulb.message.clientToServer;

import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class GetBattleRequestsMessage implements ClientToServerMessage {
    private final String username;

    public GetBattleRequestsMessage(String username) { this.username = username; }

    public String getUsername() { return username; }

    @Override
    public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException { handler.handle(this); }
}
