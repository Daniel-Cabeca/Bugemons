package ulb.message.serverToClient.gameInfo;

import java.io.Serializable;

public class BattleEndInfoMessage implements Serializable{
	private final boolean victory;
	private final Integer totalXp;

	public BattleEndInfoMessage(boolean victory, Integer totalXp){
		this.victory = victory;
		this.totalXp = totalXp;
	}

	public boolean isVictory(){return this.victory;}
	public Integer getTotalXp(){return this.totalXp;}
}
