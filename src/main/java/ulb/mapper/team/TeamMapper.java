package ulb.mapper.team;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.team.TeamDTO;
import ulb.exceptions.MappingException;
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

	/**
	 * Converts a Team entity to a DTO.
	 *
	 * @param entity The Team entity
	 * @return The corresponding DTO or null
	 */
	public static TeamDTO toDTO(Team entity) {
		if (entity == null) return null;

		List<BugemonDTO> members = new ArrayList<>();

		for (Bugemon b : entity.getMembers())
			members.add(BugemonMapper.toDTO(b));

		TeamDTO teamDTO = new TeamDTO(entity.getId(), entity.getTeamName(), members);
		return teamDTO;
	}

	/**
	 * Converts a TeamDTO to an entity.
	 *
	 * @param dto The Team DTO
	 * @return The corresponding entity or null
	 * @throws MappingException If mapping fails
	 */
	public static Team toEntity(TeamDTO dto) throws MappingException {
		if (dto == null) return null;
		List<Bugemon> members = new ArrayList<>();

		for (BugemonDTO b : dto.members()) {
			members.add(BugemonMapper.toEntity(b));
		}
		Team team = new Team(members);
		team.setTeamName(dto.teamName());
		team.setId(dto.id());
		return team;
	}

}
