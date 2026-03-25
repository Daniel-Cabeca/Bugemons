package ulb.model.sample;

import java.util.Map;

import ulb.model.effect.Effect;
import ulb.model.effect.EffectHeal;
import ulb.model.effect.EffectResetMalus;
import ulb.model.effect.EffectStatModifier;
import ulb.model.effect.EffectSwitch;

public class EffectSample {
    static public Effect getHeal(){
        return new EffectHeal(Effect.EffectTarget.OWN_BUGEMON, 10);
    }

    static public Effect getDefenseDecreaseOther(){
        return new EffectStatModifier(Effect.EffectTarget.OPPOSITE_BUGEMON,
            EffectStatModifier.EffectDuration.PERMANENT, Map.of(EffectStatModifier.StatType.DEFENSE, -10));
    }

    static public Effect getAttackIncreaseSelf(){
        return new EffectStatModifier(Effect.EffectTarget.OWN_BUGEMON, EffectStatModifier.EffectDuration.PERMANENT,
            Map.of(EffectStatModifier.StatType.ATTACK, 10));
    }

    static public Effect getMultiEffect(){
        return new EffectStatModifier(Effect.EffectTarget.OWN_BUGEMON, EffectStatModifier.EffectDuration.PERMANENT,
            Map.of(EffectStatModifier.StatType.ATTACK, 10, EffectStatModifier.StatType.DEFENSE, 10));
    }

    static public Effect getSwitchEffect(){
        return new EffectSwitch(Effect.EffectTarget.OWN_BUGEMON);
    }

    static public Effect getResetMalusEffect(){
        return new EffectResetMalus(Effect.EffectTarget.OWN_BUGEMON);
    }
}
