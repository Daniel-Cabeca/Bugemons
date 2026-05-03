package ulb.model.battle;

import ulb.model.team.Team;

public class MultiBattleParticipant {
	private final int userId;
	private boolean accepted = false;
	private Team team = null;

	public MultiBattleParticipant(int userId) {
		this.userId = userId;
	}

	public int getUserId() { return this.userId; }

	public boolean hasAccepted() { return this.accepted; }
	public void accept() { this.accepted = true; }

	public boolean hasTeam() { return this.team != null; }
	public Team getTeam() { return this.team; }
	public void setTeam(Team team) { this.team = team; }
}
