package ulb.communication.types;

import java.util.List;

import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.communication.Message;
import ulb.communication.MessageType;
import ulb.controller.GameController;

public class GetAbilityEffectivenessMessage implements Message {
    private List<AbilityDTO> abilities;
    private BugemonDTO bugemonTarget;

    public GetAbilityEffectivenessMessage(List<AbilityDTO> abilities, BugemonDTO bugemonTarget){
        this.abilities = abilities;
        this.bugemonTarget = bugemonTarget;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.GET_ABILITY_EFFECTIVENESS;
    }

    @Override
    public Message handle(GameController controller) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<AbilityDTO> getAbilities(){return this.abilities;}
    public BugemonDTO getBugemonTarget(){return this.bugemonTarget;}
}
