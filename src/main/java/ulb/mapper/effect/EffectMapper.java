package ulb.mapper.effect;

import ulb.DTO.effect.EffectDTO;
import ulb.model.effect.Effect;

public class EffectMapper {

    private EffectMapper(){}

    public static Effect toEntity(EffectDTO dto){
        if(dto == null) return null;

        return new Effect(dto.getType(), dto.getTarget()) {
            @Override
            public void apply(ulb.model.battle.Battle battle, ulb.model.battle.Battle.ParticipantLabel team){}
        };
    }

    public static EffectDTO toDTO(Effect entity){
        if(entity == null) return null;

        return new EffectDTO(entity.getType(), entity.getTarget()
        );
    }
}