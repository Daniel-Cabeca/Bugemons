package ulb.message.serverToClient.gameInfo;

import java.io.Serializable;
import java.util.Map;

public class UsableItemsMessage implements Serializable {
    private final Map<String, Boolean> itemMap;

    public UsableItemsMessage(Map<String, Boolean> itemMap){
        this.itemMap = itemMap;
    }

    public Map<String, Boolean> getItemMap(){return this.itemMap;}
    
}
