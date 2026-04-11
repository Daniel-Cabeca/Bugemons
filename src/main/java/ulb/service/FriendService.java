package ulb.service;

import ulb.repository.LoadException;
import ulb.repository.database.AccountDatabaseRepository;
import ulb.repository.database.FriendDatabaseRepository;

import java.util.List;

public class FriendService {
    private final FriendDatabaseRepository friendRepository;
    private final AccountDatabaseRepository accountRepository;

    public FriendService(FriendDatabaseRepository friendRepository, AccountDatabaseRepository accountRepository) {
        this.friendRepository = friendRepository;
        this.accountRepository = accountRepository;
    }

    private int getUserId(String username) throws LoadException {
        int id = accountRepository.getUserId(username);
        if (id == -1) throw new LoadException("User not found: " + username);
        return id;
    }

    public void addFriend(String username, String friendUsername) throws LoadException {
        friendRepository.addFriend(getUserId(username), getUserId(friendUsername));
    }

    public List<String> getFriends(String username) throws LoadException {
        return friendRepository.getFriends(getUserId(username));
    }
}
