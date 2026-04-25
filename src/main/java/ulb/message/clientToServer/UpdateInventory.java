package ulb.message.clientToServer;

import ulb.DTO.item.ItemDTO;
import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class UpdateInventory implements ClientToServerMessage {
    private ItemDTO itemDTO;
    private int quantity;
    private String username;
    private boolean isAdd;

    public UpdateInventory(ItemDTO itemDTO, int quantity, String username, boolean isAdd) {
        this.itemDTO = itemDTO;
        this.username = username;
        this.isAdd = isAdd;
        this.quantity = quantity;
    }

    @Override
	public void dispatch(ServerMessageHandler handler) {
		handler.handle(this);
	}

    public ItemDTO getItem() {
        return this.itemDTO;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public String getUsername() {
        return this.username;
    }

    public boolean isAdd() {
        return this.isAdd;
    }

    
}
