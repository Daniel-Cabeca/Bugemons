package ulb.DTO.stats;

public class StatsDTO {
    private int hp;
	private int attack;
	private int defense;
	private int initiative;

    public int getHP(){return this.hp;}
    public int getAttack(){return this.attack;}
    public int getDefense(){return this.defense;}
    public int getInitiative(){return this.initiative;}

    public void setHP(int hp){this.hp = hp;}
    public void setAttack(int attack){this.attack = attack;}
    public void setDefense(int defense){this.defense = defense;}
    public void setInitative(int initiative){this.initiative = initiative;}
}
