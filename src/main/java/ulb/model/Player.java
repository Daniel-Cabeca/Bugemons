package ulb.model;

import ulb.model.team.Team;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import ulb.repository.loader.json.parser.ItemJsonParser;
import ulb.model.item.Inventory;
import ulb.model.item.Item;

import ulb.service.ServiceLoader;
import ulb.service.ItemService;

public class Player {
	private String name;
	private Team team;
	private Inventory inventory;

	private static final String ITEMS_PATH = "src/main/resources/json/objets.json";

	public Player() {
		this.name = "Player";
		this.team = new Team();
		this.inventory = new Inventory();

		addDefaultItems();
	}

	public Player(String name) {
		this.name = name;
		this.team = new Team();
		this.inventory = new Inventory();

		addDefaultItems();
	}

	public Player(String name, Team team, Inventory inventory) {
		this.name = name;
		this.team = team;
		this.inventory = inventory;
	}

	public final String getName() {return this.name;}
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
