package ulb.service;

import ulb.model.Player;
import ulb.model.bugemon.Bugemon;
import ulb.model.team.Team;
import ulb.exceptions.EntityNotFoundException;
import ulb.exceptions.LoadException;
import ulb.repository.TeamRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for team saving and loading.
 */
public class TeamService {

    private final TeamRepository repository;

    /**
     * Creates a team service using the provided repository.
     *
     * @param teamRepository the repository used for team persistence
     */
    public TeamService(TeamRepository teamRepository) {
        this.repository = teamRepository;
    }

    /**
     * Saves a new team
     * @param player the user who saves the team
     * @param team the team to be saved
     * @throws LoadException if the saving fails
     */
    public void insertTeam(Player player, Team team) throws LoadException {
		Optional<Integer> userId = player.getUserId();

		if (userId.isEmpty()){
			return;
		}

		for (Bugemon b : team.getMembers()) {
			insertUserBugemon(b, player);
		}
        repository.insertTeam(userId.get(), team, false);
		insertAllBugemonsInTeam(team);
    }

	/**
     * Saves a new team as the team used for the tower
     * @param player the user who saves the team
     * @throws LoadException if the saving fails
     */
    public void insertTowerTeam(Player player) throws LoadException {
		Optional<Integer> userId = player.getUserId();

		if (userId.isEmpty()){
			return;
		}
		Team team = player.getTeam();
		if (repository.hasTowerTeam(userId.get())){
			for (Bugemon b : team.getMembers()) {
				updateUserBugemon(b, player);
			}
			repository.updateTowerTeam(userId.get(), team);
		} else {
			for (Bugemon b : team.getMembers()) {
				insertUserBugemon(b, player);
			}
        	repository.insertTeam(userId.get(), team, true);
			insertAllBugemonsInTeam(team);
		}
    }

	/**
     * deletes the team used for the tower
     * @param player the user whose team is deleted
     * @throws LoadException if the deleting fails
     */
	public void deleteTowerTeam(Player player) throws LoadException, EntityNotFoundException {
		Optional<Integer> userId = player.getUserId();

		if (userId.isEmpty()){
			return;
		}

		if (repository.hasTowerTeam(userId.get())){
			Optional<Team> team = getTowerTeam(player);

			for (Bugemon b : team.get().getMembers()) {
				repository.deleteUserBugemon(b, userId.get());
				repository.deleteBugemonInTeam(b, team.get().getId());
			}

			repository.deleteTowerTeam(userId.get());
		}
	}

    /**
     * Saves all the bugemons in the team
     * @param team the team containing the bugemons to be saved
     * @throws LoadException if the saving fails
     */
    public void insertAllBugemonsInTeam(Team team) throws LoadException {
        for (Bugemon bugemon : team.getMembers()) {
            repository.insertBugemonInTeam(bugemon, team.getId());
        }
    }

    /**
     * Checks if a team with the same name for the same user already exists in the database
     *
     * @param teamName the name of the team
     * @param player the user
     * @return true if a team with the same name already exists, false otherwise
     * @throws LoadException if the operation fails
     */
    public boolean teamExists(String teamName, Player player) throws LoadException {
		Optional<Integer> userId = player.getUserId();

		if (userId.isPresent()){
			return false;
		}

        return repository.teamExists(teamName, userId.get());
    }

    /**
     * Inserts a user-specific bugemon in the database
     *
     * @param bugemon the bugemon to insert
     * @param player the user
     */
	public void insertUserBugemon(Bugemon bugemon, Player player) throws LoadException {
		Optional<Integer> userId = player.getUserId();

		if (userId.isEmpty()){
			return;
		}

		repository.insertUserBugemon(bugemon, userId.get());
	}

	/**
     * Updates a user-specific bugemon in the database
     *
     * @param bugemon the bugemon to insert
     * @param player the user
     */
	public void updateUserBugemon(Bugemon bugemon, Player player) throws LoadException {
		Optional<Integer> userId = player.getUserId();

		if (userId.isEmpty()){
			return;
		}

		repository.updateUserBugemon(bugemon, userId.get());
	}

    /**
     * Returns a list of all the teams belonging to a user
     *
     * @param username the user's name
     * @return the list of the user's saved teams
     */
	public List<Team> getAllTeams(Player player) throws LoadException, EntityNotFoundException {
		Optional<Integer> userId = player.getUserId();

		if (userId.isEmpty()){
			return List.of();
		}

		return repository.findAll(userId.get());
	}

	/**
	 * Get the team saved as the tower team for a player
	 * @param player the user
	 * @return the team saved
	 */
	public Optional<Team> getTowerTeam(Player player) throws LoadException, EntityNotFoundException {
		Optional<Integer> userId = player.getUserId();

		if (userId.isPresent()){
			return repository.getTowerTeam(userId.get());
		}
		return Optional.empty();
	}
}
