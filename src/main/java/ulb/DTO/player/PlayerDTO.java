package ulb.DTO.player;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.item.ItemDTO;

/**
 * Transferable Player, used on the vue side.
 */
public class PlayerDTO implements Serializable {
	private final int userId;
    private final String userName;
	private List<BugemonDTO> team;
	private Map<ItemDTO, Integer> inventory;

	public PlayerDTO(int userId, String userName, List<BugemonDTO> team, Map<ItemDTO, Integer> inventory){
		this.userId = userId;
		this.userName = userName;
		this.team = team;
		this.inventory = inventory;
	}

	public int getUserId() { return this.userId; }
	public String getUsername() {return userName;}
	public List<BugemonDTO> getTeam() {return team;}
	public Map<ItemDTO, Integer> getInventory() {return inventory;}

	public void setTeam(List<BugemonDTO> team) {this.team = team;}
	public void setInventory(Map<ItemDTO, Integer> inventory) {this.inventory = inventory;}
}

