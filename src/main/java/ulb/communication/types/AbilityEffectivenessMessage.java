package ulb.communication.types;

import java.util.Map;

import ulb.DTO.ability.AbilityDTO;
import ulb.communication.Message;
import ulb.communication.MessageType;
import ulb.controller.GameController;

public class AbilityEffectivenessMessage implements Message {
    private Map<AbilityDTO, String> effectiveness;

    public AbilityEffectivenessMessage(Map<AbilityDTO, String> effectiveness){
        this.effectiveness = effectiveness;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.ABILITY_EFFECTIVENESS;
    }

    @Override
    public Message handle(GameController controller) {
        // TODO Auto-generated method stub
        return null;
    }

    public Map<AbilityDTO, String> getEffectiveness(){return this.effectiveness;}
}
