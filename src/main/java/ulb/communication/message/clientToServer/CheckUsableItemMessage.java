package ulb.communication.message.clientToServer;

import java.util.List;

import ulb.DTO.item.ItemDTO;
import ulb.communication.Message;
import ulb.communication.MessageType;
import ulb.controller.GameController;

public class CheckUsableItemMessage implements Message{
    private List<ItemDTO> items;

    public CheckUsableItemMessage(List<ItemDTO> items){
        this.items = items;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.CHECK_USABLE_ITEMS;
    }

    @Override
    public Message handle(GameController controller) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<ItemDTO> getItems(){return this.items;}
}
