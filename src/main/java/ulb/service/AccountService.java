package ulb.service;

import ulb.repository.LoadException;
import ulb.repository.AccountRepository;

import java.util.List;

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
     */
    public boolean register(String username, String password) throws LoadException {
        return repository.register(username, password);
    }

    /**
     * Authenticates a user against stored credentials.
     *
     * @param username The account username
     * @param password The provided password hash
     * @return True if credentials match, false otherwise
     * @throws LoadException If the lookup fails
     */
    public boolean login(String username, String password) throws LoadException {
        String storedPassword = repository.getPasswordHash(username);
        // null means the username doesn't exist
        if (storedPassword == null) return false;
        return storedPassword.equals(password);
    }

    /**
     * Returns the internal identifier of a user account.
     *
     * @param username The account username
     * @return The user id, or -1 if the user does not exist
     * @throws LoadException If the lookup fails
     */
    public int getUserId(String username) throws LoadException {
        return repository.getUserId(username);
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

	public List<String> getPendingRequests(int receiverId) throws LoadException {
		return repository.getPendingRequests(receiverId);
	}

	public void acceptFriendRequest(int senderId, int receiverId) throws LoadException {
		repository.acceptFriendRequest(senderId, receiverId);
	}

	public void declineFriendRequest(int senderId, int receiverId) throws LoadException {
		repository.declineFriendRequest(senderId, receiverId);
	}
}
