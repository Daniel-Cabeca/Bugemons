package ulb.service;

import ulb.repository.LoadException;
import ulb.repository.database.AccountDatabaseRepository;

public class AccountService {
    private final AccountDatabaseRepository repository;

    public AccountService(AccountDatabaseRepository repository) {
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
}
