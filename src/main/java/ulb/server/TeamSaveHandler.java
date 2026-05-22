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

/**
 * Handles requests related to saving and retrieving player teams.
 */
public class TeamSaveHandler {
	private final TeamService teamService;
	ClientHandler clientHandler;

	/**
	 * Creates a team save handler for the given client and services.
	 *
	 * @param clientHandler the client handler owning this session
	 * @param teamService service for team persistence
	 */
	public TeamSaveHandler(ClientHandler clientHandler, TeamService teamService) {
		this.clientHandler = clientHandler;
		this.teamService = teamService;
	}

	/**
	 * Retrieves all saved teams for the currently connected player and sends them to the client.
	 *
	 * @throws DataAccessException if the saved teams cannot be retrieved
	 */
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

	/**
	 * Saves the given team for the currently connected player.
	 *
	 * @param teamDTO the team to save
	 * @throws UserFacingException if a team with the same name already exists for the player
	 * @throws DataAccessException if the team cannot be saved
	 */
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
