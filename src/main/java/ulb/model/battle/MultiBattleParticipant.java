package ulb.model.battle;

public class MultiBattleParticipant {
	private final int userId;
	private boolean accepted = false;

	public MultiBattleParticipant(int userId) {
		this.userId = userId;
	}

	public int getUserId() { return this.userId; }

	public boolean hasAccepted() { return this.accepted; }
	public void accept() { this.accepted = true; }
}
