package ulb.service;

import ulb.repository.LoadException;
import ulb.repository.AccountRepository;

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

    public boolean isFirstLogin(String username) throws LoadException {
        return repository.isFirstLogin(username);
    }

    public String[] getPlayerProfile(String username) throws LoadException {
        return repository.getPlayerProfile(username);
    }

    public void savePlayerProfile(String username, String playerName, String gender) throws LoadException {
        repository.savePlayerProfile(username, playerName, gender);
    }
}
