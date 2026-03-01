package ulb.model.team;

import java.util.ArrayList;
import java.util.List;

import ulb.model.Bugemon;

public class Team {
    public static final int MAX_PARTY_SIZE = 6;

    private List<Bugemon> members;

    public Team() {
        this.members = new ArrayList<>();
    }

    public Team(List<Bugemon> initalMembers) {
        this();

        if (initalMembers == null) {
            throw new IllegalArgumentException("Initial members list cannot be null.");
        }

        for (Bugemon bugemon : initalMembers) {
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

    public boolean remove(Bugemon bugemon) {
        if (bugemon == null) {
            return false;
        }
        return removeByName(bugemon.getName());
    }

	public boolean checkTeamKO(){
		boolean dead = true;
		for (int i = 0 ; i < 6 ; i++){
			if (!this.members.get(i).isKO()) {
				dead = false;
				return dead;
			}
		}
		return dead;
	}
}
