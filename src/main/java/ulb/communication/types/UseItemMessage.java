package ulb.communication.types;

import ulb.DTO.item.ItemDTO;
import ulb.communication.Message;
import ulb.communication.MessageType;
import ulb.controller.GameController;

public class UseItemMessage implements Message{
    private ItemDTO item;

    public UseItemMessage(ItemDTO item){
        this.item = item;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.USE_ITEM;
    }

    @Override
    public Message handle(GameController controller) {
        // TODO Auto-generated method stub
        return null;
    }

    public ItemDTO getItem(){return this.item;}
}
