package ulb.model;

import ulb.model.team.Team;
import ulb.model.item.Inventory;
import ulb.service.ItemService;

public class Player {
	private String name;
	private Team team;
	private Inventory inventory;

	private static final String ITEMS_PATH = "src/main/resources/json/objets.json";

	public Player(ItemService itemService) {
		this("Player", itemService);
	}

	public Player(String name, ItemService itemService) {
		this.name = name;
		this.team = new Team();
		this.inventory = new Inventory();

		addDefaultItems(itemService);
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
	public void addDefaultItems(ItemService itemService) {
		this.inventory = itemService.createStarterInventory();
	}
}
