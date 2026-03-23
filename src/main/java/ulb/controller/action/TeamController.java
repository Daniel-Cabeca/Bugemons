package ulb.controller.action;

import ulb.model.Player;
import ulb.model.battle.Battle;
import ulb.model.bugemon.Bugemon;
import ulb.model.team.Team;

import java.util.ArrayList;
import java.util.List;

public class TeamController {
	private Player player;
	private Team team;

	public TeamController(Player player) {
		this.player = player;
	}

	public void setTeam(List<String> selectedBugemons){
		List<Bugemon> teamABugemons = new ArrayList<Bugemon>();
		for (String bugemon : selectedBugemons) {
			teamABugemons.add(new Bugemon(bugemon.toLowerCase()));
		}
		Team playerTeam = new Team(teamABugemons);
		player.setTeam(playerTeam);
	}





}
