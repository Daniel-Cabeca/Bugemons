package ulb.communication.types;

import java.util.List;

import ulb.DTO.bugemon.BugemonSpeciesDTO;
import ulb.communication.Message;
import ulb.communication.MessageType;
import ulb.controller.GameController;

public class BugemonSpeciesMessage implements Message {
    private List<BugemonSpeciesDTO> species;

    public BugemonSpeciesMessage(List<BugemonSpeciesDTO> species){
        this.species = species;
    }

    public List<BugemonSpeciesDTO> getSpecies(){
        return this.species;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.BUGEMON_SPECIES;
    }

    @Override
    public Message handle(GameController controller) {
        // TODO Auto-generated method stub
        return null;
    }
}
