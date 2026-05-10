package ulb.server;

import ulb.exceptions.DataAccessException;

import java.util.ArrayList;
import java.util.List;

import ulb.DTO.team.TeamDTO;
import ulb.mapper.team.TeamMapper;
import ulb.message.response.teamSave.*;
import ulb.model.Player;
import ulb.model.team.Team;
import ulb.exceptions.LoadException;
import ulb.service.TeamService;

public class TeamSaveHandler {
    ClientHandler clientHandler;
    private final TeamService teamService;

    public TeamSaveHandler(ClientHandler clientHandler, TeamService teamService) {
        this.clientHandler = clientHandler;
        this.teamService = teamService;
    }

	public void getSavedTeams() throws DataAccessException {
		Player player = clientHandler.getPlayer();

		List<TeamDTO> DTOTeams = new ArrayList<>();

		for (Team team : teamService.getAllTeams(player)){
			DTOTeams.add(TeamMapper.toDTO(team));
		}
		clientHandler.sendMessage(new SavedTeamsResponse(DTOTeams));
	}

	public void saveTeam(TeamDTO teamDTO) throws DataAccessException {
        Player player = clientHandler.getPlayer();
        
		Team team = TeamMapper.toEntity(teamDTO);

		try {
			if (teamService.teamExists(team.getTeamName(), player)) {
				clientHandler.sendErrorMessage("A team with this name already exists.");
				return;
			}

			teamService.insertTeam(player, team);
			clientHandler.sendSuccessMessage();

		} catch (LoadException e) {
			clientHandler.sendErrorMessage(e.getMessage());
		}
	}

}
