package ulb.repository;

import ulb.exceptions.DataAccessException;
import ulb.exceptions.EntityNotFoundException;
import ulb.exceptions.LoadException;
import ulb.exceptions.UserAlreadyExistsException;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Repository for the user accounts.
 */
public interface AccountRepository {

	/**
	 * Registers a new user account.
	 *
	 * @param username The account username
	 * @param password The password hash to store
	 * @return False if the username is already taken, true otherwise
	 * @throws LoadException If the user cannot be persisted
	 * @throws UserAlreadyExistsException If the user already is registered in the database
	 */
	void register(String username, String password) throws LoadException, UserAlreadyExistsException;


	/**
	 * Returns the password hash associated with a username.
	 *
	 * @param username The account username
	 * @return The stored password hash, or null if not found
	 * @throws LoadException If the lookup fails
	 * @throws EntityNotFoundException If no match was found for the username
	 */
	String getPasswordHash(String username) throws LoadException, EntityNotFoundException;


	/**
	 * Returns the internal user identifier associated with a username.
	 *
	 * @param username The account username
	 * @return The user id, or -1 if not found
	 * @throws NoSuchElementException If the lookup fails
	 */
	Integer getUserId(String username) throws EntityNotFoundException;

	/**
	 * Returns the username of the player specified by a given id.
	 *
	 * @param userId The user's id
	 * @return The user's name
	 * @throws NoSuchElementException If no user has the given id
	 * @throws DataAccessException If some unexpected SQL error occurs
	 */
	String getUsername(int userId) throws EntityNotFoundException, DataAccessException;

	/**
	 * Gives a list of all the users added as friends for a given user.
	 *
	 * @param userId The id of the user whose friends to fetch
	 * @return The list of friends of the user whose id was provided
	 * @throws LoadException If the friends list could not be fetched
	 */
	List<String> getFriendsList(int userId) throws LoadException;

	/**
	 * Registers a user as another user's friend.
	 *
	 * @param userId The user for whom to add the friend
	 * @param friendId The user to register as friend
	 * @throws LoadException If the operation failed
	 */
	void addFriend(int userId, int friendId) throws LoadException;

	/**
	 * Creates a friend request.
	 *
	 * @param senderId The id of the user sending the request
	 * @param receiverId The id of the user the request is sent to
	 * @throws LoadException If the operation fails
	 */
	void sendFriendRequest(int senderId, int receiverId) throws LoadException;

	void sendBattleRequest(int senderId, int receiverId) throws LoadException;
	boolean hasPendingBattleRequestBetween(int userIdA, int userIdB) throws LoadException;
	List<String> getPendingBattleRequests(int receiverId) throws LoadException;
	void acceptBattleRequest(int senderId, int receiverId) throws LoadException;

	/**
	 * Gets the list of friend requests that have not been handled for a given player.
	 *
	 * @param receiverId The id of the user whose pending requests to fetch
	 * @return The list of the usernames of the pending friend requests
	 * @throws LoadException If the operation fails
	 */
	List<String> getPendingFriendRequests(int receiverId) throws LoadException;

	/**
	 * Accept a friend request.
	 *
	 * @param senderId The id of the user who sent the request
	 * @param receiverId The id of the user who accepts the request
	 * @throws LoadException If the operation fails
	 */
	void acceptFriendRequest(int senderId, int receiverId) throws LoadException;

	/**
	 * Decline a friend request.
	 *
	 * @param senderId The id of the user who sent the request
	 * @param receiverId The id of the user who declines the request
	 * @throws LoadException If the operation fails
	 */
	void declineFriendRequest(int senderId, int receiverId) throws LoadException;

	void declineBattleRequest(int senderId, int receiverId) throws LoadException;

	void addPoints(int userId, int pointsToAdd) throws LoadException;

	/**
	 * Gets the list of all the players' score for multiplayer battles.
	 *
	 * @return A map associating each player's username to his score
	 * @throws LoadException If the repository fails to load the scores.
	 */
	Map<String, Integer> getLeaderboard() throws LoadException;
}
