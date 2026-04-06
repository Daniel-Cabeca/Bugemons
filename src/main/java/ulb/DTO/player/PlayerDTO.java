package ulb.DTO.player;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.item.ItemDTO;

public class PlayerDTO implements Serializable{
    private String name;
	private List<BugemonDTO> team;
	private Map<ItemDTO, Integer> inventory;

	public PlayerDTO(String name, List<BugemonDTO> team, Map<ItemDTO, Integer> inventory){
		this.name = name;
		this.team = team;
		this.inventory = inventory;
	}

	public String getName() {return name;}
	public List<BugemonDTO> getTeam() {return team;}
	public Map<ItemDTO, Integer> getInventory() {return inventory;}

	public void setName(String name) {this.name = name;}
	public void setTeam(List<BugemonDTO> team) {this.team = team;}
	public void setInventory(Map<ItemDTO, Integer> inventory) {this.inventory = inventory;}
}
