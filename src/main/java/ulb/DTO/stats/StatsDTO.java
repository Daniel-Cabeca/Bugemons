package ulb.DTO.stats;

import java.io.Serializable;

public class StatsDTO implements Serializable{
    private int hp;
	private int attack;
	private int defense;
	private int initiative;

    public StatsDTO(int hp, int attack, int defense, int initiative){
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.initiative = initiative;
    }

    public int getHp(){return this.hp;}
    public int getAttack(){return this.attack;}
    public int getDefense(){return this.defense;}
    public int getInitiative(){return this.initiative;}

    public void setHP(int hp){this.hp = hp;}
    public void setAttack(int attack){this.attack = attack;}
    public void setDefense(int defense){this.defense = defense;}
    public void setInitative(int initiative){this.initiative = initiative;}
}
