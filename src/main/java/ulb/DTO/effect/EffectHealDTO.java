package ulb.DTO.effect;

import ulb.model.effect.EffectTarget;
import ulb.model.effect.EffectType;

/**
 * Transferable Effect-Heal, used on the vue side.
 */
public class EffectHealDTO extends EffectDTO {
    private int value;

    public EffectHealDTO(EffectType type, EffectTarget target, int value){
        super(type, target);
        this.value = value;
    }

    public int getValue(){return this.value;}

    public void setValue(int value){this.value = value;}
}
