package ulb.model.battle;

import ulb.model.team.Team;

public class Battle {
	private Team teamA;
	private Team teamB;

	public Battle(Team teamA, Team teamB) {
		this.teamA = teamA;
		this.teamB = teamB;
	}

	public Team getTeamA() {return this.teamA;}
	public Team getTeamB() {return this.teamB;}

}
