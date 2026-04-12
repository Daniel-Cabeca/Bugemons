package ulb.service;

import ulb.repository.LoadException;
import ulb.repository.AccountRepository;

import java.util.List;

public class AccountService {
    private final AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    public boolean register(String username, String password) throws LoadException {
        return repository.register(username, password);
    }

    public boolean login(String username, String password) throws LoadException {
        String storedPassword = repository.getPasswordHash(username);
        // null means the username doesn't exist
        if (storedPassword == null) return false;
        return storedPassword.equals(password);
    }

    public int getUserId(String username) throws LoadException {
        return repository.getUserId(username);
    }

	public List<String> getFriendsList(int userId) throws LoadException {
		return repository.getFriendsList(userId);
	}
	public void addFriend(int userId, int friendId) throws LoadException {
		repository.addFriend(userId, friendId);
	}

	public void sendFriendRequest(int requesterId, int addresseeId) throws LoadException {
		repository.sendFriendRequest(requesterId, addresseeId);
	}

	public List<AccountRepository.PendingFriendRequest> getPendingFriendRequestsForAddressee(int addresseeUserId)
			throws LoadException {
		return repository.getPendingFriendRequestsForAddressee(addresseeUserId);
	}

	public void acceptFriendRequest(int addresseeId, int requesterId) throws LoadException {
		repository.acceptFriendRequest(addresseeId, requesterId);
	}

	public void declineFriendRequest(int addresseeId, int requesterId) throws LoadException {
		repository.declineFriendRequest(addresseeId, requesterId);
	}
}
