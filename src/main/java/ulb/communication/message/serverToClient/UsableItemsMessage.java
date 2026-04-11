package ulb.communication.message.serverToClient;

import java.io.Serializable;
import java.util.Map;

import ulb.DTO.item.ItemDTO;

public class UsableItemsMessage implements Serializable {
    private Map<ItemDTO, Boolean> itemMap;

    public UsableItemsMessage(Map<ItemDTO, Boolean> itemMap){
        this.itemMap = itemMap;
    }

    public Map<ItemDTO, Boolean> getItemMap(){return this.itemMap;}
    
}
