package ulb.communication.message.serverToClient;

import java.io.Serializable;
import java.util.Map;

import ulb.DTO.ability.AbilityDTO;

public class AbilityEffectivenessMessage implements Serializable {
    private Map<AbilityDTO, String> effectiveness;

    public AbilityEffectivenessMessage(Map<AbilityDTO, String> effectiveness){
        this.effectiveness = effectiveness;
    }

    public Map<AbilityDTO, String> getEffectiveness(){return this.effectiveness;}
}
