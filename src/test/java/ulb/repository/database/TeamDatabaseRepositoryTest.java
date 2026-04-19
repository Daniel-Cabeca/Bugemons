package ulb.repository.database;

import org.junit.jupiter.api.Test;
import ulb.model.bugemon.Bugemon;
import ulb.model.team.Team;
import ulb.repository.BugemonSpeciesRepository;
import ulb.repository.database.sql.DatabaseInMemory;
import ulb.repository.database.sql.DatabaseInitializer;
import ulb.repository.database.sql.DatabaseMock;
import ulb.repository.mock.BugemonSpeciesMockRepository;
import ulb.service.BugemonService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TeamDatabaseRepositoryTest {

    private Bugemon makeBugemon() {
        BugemonSpeciesRepository bugemonRepository = new BugemonSpeciesMockRepository();
        BugemonService bugemonService = new BugemonService(bugemonRepository);

        return bugemonService.spawnBugemon("florachu");
    }

    private void insertUser(DatabaseInMemory database) {
        AccountDatabaseRepository accountRepository = new AccountDatabaseRepository(database);
        accountRepository.register("player", "password");
    }

    @Test
    public void insertTeamWorks() {
        DatabaseMock databaseMock = new DatabaseMock();
        TeamDatabaseRepository teamRepository = new TeamDatabaseRepository(databaseMock);
        insertUser(databaseMock);

        teamRepository.insertTeam("player", "team");
        assertTrue(true); // TO DO
    }

    @Test
    public void insertAllBugemonsInTeamWorks() {
        DatabaseInMemory database = new DatabaseInMemory();
        DatabaseInitializer databaseInitializer = new DatabaseInitializer(database);
        databaseInitializer.createTables();

        TeamDatabaseRepository teamRepository = new TeamDatabaseRepository(database);

        Bugemon bugemon = makeBugemon();
        Team team = new Team(new ArrayList<>(List.of(bugemon)));

        int teamId = teamRepository.getTeamId("team", "player");
        teamRepository.insertBugemonInTeam(bugemon, teamId);
        assertTrue(true);
    }
}
