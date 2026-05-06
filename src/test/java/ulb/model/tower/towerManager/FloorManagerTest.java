package ulb.model.tower.towerManager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import ulb.model.Player;
import ulb.model.ability.Ability;
import ulb.model.action.UseAbility;
import ulb.model.battle.Battle;
import ulb.model.bugemon.Bugemon;
import ulb.model.team.Team;
import ulb.model.tower.Floor;
import ulb.model.tower.Room;
import ulb.model.type.Type;
import ulb.repository.BugemonSpeciesRepository;
import ulb.repository.mock.BugemonSpeciesMockRepository;
import ulb.repository.mock.StartingInventoryMockRepository;import ulb.repository.mock.ItemMockRepository;import ulb.service.BugemonService;import ulb.service.ItemService;

public class FloorManagerTest {

	private Bugemon makeBugemon() {
		BugemonSpeciesRepository bugemonRepository = new BugemonSpeciesMockRepository();
		BugemonService bugemonService = new BugemonService(bugemonRepository);

		return bugemonService.spawnBugemon("florachu");
	}

    @Test
    void floorIsNotCompletedOnInitialisation() {
		BugemonService bugemonService = new BugemonService(new BugemonSpeciesMockRepository());
		ItemService itemService = new ItemService(new ItemMockRepository(), new StartingInventoryMockRepository());

		Player player = new Player();
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
		ItemService itemService = new ItemService(new ItemMockRepository(), new StartingInventoryMockRepository());

		Player player = new Player();
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
	void doesntMoveToNextRoomIfPreviousNotCompleted() {
		BugemonService bugemonService = new BugemonService(new BugemonSpeciesMockRepository());
		ItemService itemService = new ItemService(new ItemMockRepository(), new StartingInventoryMockRepository());

		Player player = new Player();
        Floor floor = new Floor(1, false);
		Bugemon a = makeBugemon();
		Team teamA = new Team(List.of(a));
		player.setTeam(teamA);
        FloorManager manager = new FloorManager(floor, player, bugemonService, itemService);
		// move to allowed room first
		manager.moveToRoom(5);
		// try to move to next room while previous is not completed (5 and 6 are adjacent)
		assertFalse(manager.moveToRoom(6));	

	}

	@Test 
	void doesntMoveToNextRoomIfPreviousNotAdjacent() {
		BugemonService bugemonService = new BugemonService(new BugemonSpeciesMockRepository());
		ItemService itemService = new ItemService(new ItemMockRepository(), new StartingInventoryMockRepository());

		Player player = new Player();
        Floor floor = new Floor(1, false);
		Bugemon a = makeBugemon();
		Team teamA = new Team(List.of(a));
		player.setTeam(teamA);
        FloorManager manager = new FloorManager(floor, player, bugemonService, itemService);
		manager.moveToRoom(5);
		manager.getRoom().setRoomCompleted(true);
		// rooms 5 and 7 aren't adjacent
		assertFalse(manager.moveToRoom(7));
	}
}

