package ulb.message.serverToClient.gameInfo;

import java.io.Serializable;

public class BattleEndInfoMessage implements Serializable{
	private final boolean victory;
	private final Integer totalXp;
	private final String opponent;

	public BattleEndInfoMessage(boolean victory, Integer totalXp, String opponent){
		this.victory = victory;
		this.totalXp = totalXp;
		this.opponent = opponent;
	}

	public boolean isVictory(){return this.victory;}
	public Integer getTotalXp(){return this.totalXp;}
	public String getOpponent() {return this.opponent;}
}
