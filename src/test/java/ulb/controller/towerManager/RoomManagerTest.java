package ulb.controller.towerManager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import ulb.model.Player;
import ulb.model.bugemon.Bugemon;
import ulb.model.team.Team;
import ulb.model.tower.Room;
import ulb.model.tower.RoomType;

import ulb.repository.BugemonSpeciesRepository;
import ulb.repository.mock.BugemonSpeciesMockRepository;
import ulb.service.BugemonService;

public class RoomManagerTest {

	private Bugemon makeBugemon() {
		BugemonSpeciesRepository bugemonRepository = new BugemonSpeciesMockRepository();
		BugemonService bugemonService = new BugemonService(bugemonRepository);

		return bugemonService.spawnBugemon("florachu");
	}

    @Test
    void roomNotCompletedOnInitialisation() {
		Player player = new Player("TestPlayer");
		Bugemon a = makeBugemon();
		Team teamA = new Team(List.of(a));
		player.setTeam(teamA);
        Room room = new Room(1, RoomType.BATTLE);
        RoomManager manager = new RoomManager(room, 1, player);

        assertFalse(manager.isRoomCompleted());
        assertFalse(room.isRoomCompleted());
    }

    @Test
    void setRoomCompletedUpdatesManagerAndRoom() {
		Player player = new Player("TestPlayer");
		Bugemon a = makeBugemon();
		Team teamA = new Team(List.of(a));
		player.setTeam(teamA);
        Room room = new Room(1, RoomType.REWARD);
        RoomManager manager = new RoomManager(room, 1, player);

        manager.setRoomCompleted(true);

        assertTrue(manager.isRoomCompleted());
        assertTrue(room.isRoomCompleted());
    }

	@Test
	void createBattleRoomInitializesBattleController() {
		Player player = new Player("TestPlayer");
		// give the player a minimal valid team
		Team team = new Team(List.of(makeBugemon()));
		player.setTeam(team);

		Room room = new Room(1, RoomType.BATTLE);
		RoomManager manager = new RoomManager(room, 1, player);

		assertNotNull(manager.getRoomBattleController(),
				"Battle controller should be initialized for a BATTLE room");
	}
}

