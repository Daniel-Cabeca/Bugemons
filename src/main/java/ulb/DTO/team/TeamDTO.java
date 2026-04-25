package ulb.DTO.team;

import ulb.DTO.bugemon.BugemonDTO;

import java.io.Serializable;
import java.util.List;

public class TeamDTO implements Serializable {
    private String teamName;
    private List<BugemonDTO> members;

    public TeamDTO(String teamName, List<BugemonDTO> members) {
        this.teamName = teamName;
        this.members = members;
    }
    public String getTeamName() {
        return teamName;
    }
    public void setTeamName(String teamName) {}
    public List<BugemonDTO> getMembers() {
        return members;
    }
    public void setMembers(List<BugemonDTO> members) {
        this.members = members;
    }
}
