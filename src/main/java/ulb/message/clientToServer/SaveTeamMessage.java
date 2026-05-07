package ulb.message.clientToServer;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;

import ulb.DTO.team.TeamDTO;
import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class SaveTeamMessage implements ClientToServerMessage {

    private TeamDTO team;

    public SaveTeamMessage(TeamDTO team) {
        this.team = team;
    }

    public TeamDTO getTeam() { return team; }

    @Override
    public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException { handler.handle(this); }
}
