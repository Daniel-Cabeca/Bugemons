package ulb.model.battle;

import ulb.model.team.Team;
import ulb.model.bugemon.Bugemon;

import java.util.Random;


public class Battle {
	private Team teamA;
	private Team teamB;
	private Bugemon activeBugemonA;
	private Bugemon activeBugemonB;

	public Battle(Team teamA, Team teamB) {
		this.teamA = teamA;
		this.teamB = teamB;
		this.activeBugemonA = this.teamA.getMembers().get(0);
		this.activeBugemonB = this.teamB.getMembers().get(0);
	}

	public Battle(Team teamA, Team teamB, Bugemon activeBugemonA, Bugemon activeBugemonB) {
		this.teamA = teamA;
		this.teamB = teamB;
		this.activeBugemonA = activeBugemonA;
		this.activeBugemonB = activeBugemonB;
	}

	public Team getTeamA() {return this.teamA;}
	public Team getTeamB() {return this.teamB;}
	public Bugemon getActiveBugemonA() {return this.activeBugemonA;}
	public Bugemon getActiveBugemonB() {return this.activeBugemonB;}

	public void setActiveBugemonA(Bugemon bugemon) { this.activeBugemonA = bugemon; }
	public void setActiveBugemonB(Bugemon bugemon) { this.activeBugemonB = bugemon; }


	public boolean isBugemonAKO() {
		   return  getActiveBugemonA().isKO();
	}

	public boolean isBugemonBKO() {
		return  getActiveBugemonB().isKO();
	}

	public boolean isTeamAKO() { // victory
		return getTeamA().checkTeamKO();
	}

	public boolean isTeamBKO() { // defeat
		return getTeamB().checkTeamKO();
	}

	public Bugemon CheckInitiave(){
		if (getActiveBugemonA().getFightStats().getInitiative() > getActiveBugemonB().getFightStats().getInitiative()){
			return getActiveBugemonA();
		}
		else if(getActiveBugemonA().getFightStats().getInitiative() == getActiveBugemonB().getFightStats().getInitiative()) {
			 Random rand = new Random();
			 int i = rand.nextInt(2);
			 if (i == 0) {
				 return getActiveBugemonA();
			 }
			 else  {
				 return getActiveBugemonB();
			}
		}
		return getActiveBugemonB();
	}

}
