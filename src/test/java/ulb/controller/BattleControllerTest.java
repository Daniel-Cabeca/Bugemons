package ulb.controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Vector;

import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.Stats;
import ulb.controller.action.Action;
import ulb.controller.action.Swap;
import ulb.controller.action.UseAbility;
import ulb.controller.action.UseItem;
import ulb.model.item.Item;
import ulb.model.reward.Reward;
import ulb.model.Player;
import ulb.model.ability.Ability;
import ulb.model.battle.Battle;
import ulb.model.battle.Battle.ParticipantLabel;
import ulb.model.battle.BattleState;
import ulb.model.team.Team;
import ulb.repository.BugemonSpeciesRepository;
import ulb.repository.ItemRepository;
import ulb.repository.mock.BugemonSpeciesMockRepository;
import ulb.repository.mock.InventoryMockRepository;
import ulb.repository.mock.ItemMockRepository;
import ulb.service.BugemonService;
import ulb.service.ItemService;

public class BattleControllerTest {
	private static Item getItem(String id) {
		ItemRepository repository = new ItemMockRepository();
		return repository.findById(id);
	}

	private static int getGainedPoint(Stats previous, Stats actual){
		Stats difference = new Stats(actual);
		Stats opposite = new Stats(-previous.getHp(), -previous.getAttack(), -previous.getDefense(), -previous.getInitiative());
		difference.change(opposite);
		return difference.getHp() / 2 + difference.getInitiative() / 2 + difference.getAttack() + difference.getDefense();
	}

	private static BattleController makeBattleController1v1(String bugemonIdA, String bugemonIdB) {
		BugemonSpeciesRepository bugemonRepository = new BugemonSpeciesMockRepository();
		BugemonService bugemonService = new BugemonService(bugemonRepository);
		ItemService itemService = new ItemService(new ItemMockRepository(), new InventoryMockRepository());

		Bugemon bugemonA = bugemonService.spawnBugemon(bugemonIdA);
		Bugemon bugemonB = bugemonService.spawnBugemon(bugemonIdB);

		Team teamA = new Team(List.of(bugemonA));
		Team teamB = new Team(List.of(bugemonB));

		Player player = new Player("TestPlayer", itemService);
		player.setTeam(teamA);

		Player otherPlayer = new Player("OtherPlayer", itemService);
		otherPlayer.setTeam(teamB);

		Battle battle = new Battle(teamA, teamB, player, otherPlayer);
		return new BattleController(player, battle, ParticipantLabel.TEAM_A, new ItemService(new ItemMockRepository(), new InventoryMockRepository()));
	}

	private static BattleController makeBattleController2v1(String bugemonIdA1, String bugemonIdA2, String bugemonIdB) {
		BugemonSpeciesRepository bugemonRepository = new BugemonSpeciesMockRepository();
		BugemonService bugemonService = new BugemonService(bugemonRepository);
		ItemService itemService = new ItemService(new ItemMockRepository(), new InventoryMockRepository());

		Bugemon bugemonA1 = bugemonService.spawnBugemon(bugemonIdA1);
		Bugemon bugemonA2 = bugemonService.spawnBugemon(bugemonIdA2);
		Bugemon bugemonB = bugemonService.spawnBugemon(bugemonIdB);

		Team teamA = new Team(List.of(bugemonA1, bugemonA2));
		Team teamB = new Team(List.of(bugemonB));

		Player player = new Player("TestPlayer", itemService);
		player.setTeam(teamA);

		Player otherPlayer = new Player("OtherPlayer", itemService);
		otherPlayer.setTeam(teamB);

		Battle battle = new Battle(teamA, teamB, player, otherPlayer);
		return new BattleController(player, battle, ParticipantLabel.TEAM_A, new ItemService(new ItemMockRepository(), new InventoryMockRepository()));
	}

	private BattleController makeOtherPlayerBattleController(BattleController selfController) {
		Battle battle = selfController.getBattle();
		Player otherPlayer = battle.getPlayer(ParticipantLabel.TEAM_B);

		return new BattleController(otherPlayer, battle, ParticipantLabel.TEAM_B, new ItemService(new ItemMockRepository(), new InventoryMockRepository()));
	}

	private static void playTurn(BattleController controllerA, BattleController controllerB, Action actionA, Action actionB) {
		controllerA.useAction(actionA);
		controllerB.useAction(actionB);
	}

	private static void playTurn(BattleController controllerA, BattleController controllerB, Action actionA) {
		Ability abilityB = controllerA.getBattle().getActiveBugemon(ParticipantLabel.TEAM_B).getAbilities().getAbility(0);
		playTurn(controllerA, controllerB, actionA, new UseAbility(abilityB));
	}

	private static void playTurn(BattleController controllerA, BattleController controllerB, Item item) {
		playTurn(controllerA, controllerB, new UseItem(item));
	}

	private static void playTurn(BattleController controllerA, BattleController controllerB) {
		Ability abilityA = controllerA.getBattle().getActiveBugemon(ParticipantLabel.TEAM_A).getAbilities().getAbility(0);
		playTurn(controllerA, controllerB, new UseAbility(abilityA));
	}

	@Test
	public void battleEndsWhenTeamKO(){
		BattleController controllerA = makeBattleController1v1("insta_kill", "pass_turn");
		BattleController controllerB = makeOtherPlayerBattleController(controllerA);

		playTurn(controllerA, controllerB);
		assertTrue(controllerA.isGameFinished());
	}

	@Test
	public void allowUseOfHealItem() throws Exception {
		BattleController controllerA = makeBattleController1v1("florachu", "pyricore");

		Item item = getItem("heal_10");
		Player player = controllerA.getPlayer();
		player.getInventory().addItem(item, 1);

		controllerA.getBattle().getTeam(ParticipantLabel.TEAM_A).getBugemon(0).changeFightStats(new Stats(-1, 0, 0, 0));
		assertTrue(controllerA.checkItem(item));
	}

	@Test
	public void doesNotAllowUseOfHealItem() throws Exception {
		BattleController controllerA = makeBattleController1v1("florachu", "pyricore");

		Item item = getItem("heal_10");
		Player player = controllerA.getPlayer();
		player.getInventory().addItem(item, 1);

		assertFalse(controllerA.checkItem(item));
	}

	@Test
	public void itemRemovedFromInventoryWhenUsed() throws Exception {
		BattleController controllerA = makeBattleController1v1("insta_kill", "pass_turn");
		BattleController controllerB = makeOtherPlayerBattleController(controllerA);

		Item item = getItem("heal_10");
		Player player = controllerA.getPlayer();
		player.getInventory().addItem(item, 3);

		assertEquals(3, (int) player.getInventory().getItems().get(item));

		playTurn(controllerA, controllerB, item);
		assertEquals(2, (int) player.getInventory().getItems().get(item));

		playTurn(controllerA, controllerB, item);
		assertEquals(1, (int) player.getInventory().getItems().get(item));

		playTurn(controllerA, controllerB, item);
		assertFalse(player.getInventory().getItems().containsKey(item));
	}


	@Test
	public void noXpGainedOnLostBattle() throws Exception {
		BattleController controllerA = makeBattleController1v1("pass_turn", "insta_kill");
		BattleController controllerB = makeOtherPlayerBattleController(controllerA);

		Battle battle = controllerA.getBattle();
		Bugemon a = battle.getTeam(ParticipantLabel.TEAM_A).getBugemon(0);

		playTurn(controllerA, controllerB);

		assertEquals(0, a.getXp());
		assertEquals(1, a.getLevel());
	}

	@Test
	public void xpGainedOnWonBattle() throws Exception {
		BattleController controllerA = makeBattleController1v1("insta_kill", "pass_turn");
		BattleController controllerB = makeOtherPlayerBattleController(controllerA);

		Battle battle = controllerA.getBattle();
		Bugemon a = battle.getTeam(ParticipantLabel.TEAM_A).getBugemon(0);

		battle.setFloorNumber(5);

		playTurn(controllerA, controllerB);

		assertTrue(controllerA.isGameFinished());
		assertEquals(3, a.getLevel());
	}

	@Test
	public void winAgainstBossGivesMoreXp() throws Exception {
		BattleController controllerA = makeBattleController1v1("insta_kill", "pass_turn");
		BattleController controllerB = makeOtherPlayerBattleController(controllerA);

		Battle battle = controllerA.getBattle();
		Bugemon a = battle.getTeam(ParticipantLabel.TEAM_A).getBugemon(0);

		battle.setFloorNumber(5);
		battle.enableBossBattle();

		playTurn(controllerA, controllerB);

		assertEquals(4, a.getLevel());
	}

	@Test
	public void shareXpWonBetweenActiveBugemons(){
		BattleController controllerA = makeBattleController2v1("florachu", "insta_kill", "pass_turn");
		BattleController controllerB = makeOtherPlayerBattleController(controllerA);

		Battle battle = controllerA.getBattle();
		Bugemon a1 = battle.getTeam(ParticipantLabel.TEAM_A).getBugemon(0);
		Bugemon a2 = battle.getTeam(ParticipantLabel.TEAM_A).getBugemon(1);

		battle.setFloorNumber(4);

		playTurn(controllerA, controllerB, new Swap(a2));
		playTurn(controllerA, controllerB);

		assertEquals(2, a1.getLevel());
		assertEquals(2, a2.getLevel());
		assertEquals(10, a1.getXp());
		assertEquals(10, a2.getXp());
	}

	@Test
	public void debuffsRemovedWhenBattleIsFinished() throws Exception {
		BattleController controllerA = makeBattleController1v1("insta_kill", "pass_turn");
		BattleController controllerB = makeOtherPlayerBattleController(controllerA);

		Battle battle = controllerA.getBattle();
		Bugemon a = battle.getTeam(ParticipantLabel.TEAM_A).getBugemon(0);

		a.changeFightStats(new Stats(1, -10, 0, 0));

		playTurn(controllerA, controllerB);
		BattleState state = battle.getState(ParticipantLabel.TEAM_A);
		if (state == BattleState.WON || state == BattleState.LOST){
			battle.resetFightStats();
		}

		assertTrue(a.getFightStats().getAttack() >= a.getBaseStats().getAttack());
	}

	@Test
	public void hpRestoredWhenLevelUp() throws Exception {
		BattleController controllerA = makeBattleController1v1("insta_kill", "pass_turn");
		BattleController controllerB = makeOtherPlayerBattleController(controllerA);

		Battle battle = controllerA.getBattle();
		Bugemon a = battle.getTeam(ParticipantLabel.TEAM_A).getBugemon(0);

		a.changeFightStats(new Stats(-50, 0, 0, 0));

		battle.setFloorNumber(9);
		battle.enableBossBattle();

		playTurn(controllerA, controllerB);
		BattleState state = battle.getState(ParticipantLabel.TEAM_A);
		if (state == BattleState.WON || state == BattleState.LOST){
			battle.resetFightStats();
		}
		
		if (a.getLevel() > 1)
			assertEquals(a.getBaseStats().getHp(), a.getFightStats().getHp());
	}

	@Test
	public void swapBugemon(){
		BattleController controllerA = makeBattleController2v1("pass_turn", "florachu", "pass_turn");
		BattleController controllerB = makeOtherPlayerBattleController(controllerA);

		Battle battle = controllerA.getBattle();
		Bugemon a1 = battle.getTeam(ParticipantLabel.TEAM_A).getBugemon(0);
		Bugemon a2 = battle.getTeam(ParticipantLabel.TEAM_A).getBugemon(1);

		playTurn(controllerA, controllerB, new Swap(a2));

		assertEquals(a2.getId(), controllerA.getActiveBugemonSelf().getId());
	}

	@Test
	public void swapKOBugemon(){
		BattleController controllerA = makeBattleController2v1("pass_turn", "florachu", "pass_turn");
		BattleController controllerB = makeOtherPlayerBattleController(controllerA);

		Battle battle = controllerA.getBattle();
		Bugemon a1 = battle.getTeam(ParticipantLabel.TEAM_A).getBugemon(0);
		Bugemon a2 = battle.getTeam(ParticipantLabel.TEAM_A).getBugemon(1);

		a2.changeFightStats(new Stats(-100000, 0, 0, 0));

		playTurn(controllerA, controllerB, new Swap(a2));

		assertEquals(a1.getId(), controllerA.getActiveBugemonSelf().getId());
		assertEquals(controllerA.getState(), BattleState.INGAME);
	}

	@Test
	public void applyReward(){
		BattleController controllerA = makeBattleController1v1("insta_kill", "pass_turn");
		BattleController controllerB = makeOtherPlayerBattleController(controllerA);

		Battle battle = controllerA.getBattle();
		Bugemon a = battle.getTeam(ParticipantLabel.TEAM_A).getBugemon(0);

		a.gainXp(50);

		Stats previousStats = new Stats(a.getBaseStats());
		Vector<Reward> rewards = controllerA.getRewards(a);

		assertTrue(controllerA.applyReward(a, rewards.get(0)));
		assertEquals(10, getGainedPoint(previousStats, a.getBaseStats()));
	}

}
