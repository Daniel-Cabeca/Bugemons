package ulb.controller.towerManager;

import org.junit.jupiter.api.Test;
import ulb.model.Player;
import ulb.model.bugemon.Bugemon;
import ulb.model.sample.BugemonSample;
import ulb.model.team.Team;
import ulb.model.tower.Room;
import ulb.model.tower.RoomType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RoomManagerTest {

    @Test
    void roomIsInitiallyNotCompleted() {
		Player player = new Player("TestPlayer");
		Bugemon a = BugemonSample.getA();
		Team teamA = new Team(List.of(a));
		player.setTeam(teamA);
        Room room = new Room(1, RoomType.BATTLE);
        RoomManager manager = new RoomManager(room, player);

        assertFalse(manager.isRoomCompleted());
        assertFalse(room.isRoomCompleted());
    }

    @Test
    void setRoomCompletedUpdatesUnderlyingRoom() {
		Player player = new Player("TestPlayer");
		Bugemon a = BugemonSample.getA();
		Team teamA = new Team(List.of(a));
		player.setTeam(teamA);
        Room room = new Room(1, RoomType.REWARD);
        RoomManager manager = new RoomManager(room, player);

        manager.setRoomCompleted(true);

        assertTrue(manager.isRoomCompleted());
        assertTrue(room.isRoomCompleted());
    }

	@Test
	void initializeNormalBattleRoomCreatesBattleControllerForBattleRoom() {
		Player player = new Player("TestPlayer");
		// give the player a minimal valid team
		Team team = new Team(List.of(new Bugemon(10, 10, 10, 10)));
		player.setTeam(team);

		Room room = new Room(1, RoomType.BATTLE);
		RoomManager manager = new RoomManager(room, player);

		assertNotNull(manager.getRoomBattleController(),
				"Battle controller should be initialized for a BATTLE room");
	}
}

