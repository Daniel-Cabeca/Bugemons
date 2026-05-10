package ulb.message.response.teamSave;

import ulb.DTO.team.TeamDTO;
import ulb.message.response.Response;

import java.util.List;

public class SavedTeamsResponse extends Response {
    private final List<TeamDTO> teams;

    public SavedTeamsResponse(List<TeamDTO> teams) { this.teams = teams; }

    public List<TeamDTO> getTeams() { return teams; }
}
