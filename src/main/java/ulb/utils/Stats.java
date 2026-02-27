package ulb.utils;


public class Stats {
	public int pv;
	public int defense;
	public int attack;
	public int initiative;

	public Stats() {
		this.pv = 0;
		this.defense = 0;
		this.attack = 0;
		this.initiative = 0;
	}

	public Stats(int pv, int defense, int attack, int initiative) {
		this.pv = pv;
		this.defense = defense;
		this.attack = attack;
		this.initiative = initiative;
	}

	public void add(Stats delta) {
		this.pv = (this.pv <= Math.abs(delta.pv) && delta.pv < 0)? 0: this.pv + delta.pv;
		this.attack = (this.attack <= Math.abs(delta.attack) && delta.attack < 0)? 0: this.attack + delta.attack;
		this.defense = (this.defense <= Math.abs(delta.defense) && delta.defense < 0)? 0: this.defense + delta.defense;
		this.initiative = (this.initiative <= Math.abs(delta.initiative) && delta.initiative < 0)? 0: this.initiative + delta.initiative;
	}
}
