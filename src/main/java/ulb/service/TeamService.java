package ulb.service;

import ulb.model.bugemon.Bugemon;
import ulb.model.team.Team;
import ulb.repository.LoadException;
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
     * @param teamRepository The repository used for team persistence
     */
    public TeamService(TeamRepository teamRepository) {
        this.repository = teamRepository;
    }

    /**
     * Saves a new team
     * @param username the name of the user who saves the team
     * @param teamName the name of the team to be saved
     * @throws LoadException if the saving fails
     */
    public void insertTeam(String username, String teamName) throws LoadException {
        repository.insertTeam(username, teamName);
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

    public int getTeamId(String teamName, String username) throws LoadException {
        return repository.getTeamId(teamName, username);
    }

	public void insertUserBugemon(Bugemon bugemon, String username){
		repository.insertUserBugemon(bugemon, username);
	}

	public List<Team> getAllTeams(String username) {
		return repository.findAll(username);
	}

	public Team getTeam(String teamName, String username) {
		int teamId = repository.getTeamId(teamName, username);
		return repository.findById(teamId);
	}
}
