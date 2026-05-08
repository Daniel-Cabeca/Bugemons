package ulb.model.battle;

import ulb.model.Player;
import ulb.model.team.Team;

public class MultiBattleParticipant {
	private final Player player;

	private boolean accepted = false;
	private boolean declined = false;
	private Team team = null;
	private Battle.ParticipantLabel participantLabel;

	public MultiBattleParticipant(Player player, Battle.ParticipantLabel participantLabel) {
		this.player = player;
		this.participantLabel = participantLabel;
	}

	public Player getPlayer() { return this.player; }
	public int getUserId() { return this.player.getUserId(); }
	public Battle.ParticipantLabel getParticipantLabel() { return this.participantLabel; }

	public boolean hasAccepted() { return this.accepted; }
	public void accept() { this.accepted = true; }

	public boolean hasDeclined() { return this.declined; }
	public void decline() { this.declined = true; }

	public boolean hasTeam() { return this.team != null; }
	public Team getTeam() { return this.team; }
	public void setTeam(Team team) { this.team = team; }
}
