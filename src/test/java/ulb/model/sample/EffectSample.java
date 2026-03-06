package ulb.model.sample;

import ulb.model.Effect;

public class EffectSample {
    static public Effect getHeal(){
        return new Effect("soin", "lanceur", 10);
    }

    static public Effect getDefenseDecreaseSelf(){
        return new Effect("stat_modifier", "lanceur", "defense", -10, "permanent");
    }

    static public Effect getDefenseDecreaseOther(){
        return new Effect("stat_modifier", "adversaire", "defense", -10, "permanent");
    }

    static public Effect getAttackDecreaseSelf(){
        return new Effect("stat_modifier", "lanceur", "attaque", -10, "permanent");
    }

    static public Effect getAttackDecreaseOther(){
        return new Effect("stat_modifier", "adversaire", "attaque", -10, "permanent");
    }

    static public Effect getInitiativeDecreaseSelf(){
        return new Effect("stat_modifier", "lanceur", "initiative", -10, "permanent");
    }

    static public Effect getInitiativeDecreaseOther(){
        return new Effect("stat_modifier", "adversaire", "initiative", -10, "permanent");
    }
}
