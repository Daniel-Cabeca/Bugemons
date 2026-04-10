package ulb.mapper.effect;

import ulb.DTO.effect.EffectDTO;
import ulb.DTO.effect.EffectHealDTO;
import ulb.DTO.effect.EffectStatModifierDTO;
import ulb.model.effect.Effect;
import ulb.model.effect.EffectHeal;
import ulb.model.effect.EffectResetMalus;
import ulb.model.effect.EffectStatModifier;
import ulb.model.effect.EffectSwitch;
import ulb.model.effect.EffectType;
import ulb.model.battle.Battle;

public class EffectMapper {

    private EffectMapper(){}

    public static Effect toEntity(EffectDTO dto){
        if(dto == null) return null;
        Effect effect = null;
        switch (dto.getType()) {
            case SWITCH:
                effect = new EffectSwitch(dto.getTarget()) {
                    @Override
                    public void apply(Battle battle, Battle.ParticipantLabel team){}
                };
                break;
            
            case RESET_MALUS:
                effect = new EffectResetMalus(dto.getTarget()) {
                    @Override
                    public void apply(Battle battle, Battle.ParticipantLabel team){}
                };
                break;

            case STAT_MODIFIER:
                if (dto instanceof EffectStatModifierDTO effectStat){
                    effect = toEntity(effectStat);
                }
                break;

            case HEAL:
                if (dto instanceof EffectHealDTO effectHeal){
                    effect = toEntity(effectHeal);
                }
                break;
                
            default:
                throw new RuntimeException("Effect Type not known !");
        }

        return effect;
    }

    public static EffectHeal toEntity(EffectHealDTO dto){
        if(dto == null) return null;

        return new EffectHeal(dto.getTarget(), dto.getValue()) {
            @Override
            public void apply(Battle battle, Battle.ParticipantLabel team){}
        };
    }

    public static EffectStatModifier toEntity(EffectStatModifierDTO dto){
        if(dto == null) {
            return null;
        }
        return new EffectStatModifier(dto.getTarget(), dto.getDuration(), dto.getModifiers()) {
            @Override
            public void apply(Battle battle, Battle.ParticipantLabel team){}
        };
    }

    public static EffectDTO toDTO(Effect entity){
        if(entity == null) return null;

        if (entity instanceof EffectStatModifier statModifier){
            return new EffectStatModifierDTO(EffectType.STAT_MODIFIER, statModifier.getTarget(), statModifier.getDuration(), statModifier.getModifiers());
        } else if (entity instanceof EffectHeal heal){
            return new EffectHealDTO(EffectType.HEAL, heal.getTarget(), heal.getValue());
        } else if (entity instanceof EffectResetMalus resetMalus){
            return new EffectDTO(EffectType.RESET_MALUS, resetMalus.getTarget());
        } else if (entity instanceof EffectSwitch switchEffect){
            return new EffectDTO(EffectType.SWITCH, switchEffect.getTarget());
        }
        return null;
    }
}