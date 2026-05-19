package ulb.DTO.player;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.item.ItemDTO;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Transferable Player, used on the vue side.
 */
public class PlayerDTO implements Serializable {
	private final int userId;
	private final String username;
	private List<BugemonDTO> team;
	private Map<ItemDTO, Integer> inventory;

	public PlayerDTO(int userId, String username, List<BugemonDTO> team, Map<ItemDTO, Integer> inventory) {
		this.userId = userId;
		this.username = username;
		this.team = team;
		this.inventory = inventory;
	}

	public int getUserId() { return this.userId; }

	public String getUsername() { return username; }

	public List<BugemonDTO> getTeam() { return team; }

	public void setTeam(List<BugemonDTO> team) { this.team = team; }

	public Map<ItemDTO, Integer> getInventory() { return inventory; }

	public void setInventory(Map<ItemDTO, Integer> inventory) { this.inventory = inventory; }
}

