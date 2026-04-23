package ulb.mapper.team;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.team.TeamDTO;
import ulb.mapper.bugemon.BugemonMapper;
import ulb.model.bugemon.Bugemon;
import ulb.model.team.Team;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to convert regular Team to DTO Team
 */
public class TeamMapper {

    private TeamMapper() {}

    public static TeamDTO toDTO(Team entity) {
        if (entity == null) return null;

        List<BugemonDTO> members = new ArrayList<>();

        for (Bugemon b : entity.getMembers())
            members.add(BugemonMapper.toDTO(b));


        return new TeamDTO(entity.getTeamName(), members);
    }

    public static Team toEntity(TeamDTO dto) {
        if (dto == null) return null;
        List<Bugemon> members = new ArrayList<>();

        for (BugemonDTO b : dto.getMembers()) {
            members.add(BugemonMapper.toEntity(b));
        }
        Team entity = new Team(members);
        entity.setTeamName(dto.getTeamName());
        return entity;
    }

}
