package ulb.server;

import java.util.ArrayList;
import java.util.List;

import ulb.DTO.team.TeamDTO;
import ulb.mapper.team.TeamMapper;
import ulb.message.clientToServer.GetSavedTeamsMessage;
import ulb.message.clientToServer.SaveTeamMessage;
import ulb.message.serverToClient.SavedTeamsMessage;
import ulb.model.Player;
import ulb.model.bugemon.Bugemon;
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

	public void handle(GetSavedTeamsMessage message) {
		Player player = clientHandler.getPlayer();

		List<TeamDTO> DTOTeams = new ArrayList<>();

		for (Team team : teamService.getAllTeams(player.getUsername())){
			DTOTeams.add(TeamMapper.toDTO(team));
		}
		clientHandler.sendMessage(new SavedTeamsMessage(DTOTeams));
	}

	public void handle(SaveTeamMessage message) {
        Player player = clientHandler.getPlayer();
        
		TeamDTO teamDTO = message.getTeam();
		Team team = TeamMapper.toEntity(teamDTO);

		try {
			if (teamService.teamExists(team.getTeamName(), player.getUsername())) {
				clientHandler.sendErrorMessage("A team with this name already exists.");
				return;
			}

			teamService.insertTeam(player.getUsername(), team);
			clientHandler.sendSuccessMessage();

		} catch (LoadException e) {
			clientHandler.sendErrorMessage(e.getMessage());
		}
	}

}
