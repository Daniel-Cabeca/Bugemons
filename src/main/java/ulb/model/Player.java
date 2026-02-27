package ulb.model;

import ulb.model.team.Team;

import java.io.IOException;
import java.util.List;

import ulb.model.parser.ItemParser;

public class Player {
	private String name;
	private Team team;
	private Inventory inventory;

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

	/**
	 * Loads all items and adds the default items to the player's inventory
	 */
	public void addDefaultItems() {
		try {
			List<Item> items = ItemParser.loadItems("src/main/resources/json/objets.json");
			for (Item item : items) {
				if (item.getName().equals("Baie Revigorante")) {
					this.inventory.addItem(item, 3);
				}
				else if (item.getName().equals("Baie Tonique")) {
					this.inventory.addItem(item, 2);
				}
				else if (item.getName().equals("Gel Défensif") || item.getName().equals("Sérum Offensif")) {
					this.inventory.addItem(item, 1);
				}
			}

		} catch (IllegalArgumentException e) {
			System.err.println("Error loading items: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("Error reading items file: " + e.getMessage());
		}
	}
}