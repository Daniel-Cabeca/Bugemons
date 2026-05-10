package ulb.message.serverToClient.teamSave;

import ulb.DTO.team.TeamDTO;

import java.io.Serializable;
import java.util.List;

public class SavedTeamsMessage implements Serializable {
    private final List<TeamDTO> teams;

    public SavedTeamsMessage(List<TeamDTO> teams) { this.teams = teams; }

    public List<TeamDTO> getTeams() { return teams; }
}
