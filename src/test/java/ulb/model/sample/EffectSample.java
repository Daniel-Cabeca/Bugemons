package ulb.model.sample;

import java.util.Map;

import ulb.model.effect.Effect;

public class EffectSample {
    static public Effect getHeal(){
        return new Effect(Effect.EffectType.HEAL, Effect.EffectTarget.OWN_BUGEMON, Map.of(Effect.StatType.HP, 10), Effect.EffectDuration.PERMANENT);
    }

    static public Effect getDefenseDecreaseSelf(){
        return new Effect(Effect.EffectType.STAT_MODIFIER, Effect.EffectTarget.OWN_BUGEMON,
            Map.of(Effect.StatType.DEFENSE, -10), Effect.EffectDuration.PERMANENT);
    }

    static public Effect getDefenseDecreaseOther(){
        return new Effect(Effect.EffectType.STAT_MODIFIER, Effect.EffectTarget.OPPOSITE_BUGEMON,
            Map.of(Effect.StatType.DEFENSE, -10), Effect.EffectDuration.PERMANENT);
    }

    static public Effect getAttackDecreaseSelf(){
        return new Effect(Effect.EffectType.STAT_MODIFIER, Effect.EffectTarget.OWN_BUGEMON,
            Map.of(Effect.StatType.ATTACK, -10), Effect.EffectDuration.PERMANENT);
    }

    static public Effect getAttackDecreaseOther(){
        return new Effect(Effect.EffectType.STAT_MODIFIER, Effect.EffectTarget.OPPOSITE_BUGEMON,
            Map.of(Effect.StatType.ATTACK, -10), Effect.EffectDuration.PERMANENT);
    }

    static public Effect getAttackIncreaseSelf(){
        return new Effect(Effect.EffectType.STAT_MODIFIER, Effect.EffectTarget.OWN_BUGEMON,
            Map.of(Effect.StatType.ATTACK, 10), Effect.EffectDuration.PERMANENT);
    }

    static public Effect getInitiativeDecreaseSelf(){
        return new Effect(Effect.EffectType.STAT_MODIFIER, Effect.EffectTarget.OWN_BUGEMON,
            Map.of(Effect.StatType.INITIATIVE, -10), Effect.EffectDuration.PERMANENT);
    }

    static public Effect getInitiativeDecreaseOther(){
        return new Effect(Effect.EffectType.STAT_MODIFIER, Effect.EffectTarget.OPPOSITE_BUGEMON,
            Map.of(Effect.StatType.INITIATIVE, -10), Effect.EffectDuration.PERMANENT);
    }

    static public Effect getMultiEffect(){
        return new Effect(Effect.EffectType.STAT_MODIFIER, Effect.EffectTarget.OWN_BUGEMON,
            Map.of(Effect.StatType.ATTACK, 10, Effect.StatType.DEFENSE, 10), Effect.EffectDuration.PERMANENT);
    }

    static public Effect getSwitchEffect(){
        return new Effect(Effect.EffectType.SWITCH, Effect.EffectTarget.OWN_BUGEMON, Map.of(), Effect.EffectDuration.PERMANENT);
    }

    static public Effect getResetMalusEffect(){
        return new Effect(Effect.EffectType.RESET_MALUS, Effect.EffectTarget.OWN_BUGEMON, Map.of(), Effect.EffectDuration.PERMANENT);
    }
}
