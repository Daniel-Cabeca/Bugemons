package ulb.model;

public class Bugemon {
    public enum Type {
        pyro,
        flora,
        aqua
    }
    private final String name;
    private final Type type;

    private int pv;
    private int defense;
    private int attack;
    private int initiative;
    //private Buff buff; Spécifie dynamiquement la valeur des stats

    private int xp;
    private int level;

    //private Vector<Ability> abilities;

    public Bugemon (String name, Type type, int pv, int attack, int defense, int level){ //,Vector<Ability> abilities) {
        this.name = name;
        this.type = type;
        this.pv = pv;
        this.attack = attack;
        this.defense = defense;
        this.level = level;
        //this.abilities = abilities;
        this.xp = 0;
    }

    public void increasePV (int delta) {}
    public void increaseDefense (int delta) {}
    public void increaseAttack (int delta) {}
    public void increaseInitiative (int delta) {}
    public void gainXP (int experience) {}

    public final String getName () {return this.name;}
    public final Type getType () {return this.type;}
    public int getPV () {return 0;}
    public int getDefense () {return 0;}
    public int getAttack () {return 0;}
    public int getInitiative () {return 0;}
    public int getLevel () {return this.level;}
    public int getXP () {return this.xp;}
    //public Vector<Ability> getAbilities () {}

    //public void applyBuff (Buff buff) {}
    //public void resetBuff () {}
}
