package ulb.communication.types;

import ulb.communication.Message;
import ulb.communication.MessageType;
import ulb.controller.GameController;
import ulb.model.type.Effectiveness;

public class AbilityEffectivenessMessage implements Message {
    private Effectiveness effectiveness;

    public AbilityEffectivenessMessage(Effectiveness effectiveness){
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

    public Effectiveness getEffectiveness(){return this.effectiveness;}
}
