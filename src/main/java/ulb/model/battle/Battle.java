package ulb.model.battle;

import ulb.model.team.Team;
import ulb.model.Bugemon;

public class Battle {
	private Team teamA;
	private Team teamB;
	private Bugemon activeBugemonA;
	private Bugemon activeBugemonB;

	public Battle(Team teamA, Team teamB) {
		this.teamA = teamA;
		this.teamB = teamB;
	}

	public Team getTeamA() {return this.teamA;}
	public Team getTeamB() {return this.teamB;}
	public Bugemon getActiveBugemonA() {return this.activeBugemonA;}
	public Bugemon getActiveBugemonB() {return this.activeBugemonB;}

}
