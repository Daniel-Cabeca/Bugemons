package ulb.DTO.team;

import ulb.DTO.bugemon.BugemonDTO;

import java.io.Serializable;
import java.util.List;

public class TeamDTO implements Serializable {
    private int id;
    private String teamName;
    private List<BugemonDTO> members;

    public TeamDTO(String teamName, List<BugemonDTO> members) {
        this.teamName = teamName;
        this.members = members;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTeamName() {
        return teamName;
    }
    public List<BugemonDTO> getMembers() {
        return members;
    }
}
