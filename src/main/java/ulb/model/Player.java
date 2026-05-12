package ulb.model;

import ulb.model.team.Team;

import java.util.Optional;

import ulb.model.item.Inventory;

/**
 * Represents a player with credentials, team, and inventory.
 */
public class Player {
	private String username;
	private final Optional<Integer> userId;
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
		this.team = new Team();
		this.inventory = inventory;

		if (userId == -1){
			this.userId = Optional.empty(); // empty when the player is a Bot
		} else {
			this.userId = Optional.of(userId);
		}
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
	public final Optional<Integer> getUserId() {return this.userId;}
	/** Returns username. */
	public final String getUsername() {return this.username;}
	/** Returns player team. */
	public Team getTeam() {return this.team;}
	/** Returns player team ID. */
	public final int getTeamId(){return this.team.getId();}
	/** Returns player inventory. */
	public Inventory getInventory() {return this.inventory;}
	/** Sets player team. */
	public void setTeam(Team team) {this.team = team;}

}
