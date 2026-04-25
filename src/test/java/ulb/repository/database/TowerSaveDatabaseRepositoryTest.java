package ulb.repository.database;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import ulb.repository.database.sql.DatabaseInMemory;
import ulb.repository.database.sql.DatabaseInitializer;

public class TowerSaveDatabaseRepositoryTest {

	private void insertUserAndTeam(DatabaseInMemory database, String username, String teamName) {
        AccountDatabaseRepository accountRepository = new AccountDatabaseRepository(database);
        accountRepository.register(username, "password");
		TeamDatabaseRepository teamRepository = new TeamDatabaseRepository(database);
		teamRepository.insertTeam(username, teamName);
    }

	@Test
	void addListInDatabase(){
		DatabaseInMemory database = new DatabaseInMemory();
        DatabaseInitializer databaseInitializer = new DatabaseInitializer(database);
        databaseInitializer.createTables();

        insertUserAndTeam(database, "player", "team_alpha");

		AccountDatabaseRepository accountRepository = new AccountDatabaseRepository(database);
		TeamDatabaseRepository teamRepository = new TeamDatabaseRepository(database);
		int userId = accountRepository.getUserId("player");
		int teamId = teamRepository.getTeamId("team_alpha", "player");

		TowerSaveDatabaseRepository towerSaveRepository = new TowerSaveDatabaseRepository(database);
		List<Integer> completedRoomsIdList = new ArrayList<>(List.of(1, 2, 3));
		assertDoesNotThrow(() -> towerSaveRepository.addTowerSave(userId, 4, completedRoomsIdList, teamId));
		
		List<Integer> result = towerSaveRepository.getCompletedRoomsId(userId);
		assertEquals(completedRoomsIdList, result);
		
	}
}
