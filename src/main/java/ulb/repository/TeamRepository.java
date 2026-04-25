package ulb.repository;

import ulb.model.bugemon.Bugemon;
import ulb.model.team.Team;

import java.util.List;
import java.util.NoSuchElementException;

public interface TeamRepository {

    /**
     * Saves the team in the database
     * @param teamName the team's to be saved name
     * @throws LoadException if the operation fails
     */
    void insertTeam(String username, String teamName) throws LoadException;

    /**
     * Inserts a bugemon in the team into the database
     * @param bugemon the bugemon to be saved
     * @param teamId the id of the team
     * @throws LoadException if the operation fails
     */
    void insertBugemonInTeam(Bugemon bugemon, int teamId) throws LoadException;

    /**
     * Gets the team id
     * @param teamName the name of the team
     * @param username the username of the user who saved the team
     * @return the id of the team
     * @throws LoadException if the operation fails
     */
    int getTeamId(String teamName, String username) throws LoadException;

    /**
     * Inserts a bugemon into the bugemons table
     *
     * @param bugemon the bugemon to insert
     * @param username the name of the user who has the bugemon
     * @throws LoadException if the operation fails
     */
	void insertUserBugemon(Bugemon bugemon, String username) throws LoadException;

    /**
     * Finds a bugemon in the bugemons table based on its id
     *
     * @param id the id of the bugemon to find
     * @return the constructed Bugemon object
     * @throws NoSuchElementException if the bugemon is not found
     */
	Bugemon findBugemon(int id) throws NoSuchElementException;

    /**
     * Finds the team in the teams table based on its id
     *
     * @param id the id of the team to find
     * @return the constructed Team object
     * @throws NoSuchElementException if the team is not found
     */
	Team findById(int id) throws NoSuchElementException;

    /**
     * Finds all the teams belonging to a user
     *
     * @param username the name of the user whose teams are to be retrieved
     * @return the list of Team objects
     */
	List<Team> findAll(String username);
}
