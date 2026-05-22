package ulb.repository;

import ulb.exceptions.EntityNotFoundException;
import ulb.exceptions.LoadException;
import ulb.model.bugemon.Bugemon;
import ulb.model.team.Team;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Repository holding saved Bugemon teams.
 */
public interface TeamRepository {

	/**
	 * Saves the team of a user in the database
	 *
	 * @param userId the user
	 * @param team the team to be saved
	 * @param isTowerTeam tells if the team saved is the tower team of the user
	 * @throws LoadException if the operation fails
	 */
	void insertTeam(int userId, Team team, boolean isTowerTeam) throws LoadException;

	/**
	 * Updates the team of a user in the database
	 *
	 * @param userId the user
	 * @param team the team to be saved
	 * @throws LoadException if the operation fails
	 */
	void updateTowerTeam(int userId, Team team) throws LoadException;

	/**
	 * Delete the tower team of a user in the database
	 *
	 * @param userId the user
	 * @throws LoadException if the operation fails
	 */
	void deleteTowerTeam(int userId) throws LoadException;

	/**
	 * Tells if a tower team is saved for a user
	 *
	 * @param userId the user
	 * @return a boolean depending on if a tower team is saved
	 */
	boolean hasTowerTeam(int userId) throws LoadException;

	/**
	 * Inserts a bugemon in the team into the database
	 *
	 * @param bugemon the bugemon to be saved
	 * @param teamId the id of the team
	 * @throws LoadException if the operation fails
	 */
	void insertBugemonInTeam(Bugemon bugemon, int teamId) throws LoadException;

	/**
	 * delete a bugemon in the team from the database
	 *
	 * @param bugemon the bugemon to be deleted
	 * @param teamId the id of the team
	 * @throws LoadException if the operation fails
	 */
	void deleteBugemonInTeam(Bugemon bugemon, int teamId) throws LoadException;

	/**
	 * Checks if the user already has a team with the same name
	 *
	 * @param teamName the name of the team
	 * @param userId the id of the user
	 * @return true if the team name already exists, false otherwise
	 * @throws LoadException if the operation fails
	 */
	boolean teamExists(String teamName, int userId) throws LoadException;

	/**
	 * Inserts a bugemon into the bugemons table
	 *
	 * @param bugemon the bugemon to insert
	 * @param userId the id of the user who has the bugemon
	 * @throws LoadException if the operation fails
	 */
	void insertUserBugemon(Bugemon bugemon, int userId) throws LoadException;

	/**
	 * Updates a bugemon into the bugemons table
	 *
	 * @param bugemon the bugemon to update
	 * @param userId the id of the user who has the bugemon
	 * @throws LoadException if the operation fails
	 */
	void updateUserBugemon(Bugemon bugemon, int userId) throws LoadException;

	/**
	 * Deletes a bugemon from the bugemons table
	 *
	 * @param bugemon the bugemon to delete
	 * @param userId the id of the user who has the bugemon
	 * @throws LoadException if the operation fails
	 */
	void deleteUserBugemon(Bugemon bugemon, int userId) throws LoadException;

	/**
	 * Finds a bugemon in the bugemons table based on its id
	 *
	 * @param id the id of the bugemon to find
	 * @return the constructed Bugemon object
	 * @throws EntityNotFoundException if the bugemon is not found
	 */
	Bugemon findBugemon(int id) throws LoadException, EntityNotFoundException;

	/**
	 * Finds the team in the teams table based on its id
	 *
	 * @param id the id of the team to find
	 * @return the constructed Team object
	 * @throws NoSuchElementException if the team is not found
	 */
	Team findById(int id) throws LoadException, EntityNotFoundException;

	/**
	 * Finds all the teams belonging to a user
	 *
	 * @param username the name of the user whose teams are to be retrieved
	 * @return the list of Team objects
	 */
	List<Team> findAll(int userId) throws LoadException, EntityNotFoundException;

	/**
	 * Get the tower team saved for a user as an Optional object.
	 * The object is empty if no team is found.
	 *
	 * @param userId the user
	 * @return the tower team saved
	 */
	Optional<Team> getTowerTeam(int userId) throws LoadException, EntityNotFoundException;
}
