package ulb.message.serverToClient;

import java.io.Serializable;

public class BattleEndInfoMessage implements Serializable{
	private boolean victory;
	private Integer totalXp;

	public BattleEndInfoMessage(boolean victory, Integer totalXp){
		this.victory = victory;
		this.totalXp = totalXp;
	}

	public boolean isVictory(){return this.victory;}
	public Integer getTotalXp(){return this.totalXp;}
}
