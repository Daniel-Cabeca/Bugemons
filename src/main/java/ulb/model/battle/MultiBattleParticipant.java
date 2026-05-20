package ulb.model.battle;

import ulb.model.Player;
import ulb.model.team.Team;

import java.util.Optional;

public class MultiBattleParticipant {
	private final Player player;

	private boolean accepted = false;
	private boolean declined = false;
	private Optional<Team> team = Optional.empty();
	private final Battle.ParticipantLabel participantLabel;

	public MultiBattleParticipant(Player player, Battle.ParticipantLabel participantLabel) {
		this.player = player;
		this.participantLabel = participantLabel;
	}

	public Player getPlayer() { return this.player; }

	public int getUserId() { return this.player.getUserId().get(); }

	public Battle.ParticipantLabel getParticipantLabel() { return this.participantLabel; }

	public boolean hasAccepted() { return this.accepted; }

	public void accept() { this.accepted = true; }

	public boolean hasDeclined() { return this.declined; }

	public void decline() { this.declined = true; }

	public boolean hasTeam() { return this.team.isPresent(); }

	public Optional<Team> getTeam() { return this.team; }

	public void setTeam(Team team) { this.team = Optional.of(team); }
}
