package ulb.server;

import ulb.DTO.team.TeamDTO;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.LoadException;
import ulb.exceptions.UserFacingException;
import ulb.mapper.team.TeamMapper;
import ulb.message.response.teamSave.SavedTeamsResponse;
import ulb.model.Player;
import ulb.model.team.Team;
import ulb.service.TeamService;

import java.util.ArrayList;
import java.util.List;

public class TeamSaveHandler {
	private final TeamService teamService;
	ClientHandler clientHandler;

	public TeamSaveHandler(ClientHandler clientHandler, TeamService teamService) {
		this.clientHandler = clientHandler;
		this.teamService = teamService;
	}

	public void getSavedTeams() throws DataAccessException {
		Player player = clientHandler.getPlayer();

		List<TeamDTO> DTOTeams = new ArrayList<>();
		try {
			for (Team team : teamService.getAllTeams(player)) {
				DTOTeams.add(TeamMapper.toDTO(team));
			}
		} catch (Exception e) {
			throw new DataAccessException("Cannot get all saved teams");
		}

		clientHandler.sendMessage(new SavedTeamsResponse(DTOTeams));
	}

	public void saveTeam(TeamDTO teamDTO) throws UserFacingException, DataAccessException {
		Player player = clientHandler.getPlayer();

		Team team = TeamMapper.toEntity(teamDTO);

		try {
			if (teamService.teamExists(team.getTeamName(), player)) {
				throw new UserFacingException("A team with this name already exists.");
			}

			teamService.insertTeam(player, team);
			clientHandler.sendSuccessMessage();

		} catch (LoadException e) {
			throw new DataAccessException(e.getMessage());
		}
	}

}
