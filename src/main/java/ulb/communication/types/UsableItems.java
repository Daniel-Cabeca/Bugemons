package ulb.communication.types;

import java.util.Map;

import ulb.DTO.item.ItemDTO;
import ulb.communication.Message;
import ulb.communication.MessageType;
import ulb.controller.GameController;

public class UsableItems implements Message{
    private Map<ItemDTO, Boolean> itemMap;

    public UsableItems(Map<ItemDTO, Boolean> itemMap){
        this.itemMap = itemMap;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.USABLE_ITEMS;
    }

    @Override
    public Message handle(GameController controller) {
        // TODO Auto-generated method stub
        return null;
    }

    public Map<ItemDTO, Boolean> getItemMap(){return this.itemMap;}
    
}
