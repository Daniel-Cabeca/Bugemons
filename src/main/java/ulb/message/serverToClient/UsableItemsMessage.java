package ulb.message.serverToClient;

import java.io.Serializable;
import java.util.Map;

public class UsableItemsMessage implements Serializable {
    private Map<String, Boolean> itemMap;

    public UsableItemsMessage(Map<String, Boolean> itemMap){
        this.itemMap = itemMap;
    }

    public Map<String, Boolean> getItemMap(){return this.itemMap;}
    
}
