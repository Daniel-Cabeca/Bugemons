package ulb.communication.types;

import java.util.List;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.communication.Message;
import ulb.controller.GameController;

public class SetUpTeamMessage implements Message {
    private List<BugemonDTO> team;

    public SetUpTeamMessage(List<BugemonDTO> team){
        this.team = team;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.SETUP_TEAM;
    }

    @Override
    public Message handle(GameController controller) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<BugemonDTO> getTeam(){return this.team;}
}
