package ulb.message.clientToServer.teamSave;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.clientToServer.ClientToServerMessage;
import ulb.DTO.team.TeamDTO;
import ulb.server.ServerMessageHandler;

public class SaveTeamMessage implements ClientToServerMessage {
    private final TeamDTO team;

    public SaveTeamMessage(TeamDTO team) {
        this.team = team;
    }

    @Override
    public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
        handler.saveTeam(team);
    }
}
