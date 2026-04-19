package ulb.model.team;

import java.util.ArrayList;
import java.util.List;

import ulb.model.bugemon.Bugemon;

public class Team {
    public static final int MAX_PARTY_SIZE = 6;

    private List<Bugemon> members;

    public Team() {
        this.members = new ArrayList<>();
    }

    public Team(List<Bugemon> initialMembers) {
        this();

        if (initialMembers == null) {
            throw new IllegalArgumentException("Initial members list cannot be null.");
        }

        for (Bugemon bugemon : initialMembers) {
            boolean added = this.add(bugemon);
            if (!added) {
                throw new IllegalArgumentException("Invalid team composition (duplicated Bugemons or more than 6 Bugemons.");
            }
        }
    }

    public List<Bugemon> getMembers() {
        return this.members;
    }

    public int size() {
        return this.members.size();
    }

    public boolean isEmpty() {
        return this.members.isEmpty();
    }

    public boolean isValid() {
        return !this.isEmpty() && this.size() <= MAX_PARTY_SIZE;
    }

    /**
     * Adds a Bugemon to the team
     *
     * @param bugemon the Bugemon to be added to the team
     * @return {@code true} if the Bugemon can be added, {@code false} if it cannot be added
     */
    public boolean add(Bugemon bugemon) {
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

    /**
     * Checks if the team contains the Bugemon
     *
     * @param bugemon the Bugemon to be checked
     * @return {@code true} if team contains the Bugemon, {@code false} otherwise
     */
    public boolean contains(Bugemon bugemon){
        if (bugemon == null){
            return false;
        }

        for (Bugemon b : this.members) {
            if (bugemon.equals(b)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes a Bugemon from the team based on its name
     *
     * @param name the name of the Bugemon to be removed
     * @return {@code true} if the Bugemon was removed, {@code false} otherwise
     */
    public boolean removeByName(String name) {
        if (name == null || name.isBlank()) {
            return false;
        }

        for (int i = 0; i < this.size(); i ++) {
            if (this.members.get(i).getName().equals(name)) {
                this.members.remove(i);
                return true;
            }
        }

        return false;
    }

    /**
	 * Checks if entire team is KO or not
	 *
	 * @return true if entire team is KO
	 */
	public boolean checkTeamKO(){
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

    private int getBugemonIndex(Bugemon bugemon) {
        return this.members.indexOf(bugemon);
    }

    /**
	 * Returns the next available Bugemon
	 * @param current the current Bugemon
	 * @return the next available Bugemon or null if none available
	 */
    public Bugemon getNextBugemon(Bugemon current) {
        int indexCurrent = this.getBugemonIndex(current);
        if (indexCurrent < 0) {
            return null;
        } 

        for (int i = indexCurrent+1; i % members.size() != indexCurrent; i++) {
			Bugemon candidate = members.get(i % this.members.size());
			if (!candidate.isKO()) {
				return candidate;
			}
		}

        return null;
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

    public boolean isBugemonOK(Bugemon bugemon) {
        return this.contains(bugemon) && !bugemon.isKO();
    }

	public Bugemon getFirstLevelUpBugemon(){
		for (Bugemon bugemon : this.getMembers()){
			if (bugemon.getRemainingReward() > 0){
				return bugemon;
			}
		}
		return null;
	}

	public int getLevelUpBugemonNumber(){
		int levelUpNumber = 0;
		for (Bugemon bugemon : this.getMembers()){
			if (bugemon.getRemainingReward() > 0){
				levelUpNumber++;
			} 	
		}	
		return levelUpNumber;
	}

	public Bugemon getBugemonById(String id){
		for (Bugemon bugemon : this.getMembers()){
			if (bugemon.getSpeciesId().equals(id)){
				return bugemon;
			}
		}
		return null;
	}
        
}
