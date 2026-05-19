package ulb.message.response.gameInfo;

import ulb.message.response.Response;

import java.util.Map;

public class UsableItemsResponse extends Response {
	private final Map<String, Boolean> itemMap;

	public UsableItemsResponse(Map<String, Boolean> itemMap) {
		this.itemMap = itemMap;
	}

	public Map<String, Boolean> getItemMap() { return this.itemMap; }

}
