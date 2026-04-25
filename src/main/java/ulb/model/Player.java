package ulb.model;

import ulb.model.team.Team;
import ulb.model.item.Inventory;
import ulb.service.InventoryService;
import ulb.service.ItemService;

/**
 * Represents a player with credentials, team, and inventory.
 */
public class Player {
	private String username;
	private String password;
	private Team team;
	private Inventory inventory;

	private static final String ITEMS_PATH = "src/main/resources/json/objets.json";

	/**
	 * Creates a default player named "Player".
	 *
	 * @param itemService Item service for starter inventory
	 */
	public Player(ItemService itemService) {
		this("Player", itemService);
	}

	/**
	 * Creates a player with default password.
	 *
	 * @param username Player name
	 * @param itemService Item service for starter inventory
	 */
	public Player(String username, ItemService itemService) {
		this.username = username;
		this.password = "password";
		this.team = new Team();
		this.inventory = new Inventory();

		addDefaultItems(itemService);
	}

	/**
	 * Creates a player with explicit credentials.
	 *
	 * @param username Player name
	 * @param password Player password
	 * @param itemService Item service for starter inventory
	 */
	public Player(String username, String password, ItemService itemService, InventoryService inventoryService) {
		this.username = username;
		this.password = password;
		this.team = new Team();
		this.inventory = new Inventory();

		addDefaultItems(itemService);
		inventoryService.insertInventory(this.inventory, username);
	}

	/**
	 * Creates a player from fully specified state.
	 *
	 * @param username Player name
	 * @param password Player password
	 * @param inventory Player inventory
	 */
	public Player(String username, String password, Inventory inventory) {
		this.username = username;
		this.password = password;
		this.team = new Team();
		this.inventory = inventory;
	}

	// /**
	//  * Creates a player with default password and provided state.
	//  *
	//  * @param name Player name
	//  * @param team Player team
	//  * @param inventory Player inventory
	//  */
	// public Player(String name, Team team, Inventory inventory) {
	// 	this.userName = name;
	// 	this.password = "password";
	// 	this.team = team;
	// 	this.inventory = inventory;
	// }

	/** Returns player username. */
	public final String getUsername() {return this.username;}
	/** Returns player password. */
	public final String getPassword() {return this.password;}
	/** Returns player team. */
	public Team getTeam() {return this.team;}
	/** Returns player inventory. */
	public Inventory getInventory() {return this.inventory;}
	/** Sets player username. */
	public void setUsername(String username) {this.username = username;}
	/** Sets player team. */
	public void setTeam(Team team) {this.team = team;}

	/**
	 * Loads all items and adds the default items to the player's inventory
	 */
	public void addDefaultItems(ItemService itemService) {
		this.inventory = itemService.createStarterInventory();
	}
}
