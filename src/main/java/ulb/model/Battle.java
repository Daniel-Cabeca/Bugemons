package ulb.model;

import ulb.model.team.Team;

public class Battle {
	private Team teamPlayer;
	private Team teamEnemy;

	public Battle(Team teamPlayer, Team teamEnemy) {
		this.teamPlayer = teamPlayer;
		this.teamEnemy = teamEnemy;
	}

	public Team getTeamPlayer() {return this.teamPlayer;}
	public Team getTeamEnemy() {return this.teamEnemy;}

	
}
