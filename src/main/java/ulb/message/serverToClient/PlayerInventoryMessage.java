package ulb.message.serverToClient;

import java.io.Serializable;
import java.util.Map;

import ulb.DTO.item.ItemDTO;

public class PlayerInventoryMessage implements Serializable {
    private Map<ItemDTO, Integer> inventoryDTO;

    public PlayerInventoryMessage(Map<ItemDTO, Integer> inventoryDTO) {
        this.inventoryDTO = inventoryDTO;
    }
    
     public Map<ItemDTO, Integer> getInventory() {
        return this.inventoryDTO;
    }
    
}
