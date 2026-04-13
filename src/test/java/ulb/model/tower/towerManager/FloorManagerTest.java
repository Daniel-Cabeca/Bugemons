package ulb.model.tower.towerManager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import ulb.controller.action.UseAbility;
import ulb.model.Player;
import ulb.model.ability.Ability;
import ulb.model.battle.Battle;
import ulb.model.bugemon.Bugemon;
import ulb.model.team.Team;
import ulb.model.tower.Floor;
import ulb.model.tower.Room;
import ulb.model.tower.towerManager.FloorManager;
import ulb.model.type.Type;
import ulb.repository.BugemonSpeciesRepository;
import ulb.repository.mock.BugemonSpeciesMockRepository;
import ulb.repository.mock.InventoryMockRepository;import ulb.repository.mock.ItemMockRepository;import ulb.service.BugemonService;import ulb.service.ItemService;

public class FloorManagerTest {

	private Bugemon makeBugemon() {
		BugemonSpeciesRepository bugemonRepository = new BugemonSpeciesMockRepository();
		BugemonService bugemonService = new BugemonService(bugemonRepository);

		return bugemonService.spawnBugemon("florachu");
	}

    @Test
    void floorIsNotCompletedOnInitialisation() {
		BugemonService bugemonService = new BugemonService(new BugemonSpeciesMockRepository());
		ItemService itemService = new ItemService(new ItemMockRepository(), new InventoryMockRepository());

		Player player = new Player("TestPlayer", itemService);
        Floor floor = new Floor(1, false);
		Bugemon a = makeBugemon();
		Team teamA = new Team(List.of(a));
		player.setTeam(teamA);
        FloorManager manager = new FloorManager(floor, player, bugemonService, itemService);

        assertFalse(manager.isFloorCompleted());
        assertFalse(floor.isFloorCompleted());
    }

    @Test
    void floorIsCompletedWhenAllRoomsCompleted() {
		BugemonService bugemonService = new BugemonService(new BugemonSpeciesMockRepository());
		ItemService itemService = new ItemService(new ItemMockRepository(), new InventoryMockRepository());

		Player player = new Player("TestPlayer", itemService);
		Bugemon a = makeBugemon();
		Team teamA = new Team(List.of(a));
		player.setTeam(teamA);
        Floor floor = new Floor(1, false);
        FloorManager manager = new FloorManager(floor, player, bugemonService, itemService);

        for (Room room : floor.getRooms()) {
            room.setRoomCompleted(true);
        }

        assertTrue(manager.isFloorCompleted());
        assertTrue(floor.isFloorCompleted());
    }

    @Test
    void AdvanceToNextRoomWhenCurrentRoomCompletedAndFloorNotCompleted() {
		BugemonService bugemonService = new BugemonService(new BugemonSpeciesMockRepository());
		ItemService itemService = new ItemService(new ItemMockRepository(), new InventoryMockRepository());

		Player player = new Player("TestPlayer", itemService);
		Bugemon a = makeBugemon();
		Team teamA = new Team(List.of(a));
		player.setTeam(teamA);
        Floor floor = new Floor(1, false);
        FloorManager manager = new FloorManager(floor, player, bugemonService, itemService);

        int beforeIndex = manager.getCurrentRoomIndex();
        Room currentRoom = manager.getCurrentRoomManager().getRoom();

		Battle battle = manager.getCurrentBattle();
		battle.chooseAction(new UseAbility(new Ability("", "", Type.AQUA, "", 1000)), Battle.ParticipantLabel.TEAM_A);
		battle.chooseAction(new UseAbility(new Ability("", "", Type.AQUA, "", 0)), Battle.ParticipantLabel.TEAM_B);

		currentRoom.setRoomCompleted(true);
        manager.nextRoom();

        int afterIndex = manager.getCurrentRoomIndex();
        assertEquals(beforeIndex + 1, afterIndex);
        assertEquals(floor.getRooms().get(afterIndex), manager.getCurrentRoomManager().getRoom());
    }
}

