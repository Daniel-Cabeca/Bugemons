package ulb.message.response.gameInfo;

import ulb.message.response.Response;

public class BattleEndInfoResponse extends Response {
	private final boolean victory;
	private final Integer totalXp;
	private final boolean multiplayerBattle;

	public BattleEndInfoResponse(boolean victory, Integer totalXp, boolean multiplayerBattle){
		this.victory = victory;
		this.totalXp = totalXp;
		this.multiplayerBattle = multiplayerBattle;
	}

	public boolean isVictory() { return this.victory; }
	public Integer getTotalXp() { return this.totalXp; }
	public boolean isMultiplayerBattle() { return this.multiplayerBattle; }
}
