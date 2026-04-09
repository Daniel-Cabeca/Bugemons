package ulb.communication.types;

import ulb.DTO.ability.AbilityDTO;
import ulb.communication.Message;
import ulb.communication.MessageType;
import ulb.controller.GameController;

public class UseAbility implements Message{
    private AbilityDTO ability;

    public UseAbility(AbilityDTO ability){
        this.ability = ability;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.USE_ABILITY;
    }

    @Override
    public Message handle(GameController controller) {
        // TODO Auto-generated method stub
        return null;
    }

    public AbilityDTO getAbility(){return this.ability;}
}
