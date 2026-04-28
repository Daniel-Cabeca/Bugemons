package ulb.message.serverToClient;

import ulb.DTO.team.TeamDTO;

import java.io.Serializable;
import java.util.List;

public class SavedTeamsMessage implements Serializable {
    private List<TeamDTO> teams;

    public SavedTeamsMessage(List<TeamDTO> teams) { this.teams = teams; }

    public List<TeamDTO> getTeams() { return teams; }
}
