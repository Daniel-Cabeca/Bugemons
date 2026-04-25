package ulb.model.item;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import ulb.model.ability.Ability;
import ulb.model.action.UseAbility;
import ulb.model.action.UseItem;
import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.Stats;

import ulb.model.Player;
import ulb.model.team.Team;
import ulb.model.battle.Battle;
import ulb.model.battle.Battle.ParticipantLabel;
import ulb.repository.mock.InventoryMockRepository;import ulb.service.BugemonService;
import ulb.repository.mock.BugemonSpeciesMockRepository;
import ulb.repository.ItemRepository;
import ulb.repository.mock.ItemMockRepository;import ulb.service.ItemService;

public class ItemTest {
	private static Item getItem(String id) {
		ItemRepository repository = new ItemMockRepository();
		return repository.findById(id);
	}

	private static Bugemon spawnBugemon(String id) {
		BugemonService service = new BugemonService(new BugemonSpeciesMockRepository());
		return service.spawnBugemon(id);
	}

	private Battle makeBattleController(Bugemon... bugemons) {
		ItemService itemService = new ItemService(new ItemMockRepository(), new InventoryMockRepository());

		List<Bugemon> bugemonList = bugemons.length > 0 ? List.of(bugemons) : List.of(spawnBugemon("florachu"));
		Team teamA = new Team(bugemonList);
		Team teamB = new Team(List.of(spawnBugemon("pass_turn")));

		Player player = new Player("TestPlayer", itemService);
		player.setTeam(teamA);

		Player otherPlayer = new Player("OtherPlayer", itemService);
		otherPlayer.setTeam(teamB);

		return new Battle(teamA, teamB, player, otherPlayer);
	}

	private void playTurnWithItem(Battle battle, Item item) {
		Ability otherAbility = battle.getActiveBugemon(ParticipantLabel.TEAM_B).getAbilities().getAbility(0);

		battle.chooseAction(new UseItem(item), Battle.ParticipantLabel.TEAM_A);
		battle.chooseAction(new UseAbility(otherAbility), Battle.ParticipantLabel.TEAM_B);
	}

    @Test
	public void healItemAppliesFullEffect() {
		Bugemon bugemon = spawnBugemon("100hp");
		bugemon.changeFightStats(new Stats(-20, 0, 0, 0));

		Battle battle = makeBattleController(bugemon);
		Player player = battle.getPlayer(Battle.ParticipantLabel.TEAM_A);

		Item item = getItem("heal_10");
		player.getInventory().addItem(item, 1);

		playTurnWithItem(battle, item);
		assertEquals(90, bugemon.getFightStats().getHp());
	}

	@Test
	public void healItemAppliesPartialEffect() {
		Bugemon bugemon = spawnBugemon("100hp");
		bugemon.changeFightStats(new Stats(-5, 0, 0, 0));

		Battle battle = makeBattleController(bugemon);
		Player player = battle.getPlayer(ParticipantLabel.TEAM_A);

		Item item = getItem("heal_10");
		player.getInventory().addItem(item, 1);

		playTurnWithItem(battle, item);

		assertEquals(100, bugemon.getFightStats().getHp());
	}

	@Test
	public void statModifierAppliesEffect() {
		Bugemon bugemon = spawnBugemon("50atk");

		Battle battle = makeBattleController(bugemon);
		Player player = battle.getPlayer(ParticipantLabel.TEAM_A);

		Item item = getItem("+10atk");
		player.getInventory().addItem(item, 1);

		playTurnWithItem(battle, item);

		assertEquals(60, bugemon.getFightStats().getAttack());
	}

	@Test
	public void multipleStatModifierAppliesEffect() {
		Bugemon bugemon = spawnBugemon("100all");

		Battle battle = makeBattleController(bugemon);
		Player player = battle.getPlayer(ParticipantLabel.TEAM_A);

		Item item = getItem("+10atk_+10def");
		player.getInventory().addItem(item, 1);

		playTurnWithItem(battle, item);

		assertEquals(110, bugemon.getFightStats().getAttack());
		assertEquals(110, bugemon.getFightStats().getDefense());
	}

	@Test
	public void switchItemAppliesEffect() {
		Battle battle = makeBattleController(spawnBugemon("florachu"), spawnBugemon("pyricore"));
		Player player = battle.getPlayer(ParticipantLabel.TEAM_A);

		Item item = getItem("switch");
		player.getInventory().addItem(item, 1);

		Bugemon activeBefore = battle.getActiveBugemon(ParticipantLabel.TEAM_A);
		playTurnWithItem(battle, item);

		assertNotEquals("florachu", battle.getActiveBugemon(ParticipantLabel.TEAM_A).getSpecies().getId());
		assertEquals("pyricore", battle.getActiveBugemon(ParticipantLabel.TEAM_A).getSpecies().getId());
	}

	@Test
	public void resetMalusItemAppliesEffect() {
		Bugemon bugemon = spawnBugemon("100all");
		bugemon.changeFightStats(new Stats(-5, -5, -5, -5)); 

		Battle battle = makeBattleController(bugemon);
		Player player = battle.getPlayer(ParticipantLabel.TEAM_A);

		Item item = getItem("reset_malus");
		player.getInventory().addItem(item, 1);

		playTurnWithItem(battle, item);

		assertEquals(100, bugemon.getFightStats().getHp());
		assertEquals(100, bugemon.getFightStats().getAttack());
		assertEquals(100, bugemon.getFightStats().getDefense());
		assertEquals(100, bugemon.getFightStats().getInitiative());
	}
    
}
