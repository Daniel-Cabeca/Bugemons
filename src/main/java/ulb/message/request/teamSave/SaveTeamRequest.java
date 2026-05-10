package ulb.message.request.teamSave;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.request.Request;
import ulb.DTO.team.TeamDTO;
import ulb.server.ServerMessageHandler;

public class SaveTeamRequest implements Request {
    private final TeamDTO team;

    public SaveTeamRequest(TeamDTO team) {
        this.team = team;
    }

    @Override
    public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
        handler.saveTeam(team);
    }
}
