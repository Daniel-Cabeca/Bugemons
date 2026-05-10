package ulb.message.serverToClient.gameInfo;

import java.io.Serializable;

public class BattleEndInfoMessage implements Serializable{
	private final boolean victory;
	private final Integer totalXp;
	private final String opponent;
	private final boolean multiplayerBattle;

	public BattleEndInfoMessage(boolean victory, Integer totalXp, String opponent, boolean multiplayerBattle){
		this.victory = victory;
		this.totalXp = totalXp;
		this.opponent = opponent;
		this.multiplayerBattle = multiplayerBattle;
	}

	public boolean isVictory() { return this.victory; }
	public Integer getTotalXp() { return this.totalXp; }
	public String getOpponent() { return this.opponent; }
	public boolean isMultiplayerBattle() { return this.multiplayerBattle; }
}
