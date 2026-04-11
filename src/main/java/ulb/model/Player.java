package ulb.model;

import ulb.model.team.Team;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import ulb.model.item.Inventory;

import ulb.service.ServiceLoader;
import ulb.service.ItemService;

public class Player {
	private String userName;
	private String password;
	private Team team;
	private Inventory inventory;

	private static final String ITEMS_PATH = "src/main/resources/json/objets.json";

	public Player() {
		this.userName = "Player";
		this.password = "password";
		this.team = new Team();
		this.inventory = new Inventory();

		addDefaultItems();
	}

	public Player(String name, String password) {
		this.userName = name;
		this.password = password;
		this.team = new Team();
		this.inventory = new Inventory();

		addDefaultItems();
	}

	public Player(String name, String password, Team team, Inventory inventory) {
		this.userName = name;
		this.password = password;
		this.team = team;
		this.inventory = inventory;
	}

	public final String getName() {return this.userName;}
	public final String getPassword() {return this.password;}
	public Team getTeam() {return this.team;}
	public Inventory getInventory() {return this.inventory;}

	public void setTeam(Team team) {this.team = team;}

	/**
	 * Loads all items and adds the default items to the player's inventory
	 */
	public void addDefaultItems() {
		ItemService itemService = ServiceLoader.getItemService();
		this.inventory = itemService.createStarterInventory();
	}
}
