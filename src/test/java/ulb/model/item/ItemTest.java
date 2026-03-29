package ulb.model.item;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import ulb.model.ability.Ability;
import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.Stats;

import ulb.model.Player;
import ulb.model.team.Team;
import ulb.model.battle.Battle;
import ulb.model.battle.Battle.ParticipantLabel;
import ulb.controller.BattleController;
import ulb.controller.action.UseAbility;
import ulb.controller.action.UseItem;

import ulb.service.BugemonService;
import ulb.repository.mock.BugemonSpeciesMockRepository;
import ulb.repository.ItemRepository;
import ulb.repository.mock.ItemMockRepository;

public class ItemTest {
	private static Item getItem(String id) {
		ItemRepository repository = new ItemMockRepository();
		return repository.findById(id);
	}

	private static Bugemon spawnBugemon(String id) {
		BugemonService service = new BugemonService(new BugemonSpeciesMockRepository());
		return service.spawnBugemon(id);
	}

	private BattleController makeBattleController(Bugemon... bugemons) {
		List<Bugemon> bugemonList = bugemons.length > 0 ? List.of(bugemons) : List.of(spawnBugemon("florachu"));
		Team teamA = new Team(bugemonList);
		Team teamB = new Team(List.of(spawnBugemon("pass_turn")));

		Player player = new Player("TestPlayer");
		player.setTeam(teamA);

		Player otherPlayer = new Player("OtherPlayer");
		otherPlayer.setTeam(teamB);

		Battle battle = new Battle(teamA, teamB, player, otherPlayer);
		return new BattleController(player, battle, ParticipantLabel.TEAM_A);
	}

	private BattleController makeOtherPlayerBattleController(BattleController selfController) {
		Battle battle = selfController.getBattle();
		Player otherPlayer = battle.getPlayer(ParticipantLabel.TEAM_B);

		return new BattleController(otherPlayer, battle, ParticipantLabel.TEAM_B);
	}

	private void playTurnWithItem(BattleController controller, BattleController otherController, Item item) {
		Ability otherAbility = otherController.getBattle().getActiveBugemon(ParticipantLabel.TEAM_B).getAbilities().getAbility(0);

		controller.useAction(new UseItem(item));
		otherController.useAction(new UseAbility(otherAbility));
	}

    @Test
	public void healItemAppliesFullEffect() {
		Bugemon bugemon = spawnBugemon("100hp");
		bugemon.changeFightStats(new Stats(-20, 0, 0, 0));

		BattleController controller = makeBattleController(bugemon);
		Player player = controller.getPlayer();
		BattleController otherController = makeOtherPlayerBattleController(controller);

		Item item = getItem("heal_10");
		player.getInventory().addItem(item, 1);

		playTurnWithItem(controller, otherController, item);
		assertEquals(90, bugemon.getFightStats().getHp());
	}

	@Test
	public void healItemAppliesPartialEffect() {
		Bugemon bugemon = spawnBugemon("100hp");
		bugemon.changeFightStats(new Stats(-5, 0, 0, 0));

		BattleController controller = makeBattleController(bugemon);
		Player player = controller.getPlayer();
		BattleController otherController = makeOtherPlayerBattleController(controller);

		Item item = getItem("heal_10");
		player.getInventory().addItem(item, 1);

		playTurnWithItem(controller, otherController, item);

		assertEquals(100, bugemon.getFightStats().getHp());
	}

	@Test
	public void statModifierAppliesEffect() {
		Bugemon bugemon = spawnBugemon("50atk");

		BattleController controller = makeBattleController(bugemon);
		Player player = controller.getPlayer();
		BattleController otherController = makeOtherPlayerBattleController(controller);

		Item item = getItem("+10atk");
		player.getInventory().addItem(item, 1);

		playTurnWithItem(controller, otherController, item);

		assertEquals(60, bugemon.getFightStats().getAttack());
	}

	@Test
	public void multipleStatModifierAppliesEffect() {
		Bugemon bugemon = spawnBugemon("100all");

		BattleController controller = makeBattleController(bugemon);
		Player player = controller.getPlayer();
		BattleController otherController = makeOtherPlayerBattleController(controller);

		Item item = getItem("+10atk_+10def");
		player.getInventory().addItem(item, 1);

		playTurnWithItem(controller, otherController, item);

		assertEquals(110, bugemon.getFightStats().getAttack());
		assertEquals(110, bugemon.getFightStats().getDefense());
	}

	@Test
	public void switchItemAppliesEffect() {
		BattleController controller = makeBattleController(spawnBugemon("florachu"), spawnBugemon("pyricore"));
		BattleController otherController = makeOtherPlayerBattleController(controller);
		Player player = controller.getPlayer();
		Battle battle = controller.getBattle();

		Item item = getItem("switch");
		player.getInventory().addItem(item, 1);

		Bugemon activeBefore = battle.getActiveBugemon(ParticipantLabel.TEAM_A);
		playTurnWithItem(controller, otherController, item);

		assertNotEquals("florachu", battle.getActiveBugemon(ParticipantLabel.TEAM_A).getSpecies().getId());
		assertEquals("pyricore", battle.getActiveBugemon(ParticipantLabel.TEAM_A).getSpecies().getId());
	}

	@Test
	public void resetMalusItemAppliesEffect() {
		Bugemon bugemon = spawnBugemon("100all");
		bugemon.changeFightStats(new Stats(-5, -5, -5, -5)); 

		BattleController controller = makeBattleController(bugemon);
		Player player = controller.getPlayer();
		BattleController otherController = makeOtherPlayerBattleController(controller);

		Item item = getItem("reset_malus");
		player.getInventory().addItem(item, 1);

		playTurnWithItem(controller, otherController, item);

		assertEquals(100, bugemon.getFightStats().getHp());
		assertEquals(100, bugemon.getFightStats().getAttack());
		assertEquals(100, bugemon.getFightStats().getDefense());
		assertEquals(100, bugemon.getFightStats().getInitiative());
	}
    
}
