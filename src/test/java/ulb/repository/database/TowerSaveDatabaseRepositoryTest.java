package ulb.repository.database;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import ulb.model.team.Team;
import ulb.repository.database.sql.DatabaseInMemory;
import ulb.repository.database.sql.DatabaseInitializer;

public class TowerSaveDatabaseRepositoryTest {

	private Team insertUserAndTeam(DatabaseInMemory database, String username, String teamName) {
        AccountDatabaseRepository accountRepository = new AccountDatabaseRepository(database);
        accountRepository.register(username, "password");
		TeamDatabaseRepository teamRepository = new TeamDatabaseRepository(database);
		Team team = new Team();
		team.setTeamName(teamName);
		teamRepository.insertTeam(username, team);
		return team;
    }

	@Test
	void addListInDatabase(){
		DatabaseInMemory database = new DatabaseInMemory();
        DatabaseInitializer databaseInitializer = new DatabaseInitializer(database);
        databaseInitializer.createTables();

		Team team = insertUserAndTeam(database, "player", "team_alpha");

		AccountDatabaseRepository accountRepository = new AccountDatabaseRepository(database);
		int userId = accountRepository.getUserId("player");

		TowerSaveDatabaseRepository towerSaveRepository = new TowerSaveDatabaseRepository(database);
		List<Integer> completedRoomsIdList = new ArrayList<>(List.of(1, 2, 3));
		assertDoesNotThrow(() -> towerSaveRepository.addTowerSave(userId, 4, completedRoomsIdList, team.getId()));
		
		List<Integer> result = towerSaveRepository.getCompletedRoomsId(userId);
		assertEquals(completedRoomsIdList, result);
		
	}
	@Test
	void updateInfo(){
		DatabaseInMemory database = new DatabaseInMemory();
		DatabaseInitializer databaseInitializer = new DatabaseInitializer(database);
		databaseInitializer.createTables();

		Team team = insertUserAndTeam(database, "player", "team_alpha");

		AccountDatabaseRepository accountRepository = new AccountDatabaseRepository(database);
		int userId = accountRepository.getUserId("player");

		TowerSaveDatabaseRepository towerSaveRepository = new TowerSaveDatabaseRepository(database);
		towerSaveRepository.addTowerSave(userId, 4, new ArrayList<>(List.of(1, 2, 3)), team.getId());

		ArrayList<Integer> completedRoomsId = new ArrayList<>(List.of(1, 3, 4));
		int currentFloorId = 5;
		assertDoesNotThrow(() ->towerSaveRepository.updateTowerSave(userId, currentFloorId, completedRoomsId, team.getId()));

		assertEquals(completedRoomsId, towerSaveRepository.getCompletedRoomsId(userId));
		assertEquals(currentFloorId, towerSaveRepository.getCurrentFloorId(userId));
		assertEquals(team.getId(), towerSaveRepository.getCurrentTeamId(userId));
	}
}
