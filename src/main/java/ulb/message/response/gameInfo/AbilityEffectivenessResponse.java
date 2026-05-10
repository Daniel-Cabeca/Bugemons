package ulb.message.response.gameInfo;

import java.util.Map;

import ulb.DTO.ability.AbilityDTO;
import ulb.message.response.Response;

public class AbilityEffectivenessResponse extends Response {
    private final Map<AbilityDTO, String> effectiveness;

    public AbilityEffectivenessResponse(Map<AbilityDTO, String> effectiveness){
        this.effectiveness = effectiveness;
    }

    public Map<AbilityDTO, String> getEffectiveness(){return this.effectiveness;}
}
