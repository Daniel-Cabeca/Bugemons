package ulb.message.clientToServer;

import java.util.List;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class SetUpTeamMessage implements ClientToServerMessage {
    private List<BugemonDTO> team;

    public SetUpTeamMessage(List<BugemonDTO> team){
        this.team = team;
    }

    @Override
	public void dispatch(ServerMessageHandler handler) {
		handler.handle(this);
	}

    public List<BugemonDTO> getTeam(){return this.team;}
}
