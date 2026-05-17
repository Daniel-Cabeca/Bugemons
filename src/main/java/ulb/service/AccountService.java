package ulb.service;

import ulb.exceptions.DataAccessException;
import ulb.exceptions.EntityNotFoundException;
import ulb.exceptions.InvalidCredentialsException;
import ulb.exceptions.LoadException;
import ulb.exceptions.UserAlreadyExistsException;
import ulb.repository.AccountRepository;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Service layer for account registration and authentication.
 */
public class AccountService {
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
        // return repository.register(username, password);
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
    public void login(String username, String password) throws LoadException, EntityNotFoundException, InvalidCredentialsException{
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

	public List<String> getFriendsList(int userId) throws LoadException {
		return repository.getFriendsList(userId);
	}

	public void addFriend(int userId, int friendId) throws LoadException {
		repository.addFriend(userId, friendId);
	}

	public void sendFriendRequest(int senderId, int receiverId) throws LoadException {
		repository.sendFriendRequest(senderId, receiverId);
	}

	public List<String> getPendingFriendRequests(int receiverId) throws LoadException {
		return repository.getPendingFriendRequests(receiverId);
	}

	public void acceptFriendRequest(int senderId, int receiverId) throws LoadException {
		repository.acceptFriendRequest(senderId, receiverId);
	}

	public void declineFriendRequest(int senderId, int receiverId) throws LoadException {
		repository.declineFriendRequest(senderId, receiverId);
	}

	public void sendBattleRequest(int senderId, int receiverId) throws LoadException {
		repository.sendBattleRequest(senderId, receiverId);
	}

	public boolean hasPendingBattleRequestBetween(int userIdA, int userIdB) throws LoadException {
		return repository.hasPendingBattleRequestBetween(userIdA, userIdB);
	}

	public List<String> getPendingBattleRequests(int receiverId) throws LoadException {
		return repository.getPendingBattleRequests(receiverId);
	}

	public void acceptBattleRequest(int senderId, int receiverId) throws LoadException {
		repository.acceptBattleRequest(senderId, receiverId);
	}

	public void declineBattleRequest(int senderId, int receiverId) throws LoadException {
		repository.declineBattleRequest(senderId, receiverId);
	}
	public void addPoints(int userId, int pointsToAdd) throws LoadException{
		repository.addPoints(userId,pointsToAdd);
	}

	public  Map<String, Integer> getLeaderboard() throws LoadException{
		return repository.getLeaderboard();
	}
}
