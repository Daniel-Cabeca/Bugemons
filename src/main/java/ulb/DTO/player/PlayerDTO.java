package ulb.DTO.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.item.ItemDTO;

/**
 * Transferable Player, used on the vue side.
 */
public class PlayerDTO implements Serializable{
    private String userName;
	private String password;
	private List<BugemonDTO> team;
	private Map<ItemDTO, Integer> inventory;

	public PlayerDTO(String userName, String password, List<BugemonDTO> team, Map<ItemDTO, Integer> inventory){
		this.userName = userName;
		this.password = password;
		this.team = team;
		this.inventory = inventory;
	}

	public PlayerDTO(String userName, String password) {
		this.userName = userName;
		this.password = password;
		this.team = new ArrayList<>();
		this.inventory = new HashMap<>();
	}

	public String getName() {return userName;}
	public String getPassword() {return password;}
	public List<BugemonDTO> getTeam() {return team;}
	public Map<ItemDTO, Integer> getInventory() {return inventory;}

	public void setName(String name) {this.userName = name;}
	public void setTeam(List<BugemonDTO> team) {this.team = team;}
	public void setInventory(Map<ItemDTO, Integer> inventory) {this.inventory = inventory;}
}
