package ulb.server;

import java.util.ArrayList;
import java.util.List;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.DTO.team.TeamDTO;
import ulb.mapper.bugemon.BugemonMapper;
import ulb.mapper.team.TeamMapper;
import ulb.message.clientToServer.ConfirmTeamMultiMessage;
import ulb.message.clientToServer.GetSavedTeamsMessage;
import ulb.message.clientToServer.SaveTeamMessage;
import ulb.message.serverToClient.SavedTeamsMessage;
import ulb.model.Player;
import ulb.model.battle.MultiBattleSession;
import ulb.model.bugemon.Bugemon;
import ulb.model.team.Team;
import ulb.exceptions.LoadException;
import ulb.service.MultiBattleService;
import ulb.service.TeamService;

public class TeamSaveHandler {
    ClientHandler clientHandler;
    TeamService teamService;
	MultiBattleService multiBattleService;

    public TeamSaveHandler(ClientHandler clientHandler, TeamService teamService,MultiBattleService multiBattleService) {
        this.clientHandler = clientHandler;
        this.teamService = teamService;
		this.multiBattleService = multiBattleService;
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

	public void handle(ConfirmTeamMultiMessage message) {
		Player player = clientHandler.getPlayer();
		PlayerDTO opponent = message.getOpponent();
		Team team = this.makeTeam(message.getTeam());

		MultiBattleSession battle = this.multiBattleService.getMultiBattle(player.getUserId(), opponent.getUserId());
		battle.getParticipant(player.getUserId()).setTeam(team);

		if (battle.isReady()) {
			battle.start();
		}

		clientHandler.sendSuccessMessage();
	}

	/**
	 * Creates a Team instance from a list of BugemonDTO instances.
	 *
	 * @param bugemons The Bugemons of the team
	 * @return The Team instance
	 */
	private static Team makeTeam(List<BugemonDTO> bugemons) {
		List<Bugemon> entities = new ArrayList<>();
		for (BugemonDTO dto: bugemons) {
			Bugemon entity = BugemonMapper.toEntity(dto);
			entities.add(entity);
		}

		return new Team(entities);
	}

}
