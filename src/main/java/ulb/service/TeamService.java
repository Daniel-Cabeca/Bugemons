package ulb.service;

import ulb.model.bugemon.Bugemon;
import ulb.model.team.Team;
import ulb.exceptions.LoadException;
import ulb.repository.TeamRepository;

import java.util.List;

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
     * @param username the name of the user who saves the team
     * @param team the team to be saved
     * @throws LoadException if the saving fails
     */
    public void insertTeam(String username, Team team) throws LoadException {
        repository.insertTeam(username, team);
    }

    /**
     * Saves all the bugemons in the team
     * @param team the team containing the bugemons to be saved
     * @param teamId the id of the team
     * @throws LoadException if the saving fails
     */
    public void insertAllBugemonsInTeam(Team team, int teamId) throws LoadException {
        for (Bugemon bugemon : team.getMembers()) {
            repository.insertBugemonInTeam(bugemon, teamId);
        }
    }

    /**
     * Checks if a team with the same name for the same user already exists in the database
     *
     * @param teamName the name of the team
     * @param username the name of the user
     * @return true if a team with the same name already exists, false otherwise
     * @throws LoadException if the operation fails
     */
    public boolean teamExists(String teamName, String username) throws LoadException {
        return repository.teamExists(teamName, username);
    }

    /**
     * Inserts a user-specific bugemon in the database
     *
     * @param bugemon the bugemon to insert
     * @param username the user's name
     */
	public void insertUserBugemon(Bugemon bugemon, String username){
		repository.insertUserBugemon(bugemon, username);
	}

    /**
     * Returns a list of all the teams belonging to a user
     *
     * @param username the user's name
     * @return the list of the user's saved teams
     */
	public List<Team> getAllTeams(String username) {
		return repository.findAll(username);
	}
}
