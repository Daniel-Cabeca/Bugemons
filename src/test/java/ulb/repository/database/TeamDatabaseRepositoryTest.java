package ulb.repository.database;

import org.junit.jupiter.api.Test;
import ulb.model.team.Team;
import ulb.repository.database.sql.DatabaseInMemory;
import ulb.repository.database.sql.DatabaseInitializer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TeamDatabaseRepositoryTest {
	int userId1 = -1;
	int userId2 = -1;

    private void insertUser(DatabaseInMemory database, String username, boolean isUser1) throws Exception {
        AccountDatabaseRepository accountRepository = new AccountDatabaseRepository(database);
        accountRepository.register(username, "password");
		if (isUser1){
			this.userId1 = accountRepository.getUserId(username);
		} else {
			this.userId2 = accountRepository.getUserId(username);
		}
		
    }

    @Test
    public void findByIdGivesCorrectTeamName() throws Exception {
        DatabaseInMemory database = new DatabaseInMemory();
        DatabaseInitializer databaseInitializer = new DatabaseInitializer(database);
        databaseInitializer.createTables();

        TeamDatabaseRepository repository = new TeamDatabaseRepository(database);
        insertUser(database, "player", true);

        Team team = new Team();
        team.setTeamName("Team_Alpha");
        repository.insertTeam(this.userId1, team, false);

        Team obtained = repository.findById(team.getId());

        assertEquals("Team_Alpha", obtained.getTeamName());
    }

    @Test
    public void insertTeamThrowsExceptionWithUnknownPlayer() throws Exception {
        DatabaseInMemory database = new DatabaseInMemory();
        DatabaseInitializer databaseInitializer = new DatabaseInitializer(database);
        databaseInitializer.createTables();
        TeamDatabaseRepository repository = new TeamDatabaseRepository(database);
        insertUser(database, "player", true);
        Team team = new Team();
        team.setTeamName("Team_Alpha");
		
        assertThrows(Exception.class,
                () -> repository.insertTeam(this.userId2, team, false));
    }

	@Test
	public void findAllGivesCorrectTeamNumber() throws Exception {

		DatabaseInMemory database = new DatabaseInMemory();
		DatabaseInitializer databaseInitializer = new DatabaseInitializer(database);
		databaseInitializer.createTables();

		TeamDatabaseRepository repository = new TeamDatabaseRepository(database);

		insertUser(database,  "player", true);

        Team teamA = new Team();
        teamA.setTeamName("Team_Alpha");
        Team teamB = new Team();
        teamB.setTeamName("Team_Beta");

		repository.insertTeam(this.userId1, teamA, false);
		repository.insertTeam(this.userId1, teamB, false);

		List<Team> obtained = repository.findAll(this.userId1);

		assertEquals(2, obtained.size());
		assertNotNull(obtained.get(0));
		assertNotNull(obtained.get(1));
	}

    @Test
    public void findAllGivesCorrectTeamNames() throws Exception {

        DatabaseInMemory database = new DatabaseInMemory();
        DatabaseInitializer databaseInitializer = new DatabaseInitializer(database);
        databaseInitializer.createTables();

        TeamDatabaseRepository repository = new TeamDatabaseRepository(database);

        insertUser(database, "player", true);

        Team teamA = new Team();
        teamA.setTeamName("Team_Alpha");
        Team teamB = new Team();
        teamB.setTeamName("Team_Beta");

        repository.insertTeam(this.userId1, teamA, false);
        repository.insertTeam(this.userId1, teamB, false);


        List<Team> obtained = repository.findAll(this.userId1);

        assertEquals("Team_Alpha", obtained.getFirst().getTeamName());
        assertEquals("Team_Beta", obtained.get(1).getTeamName());
    }

    @Test
    public void findAllReturnsOnlyTeamsOfGivenPlayer() throws Exception {

        DatabaseInMemory database = new DatabaseInMemory();
        DatabaseInitializer databaseInitializer = new DatabaseInitializer(database);
        databaseInitializer.createTables();

        TeamDatabaseRepository repository = new TeamDatabaseRepository(database);

        insertUser(database,  "player", true);
        insertUser(database,  "player2", false);

        Team teamA = new Team();
        teamA.setTeamName("Team_Alpha");
        Team teamB = new Team();
        teamB.setTeamName("Team_Beta");
        Team teamC = new Team();
        teamC.setTeamName("Team_Alpha");

        repository.insertTeam(this.userId1, teamA, false);
        repository.insertTeam(this.userId1, teamB, false);
        repository.insertTeam(this.userId2, teamC, false);

        assertEquals(2, repository.findAll(this.userId1).size());
    }
}
