package ulb.message.clientToServer;

import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class GetPlayerMessage implements ClientToServerMessage{
    private String username;

    public GetPlayerMessage(String username){
        this.username = username;
    }

    public String getUsername(){return this.username;}

    @Override
    public void dispatch(ServerMessageHandler handler) { handler.handle(this); }
}
