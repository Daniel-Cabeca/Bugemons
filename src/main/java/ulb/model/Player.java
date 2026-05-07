package ulb.model;

import ulb.model.team.Team;
import ulb.model.item.Inventory;

/**
 * Represents a player with credentials, team, and inventory.
 */
public class Player {
	private String username;
	private final int userId;
	private Team team;
	private Inventory inventory;
	
	/**
	 * Creates a default player named "Player".
	 * Used for tests.
	 */
	public Player() {
		this("Player", -1);
	}

	/**
	 * Creates a player from fully specified state.
	 *
	 * @param username Player name
	 * @param userId Player id
	 * @param inventory Player inventory
	 */
	public Player(String username, int userId, Inventory inventory) {
		this.username = username;
		this.userId = userId;
		this.team = new Team();
		this.inventory = inventory;
	}

	/**
	 * Creates a player with an empty inventory from its username and id.
	 *
	 * @param username Player name
	 * @param userId Player id
	 */
	public Player(String username, int userId) {
		this(username, userId, new Inventory());
	}

	/** Returns User ID. */
	public final int getUserId() {return this.userId;}
	/** Returns username. */
	public final String getUsername() {return this.username;}
	/** Returns player team. */
	public Team getTeam() {return this.team;}
	/** Returns player team ID. */
	public final int getTeamId(){return this.team.getId();}
	/** Returns player inventory. */
	public Inventory getInventory() {return this.inventory;}
	/** Sets player username. */
	public void setUsername(String username) {this.username = username;}
	/** Sets player team. */
	public void setTeam(Team team) {this.team = team;}

}
