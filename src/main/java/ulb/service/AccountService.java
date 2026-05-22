package ulb.service;

import ulb.exceptions.*;
import ulb.repository.AccountRepository;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Service layer for account registration and authentication.
 */
public class AccountService {
	/** Repository holding all user accounts. */
	private final AccountRepository repository;

	/**
	 * Creates an account service using the provided repository.
	 *
	 * @param repository The repository used for account persistence
	 */
	public AccountService(AccountRepository repository) {
		this.repository = repository;
	}

	/**
	 * Registers a user account.
	 *
	 * @param username The account username
	 * @param password The password hash to store
	 * @return False if the username is already used, true otherwise
	 * @throws LoadException If the registration fails
	 * @throws UserAlreadyExistsException If the user already is registered in the database
	 */
	public void register(String username, String password) throws LoadException, UserAlreadyExistsException {
		repository.register(username, password);
	}

	/**
	 * Authenticates a user against stored credentials.
	 *
	 * @param username The account username
	 * @param password The provided password hash
	 * @return True if credentials match, false otherwise
	 * @throws LoadException If the lookup fails
	 * @throws EntityNotFoundException If no player is registered with the username
	 * @throws InvalidCredentialsException If the wrong password is given.
	 */
	public void login(String username, String password) throws LoadException, EntityNotFoundException,
			InvalidCredentialsException {
		String storedPassword = repository.getPasswordHash(username);
		if (!storedPassword.equals(password)) {
			throw new InvalidCredentialsException();
		}
	}

	/**
	 * Returns the internal identifier of a user account.
	 *
	 * @param username The account username
	 * @return The user id, or -1 if the user does not exist
	 * @throws NoSuchElementException If the lookup fails
	 */
	public Integer getUserId(String username) throws EntityNotFoundException {
		return repository.getUserId(username);
	}

	/**
	 * Returns the username of the player specified by a given id.
	 *
	 * @param userId The user's id
	 * @return The user's name
	 * @throws NoSuchElementException If no user has the given id
	 * @throws DataAccessException If some unexpected error occurs with the repository
	 */
	public String getUsername(int userId) throws EntityNotFoundException, DataAccessException {
		return this.repository.getUsername(userId);
	}

	/**
	 * Returns the list of a given player's friends.
	 *
	 * @param userId The id of the player whose friends to fetch.
	 * @return The list of his friends' usernames
	 * @throws LoadException If an error occurs with the repository
	 */
	public List<String> getFriendsList(int userId) throws LoadException {
		return repository.getFriendsList(userId);
	}

	/**
	 * Sets two users as friends.
	 *
	 * @param userId The id of one user
	 * @param friendId The id of the other user
	 * @throws LoadException If the operation fails
	 */
	public void addFriend(int userId, int friendId) throws LoadException {
		repository.addFriend(userId, friendId);
	}

	/**
	 * Create a friend request.
	 *
	 * @param senderId The id of the user sending the request
	 * @param receiverId The id of the user the request is sent to
	 * @throws LoadException If the operation fails
	 */
	public void sendFriendRequest(int senderId, int receiverId) throws LoadException {
		repository.sendFriendRequest(senderId, receiverId);
	}

	/**
	 * Fetches the list of friend requests that a given user has not yet responded to.
	 *
	 * @param receiverId The id of the user the requests to fetch were sent to
	 * @return The list of the usernames of the users who sent the request
	 * @throws LoadException If the operation fails
	 */
	public List<String> getPendingFriendRequests(int receiverId) throws LoadException {
		return repository.getPendingFriendRequests(receiverId);
	}

	/**
	 * Deletes a friend request from the repository and sets the two users as friends.
	 *
	 * @param senderId The id of the user who sent the request
	 * @param receiverId The id of the user the request was sent to
	 * @throws LoadException If the operation fails
	 */
	public void acceptFriendRequest(int senderId, int receiverId) throws LoadException {
		repository.acceptFriendRequest(senderId, receiverId);
	}

	/**
	 * Deletes a friend request from the repository.
	 *
	 * @param senderId The user who sent the request
	 * @param receiverId The user the request was sent to
	 * @throws LoadException If the operation fails
	 */
	public void declineFriendRequest(int senderId, int receiverId) throws LoadException {
		repository.declineFriendRequest(senderId, receiverId);
	}

	/**
	 * Creates a request for a multiplayer battle.
	 *
	 * @param senderId The user who sent the request
	 * @param receiverId The user the request is sent to
	 * @throws LoadException If the operation fails
	 */
	public void sendBattleRequest(int senderId, int receiverId) throws LoadException {
		repository.sendBattleRequest(senderId, receiverId);
	}

	/**
	 * Whether there are battle requests between two users that the receiver has not replied to yet.
	 *
	 * @param userIdA The id of one of the users
	 * @param userIdB The id of the other user
	 * @return True if there is at least one pending battle request between the two users, false otherwise
	 * @throws LoadException If the operation fails
	 */
	public boolean hasPendingBattleRequestBetween(int userIdA, int userIdB) throws LoadException {
		return repository.hasPendingBattleRequestBetween(userIdA, userIdB);
	}

	/**
	 * Fetches the list of battle requests that a given user has yet to respond to.
	 *
	 * @param receiverId The id of the user the requests were sent to
	 * @return The list of the usernames of the users who sent the requests
	 * @throws LoadException If the operation fails
	 */
	public List<String> getPendingBattleRequests(int receiverId) throws LoadException {
		return repository.getPendingBattleRequests(receiverId);
	}

	/**
	 * Deletes a battle request from the repository and starts the multiplayer battle.
	 *
	 * @param senderId The id of the user who sent the request
	 * @param receiverId The id of the user the request was sent to
	 * @throws LoadException If the operation fails
	 */
	public void acceptBattleRequest(int senderId, int receiverId) throws LoadException {
		repository.acceptBattleRequest(senderId, receiverId);
	}

	/**
	 * Deletes a battle request from the repository.
	 *
	 * @param senderId The id of the user who sent the request
	 * @param receiverId The id of the user the request was sent to
	 * @throws LoadException If the operation fails
	 */
	public void declineBattleRequest(int senderId, int receiverId) throws LoadException {
		repository.declineBattleRequest(senderId, receiverId);
	}

	/**
	 * Adds points to a user's score on the multiplayer battle leaderboard.
	 * @param userId The id of the user
	 * @param pointsToAdd The points to add
	 * @throws LoadException If the operation fails
	 */
	public void addPoints(int userId, int pointsToAdd) throws LoadException {
		repository.addPoints(userId, pointsToAdd);
	}

	/**
	 * Returns the score of each player for multiplayer battles.
	 *
	 * @return A map associating each registered player's username and his score
	 * @throws LoadException If the repository fails to load the scores
	 */
	public Map<String, Integer> getLeaderboard() throws LoadException {
		return repository.getLeaderboard();
	}
}
