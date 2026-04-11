package ulb.service;

import ulb.model.team.Team;
import ulb.repository.LoadException;
import ulb.repository.database.AccountDatabaseRepository;
import ulb.repository.database.PlayerDatabaseRepository;

import java.util.List;

public class PlayerService {
    private final PlayerDatabaseRepository playerRepository;
    private final AccountDatabaseRepository accountRepository;

    public PlayerService(PlayerDatabaseRepository playerRepository, AccountDatabaseRepository accountRepository) {
        this.playerRepository = playerRepository;
        this.accountRepository = accountRepository;
    }

    public void saveTeam(String username, Team team) throws LoadException {
        int userId = accountRepository.getUserId(username);
        if (userId == -1) throw new LoadException("User not found: " + username);
        playerRepository.saveTeam(userId, team);
    }

    public List<String> loadTeamSpeciesIds(String username) throws LoadException {
        int userId = accountRepository.getUserId(username);
        if (userId == -1) throw new LoadException("User not found: " + username);
        return playerRepository.loadTeamSpeciesIds(userId);
    }
}
