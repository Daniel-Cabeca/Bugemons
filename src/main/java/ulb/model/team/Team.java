package ulb.model.team;

import ulb.exceptions.EntityNotFoundException;
import ulb.model.bugemon.Bugemon;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Team {
	public static final int MAX_PARTY_SIZE = 6;

	private int id;
	private final List<Bugemon> members;
	private String teamName;

	public Team(List<Bugemon> initialMembers) {
		this();

		if (initialMembers == null) {
			throw new IllegalArgumentException("Initial members list cannot be null.");
		}

		for (Bugemon bugemon : initialMembers) {
			boolean added = this.add(bugemon);
			if (!added) {
				throw new IllegalArgumentException("Invalid team composition (duplicated Bugemons or more than 6 " +
						"Bugemons.");
			}
		}
	}

	public Team() {
		this.members = new ArrayList<>();
		this.teamName = "";
	}

	/**
	 * Adds a Bugemon to the team
	 *
	 * @param bugemon the Bugemon to be added to the team
	 * @return {@code true} if the Bugemon can be added, {@code false} if it cannot be added
	 */
	public boolean add(Bugemon bugemon) {
		if (bugemon == null) {
			return false;
		}

		if (this.members.size() >= MAX_PARTY_SIZE) {
			return false;
		}

		if (containsName(bugemon.getName())) {
			return false;
		}

		this.members.add(bugemon);
		return true;
	}

	/**
	 * Checks if the team contains a Bugemon with the same name
	 *
	 * @param name the name to be checked
	 * @return {@code true} if team contains a Bugemon with the same name, {@code false} otherwise
	 */
	public boolean containsName(String name) {
		if (name == null) {
			return false;
		}

		for (Bugemon bugemon : this.members) {
			if (bugemon.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	public int getId() { return this.id; }

	public void setId(int id) { this.id = id; }

	public boolean isValid() {
		return !this.isEmpty() && this.size() <= MAX_PARTY_SIZE;
	}

	public boolean isEmpty() {
		return this.members.isEmpty();
	}

	public int size() {
		return this.members.size();
	}

	public String getTeamName() { return this.teamName; }

	public void setTeamName(String teamName) { this.teamName = teamName; }

	/**
	 * Checks if entire team is KO or not
	 *
	 * @return true if entire team is KO
	 */
	public boolean checkTeamKO() {
		if (this.members.isEmpty()) {
			return true;
		}

		for (Bugemon b : this.members) {
			if (!b.isKO()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Gives the Bugemon at a given index.
	 *
	 * @param index The index of the Bugemon
	 * @return The Bugemon at the index
	 */
	public Bugemon getBugemon(int index) {
		return this.members.get(index);
	}

	/**
	 * Returns the next available Bugemon as an Optional object.
	 * If the Optional is empty, no Bugemon is available
	 *
	 * @param current the current Bugemon
	 * @return the next available Bugemon as an Optional objet
	 */
	public Optional<Bugemon> getNextBugemon(Bugemon current) {
		int indexCurrent = this.getBugemonIndex(current);
		if (indexCurrent < 0) {
			return Optional.empty();
		}

		for (int i = indexCurrent + 1; i % members.size() != indexCurrent; i++) {
			Bugemon candidate = members.get(i % this.members.size());
			if (!candidate.isKO()) {
				return Optional.of(candidate);
			}
		}

		return Optional.empty();
	}

	private int getBugemonIndex(Bugemon bugemon) {
		return this.members.indexOf(bugemon);
	}

	public List<Bugemon> getBugemonsAlive() {
		List<Bugemon> availableBugemons = new ArrayList<>();

		for (Bugemon b : this.getMembers()) {
			if (!b.isKO()) {
				availableBugemons.add(b);
			}
		}
		return availableBugemons;
	}

	public List<Bugemon> getMembers() {
		return this.members;
	}

	public boolean isBugemonOK(Bugemon bugemon) {
		return this.contains(bugemon) && !bugemon.isKO();
	}

	/**
	 * Checks if the team contains the Bugemon
	 *
	 * @param bugemon the Bugemon to be checked
	 * @return {@code true} if team contains the Bugemon, {@code false} otherwise
	 */
	public boolean contains(Bugemon bugemon) {
		if (bugemon == null) {
			return false;
		}

		for (Bugemon b : this.members) {
			if (bugemon.equals(b)) {
				return true;
			}
		}
		return false;
	}

	public Optional<Bugemon> getFirstLevelUpBugemon() {
		for (Bugemon bugemon : this.getMembers()) {
			if (bugemon.getRemainingReward() > 0) {
				return Optional.of(bugemon);
			}
		}
		return Optional.empty();
	}

	public int getLevelUpBugemonNumber() {
		int levelUpNumber = 0;
		for (Bugemon bugemon : this.getMembers()) {
			if (bugemon.getRemainingReward() > 0) {
				levelUpNumber++;
			}
		}
		return levelUpNumber;
	}

	public Bugemon getBugemonById(String id) throws EntityNotFoundException {
		for (Bugemon bugemon : this.getMembers()) {
			if (bugemon.getSpeciesId().equals(id)) {
				return bugemon;
			}
		}
		throw new EntityNotFoundException("bugemon", id);
	}

}
