package ulb.repository.database;

import org.junit.jupiter.api.Test;
import ulb.model.team.Team;
import ulb.repository.database.sql.DatabaseInMemory;
import ulb.repository.database.sql.DatabaseInitializer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TeamDatabaseRepositoryTest {

    private void insertUser(DatabaseInMemory database, String username) {
        AccountDatabaseRepository accountRepository = new AccountDatabaseRepository(database);
        accountRepository.register(username, "password");
    }

    @Test
    public void findByIdGivesCorrectTeamName() {
        DatabaseInMemory database = new DatabaseInMemory();
        DatabaseInitializer databaseInitializer = new DatabaseInitializer(database);
        databaseInitializer.createTables();

        TeamDatabaseRepository repository = new TeamDatabaseRepository(database);
        insertUser(database, "player");

        Team team = new Team();
        team.setTeamName("Team_Alpha");
        repository.insertTeam("player", team);

        Team obtained = repository.findById(team.getId());

        assertEquals("Team_Alpha", obtained.getTeamName());
    }

    @Test
    public void insertTeamThrowsExceptionWithUnknownPlayer() {
        DatabaseInMemory database = new DatabaseInMemory();
        DatabaseInitializer databaseInitializer = new DatabaseInitializer(database);
        databaseInitializer.createTables();
        TeamDatabaseRepository repository = new TeamDatabaseRepository(database);
        insertUser(database, "player");
        Team team = new Team();
        team.setTeamName("Team_Alpha");

        assertThrows(Exception.class,
                () -> repository.insertTeam("ghost", team));
    }

	@Test
	public void findAllGivesCorrectTeamNumber() {

		DatabaseInMemory database = new DatabaseInMemory();
		DatabaseInitializer databaseInitializer = new DatabaseInitializer(database);
		databaseInitializer.createTables();

		TeamDatabaseRepository repository = new TeamDatabaseRepository(database);

		insertUser(database,  "player");

        Team teamA = new Team();
        teamA.setTeamName("Team_Alpha");
        Team teamB = new Team();
        teamB.setTeamName("Team_Beta");

		repository.insertTeam("player", teamA);
		repository.insertTeam("player", teamB);

		List<Team> obtained = repository.findAll("player");

		assertEquals(2, obtained.size());
		assertNotNull(obtained.get(0));
		assertNotNull(obtained.get(1));
	}

    @Test
    public void findAllGivesCorrectTeamNames() {

        DatabaseInMemory database = new DatabaseInMemory();
        DatabaseInitializer databaseInitializer = new DatabaseInitializer(database);
        databaseInitializer.createTables();

        TeamDatabaseRepository repository = new TeamDatabaseRepository(database);

        insertUser(database, "player");

        Team teamA = new Team();
        teamA.setTeamName("Team_Alpha");
        Team teamB = new Team();
        teamB.setTeamName("Team_Beta");

        repository.insertTeam("player", teamA);
        repository.insertTeam("player", teamB);


        List<Team> obtained = repository.findAll("player");

        assertEquals("Team_Alpha", obtained.getFirst().getTeamName());
        assertEquals("Team_Beta", obtained.get(1).getTeamName());
    }

    @Test
    public void findAllReturnsOnlyTeamsOfGivenPlayer() {

        DatabaseInMemory database = new DatabaseInMemory();
        DatabaseInitializer databaseInitializer = new DatabaseInitializer(database);
        databaseInitializer.createTables();

        TeamDatabaseRepository repository = new TeamDatabaseRepository(database);

        insertUser(database,  "player");
        insertUser(database,  "player2");

        Team teamA = new Team();
        teamA.setTeamName("Team_Alpha");
        Team teamB = new Team();
        teamB.setTeamName("Team_Beta");
        Team teamC = new Team();
        teamC.setTeamName("Team_Alpha");

        repository.insertTeam("player", teamA);
        repository.insertTeam("player", teamB);
        repository.insertTeam("player2", teamC);

        assertEquals(2, repository.findAll("player").size());
    }
}
