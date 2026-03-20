package ulb.controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Vector;

import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.Stats;
import ulb.controller.action.Swap;
import ulb.controller.action.UseAbility;
import ulb.controller.action.UseItem;
import ulb.model.effect.Effect;
import ulb.model.item.Item;
import ulb.model.reward.Reward;
import ulb.model.sample.AbilitySample;
import ulb.model.sample.BugemonSample;
import ulb.model.sample.EffectSample;
import ulb.model.Player;
import ulb.model.ability.Ability;
import ulb.model.battle.Battle;
import ulb.model.battle.BattleState;
import ulb.model.team.Team;

public class BattleControllerTest {
	BattleController otherPlayerController;

	public void initiateOtherPlayerController(Battle battle){
		otherPlayerController = new BattleController(new Player(), battle, false);
	}

	private void otherPlayerChooseAction(Ability a){
		otherPlayerController.useAction(new UseAbility(a));
	}

	private int getGainedPoint(Stats previous, Stats actual){
		Stats difference = new Stats(actual);
		Stats opposite = new Stats(-previous.hp, -previous.attack, -previous.defense, -previous.initiative);
		difference.change(opposite);
		return difference.hp / 2 + difference.initiative / 2 + difference.attack + difference.defense;
	}

	@Test
	public void testBattleEndTeamKO(){
		Player player = new Player("TestPlayer");

		Bugemon a = BugemonSample.getA();
		Team teamA = new Team(List.of(a));
		Team teamB = new Team(List.of(BugemonSample.getB()));

		Battle battle = new Battle(teamA, teamB, player);
		BattleController controller = new BattleController(player, battle, true);
		this.initiateOtherPlayerController(battle);

		player.setTeam(teamA);

		controller.useAction(new UseAbility(AbilitySample.getI()));
		this.otherPlayerChooseAction(AbilitySample.getH());

		assertTrue(controller.isGameFinished());
	}

	@Test
	public void testCheckItemTrue() throws Exception {
		Player player = new Player("TestPlayer");

		Bugemon bugemon = BugemonSample.getA();
		bugemon.changeFightStats(new Stats(-1, 0, 0, 0));
		Team teamA = new Team(List.of(bugemon));
		Team teamB = new Team(List.of(BugemonSample.getB()));
		Battle battle = new Battle(teamA, teamB, player);

		BattleController battleController = new BattleController(player, battle, true);

		Effect effect = EffectSample.getHeal();
		Item item = new Item("potion", "Potion", "Restaure 10 pv.", "soin", effect, "potion.png");
		player.getInventory().addItem(item, 1);

		assertTrue(battleController.checkItem(item));
	}

	@Test
	public void testCheckItemFalse() throws Exception {
		Player player = new Player("TestPlayer");
		

		Bugemon bugemon = BugemonSample.getA();
		Team teamA = new Team(List.of(bugemon));
		Team teamB = new Team(List.of(BugemonSample.getB()));
		Battle battle = new Battle(teamA, teamB, player);
		BattleController battleController = new BattleController(player, battle, true);

		Effect effect = EffectSample.getHeal();
		Item item = new Item("potion", "Potion", "Restaure 10 pv.", "soin", effect, "potion.png");
		player.getInventory().addItem(item, 1);

		assertFalse(battleController.checkItem(item));
	}

	@Test
	public void testUsedItemRemovedFromInventory() throws Exception {
		Player player = new Player("TestPlayer");
		player.getInventory().getItems().clear();

		Team teamA = new Team(List.of(BugemonSample.getA()));
		Team teamB = new Team(List.of(BugemonSample.getB()));
		Battle battle = new Battle(teamA, teamB, player);

		BattleController battleController = new BattleController(player, battle, true);
		initiateOtherPlayerController(battle);
		
		Effect effect = EffectSample.getHeal();
		Item item = new Item("potion", "Potion", "Restaure 10 pv.", "soin", effect, "potion.png");
		player.getInventory().addItem(item, 3);
		
		assertTrue(player.getInventory().getItems().containsKey(item));
		assertEquals(3, (int) player.getInventory().getItems().get(item));

		battleController.useAction(new UseItem(item));
		this.otherPlayerChooseAction(AbilitySample.getH());

		assertTrue(player.getInventory().getItems().containsKey(item));
		assertEquals(2, (int) player.getInventory().getItems().get(item));

		battleController.useAction(new UseItem(item));
		this.otherPlayerChooseAction(AbilitySample.getH());

		assertEquals(1, (int) player.getInventory().getItems().get(item));

		battleController.useAction(new UseItem(item));
		this.otherPlayerChooseAction(AbilitySample.getH());

		assertFalse(player.getInventory().getItems().containsKey(item));
	}


	@Test
	public void testNoXpOnLoss() throws Exception {
		Player player = new Player("TestPlayer");

		Bugemon a = BugemonSample.getA();
		Team teamA = new Team(List.of(a));
		Team teamB = new Team(List.of(BugemonSample.getB()));
		Battle battle = new Battle(teamA, teamB, player);

		BattleController controller = new BattleController(player, battle, true);
		this.initiateOtherPlayerController(battle);

		player.setTeam(teamA);

		this.otherPlayerChooseAction(AbilitySample.getI());	
		controller.useAction(new UseAbility(AbilitySample.getH()));

		assertEquals(0, a.getXp());
		assertEquals(1, a.getLevel());
	}

	@Test
	public void testXpAfterWin() throws Exception {
		Player player = new Player("TestPlayer");

		Bugemon a = BugemonSample.getA();
		Team teamA = new Team(List.of(a));
		Team teamB = new Team(List.of(BugemonSample.getB()));
		player.setTeam(teamA);

		Battle battle = new Battle(teamA, teamB, player);
		BattleController controller = new BattleController(player, battle, true);
		this.initiateOtherPlayerController(battle);

		battle.setFloorNumber(5);

		this.otherPlayerChooseAction(AbilitySample.getH());
		controller.useAction(new UseAbility(AbilitySample.getI()));

		assertTrue(controller.isGameFinished());
		assertEquals(3, a.getLevel());
	}


	@Test
	public void testBossGivesMoreXp() throws Exception {
		Player player = new Player("TestPlayer");
		

		Bugemon a = BugemonSample.getA();
		Team teamA = new Team(List.of(a));
		Team teamB = new Team(List.of(BugemonSample.getB()));
		player.setTeam(teamA);

		Battle battle = new Battle(teamA, teamB, player);
		BattleController controller = new BattleController(player, battle, true);
		this.initiateOtherPlayerController(battle);

		battle.setFloorNumber(5);
		battle.enableBossBattle();

		this.otherPlayerChooseAction(AbilitySample.getH());
		controller.useAction(new UseAbility(AbilitySample.getI()));

		assertEquals(4, a.getLevel());
	}

	@Test
	public void testShareXPBetweenActivesBugemon(){
		Player player = new Player("TestPlayer");

		Bugemon a = BugemonSample.getA();
		Bugemon c = BugemonSample.getC();
		Team teamA = new Team(List.of(a, c));
		Team teamB = new Team(List.of(BugemonSample.getB()));

		Battle battle = new Battle(teamA, teamB, player);
		BattleController controller = new BattleController(player, battle, true);
		this.initiateOtherPlayerController(battle);

		player.setTeam(teamA);

		battle.setFloorNumber(4);

		controller.useAction(new Swap(c));
		this.otherPlayerChooseAction(AbilitySample.getH());

		controller.useAction(new UseAbility(AbilitySample.getI()));
		this.otherPlayerChooseAction(AbilitySample.getH());

		assertEquals(2, a.getLevel());
		assertEquals(2, c.getLevel());
		assertEquals(10, a.getXp());
		assertEquals(10, c.getXp());
	}

	@Test
	public void testDebuffsRemovedAfterBattle() throws Exception {
		Player player = new Player("TestPlayer");
		

		Bugemon a = BugemonSample.getA();
		Team teamA = new Team(List.of(a));
		Team teamB = new Team(List.of(BugemonSample.getB()));

		Battle battle = new Battle(teamA, teamB, player);
		BattleController controller = new BattleController(player, battle, true);
		this.initiateOtherPlayerController(battle);

		player.setTeam(teamA);

		a.changeFightStats(new Stats(1, -10, 0, 0));
		
		this.otherPlayerChooseAction(AbilitySample.getH());
		controller.useAction(new UseAbility(AbilitySample.getI()));

		assertTrue(a.getFightStats().getAttack() >= a.getBaseStats().getAttack());
	}

	@Test
	public void testHpRestoredOnLevelUp() throws Exception {
		Player player = new Player("TestPlayer");

		Bugemon a = BugemonSample.getA();
		Team teamA = new Team(List.of(a));
		Team teamB = new Team(List.of(BugemonSample.getB()));

		Battle battle = new Battle(teamA, teamB, player);
		BattleController controller = new BattleController(player, battle, true);
		this.initiateOtherPlayerController(battle);

		player.setTeam(teamA);

		a.changeFightStats(new Stats(-50, 0, 0, 0));
		battle.setFloorNumber(9);
		battle.enableBossBattle();

		this.otherPlayerChooseAction(AbilitySample.getH());
		controller.useAction(new UseAbility(AbilitySample.getI()));

		if (a.getLevel() > 1)
			assertEquals(a.getBaseStats().getHp(), a.getFightStats().getHp());
	}

	@Test
	public void testSwapBugemon(){
		Player player = new Player("TestPlayer");

		Bugemon a = BugemonSample.getA();
		Bugemon c = BugemonSample.getC();
		Team teamA = new Team(List.of(a, c));
		Team teamB = new Team(List.of(BugemonSample.getB()));

		Battle battle = new Battle(teamA, teamB, player);
		BattleController controller = new BattleController(player, battle, true);
		this.initiateOtherPlayerController(battle);

		player.setTeam(teamA);

		controller.useAction(new Swap(c));
		this.otherPlayerChooseAction(AbilitySample.getH());

		assertEquals(c.getId(), controller.getActiveBugemonSelf().getId());
	}

	@Test
	public void testSwapKOBugemon(){
		Player player = new Player("TestPlayer");

		Bugemon a = BugemonSample.getA();
		Bugemon c = BugemonSample.getC();
		c.changeFightStats(new Stats(-100, 0, 0, 0));

		Team teamA = new Team(List.of(a, c));
		Team teamB = new Team(List.of(BugemonSample.getB()));

		Battle battle = new Battle(teamA, teamB, player);
		BattleController controller = new BattleController(player, battle, true);
		this.initiateOtherPlayerController(battle);

		player.setTeam(teamA);

		controller.useAction(new Swap(c));
		this.otherPlayerChooseAction(AbilitySample.getH());

		assertEquals(a.getId(), controller.getActiveBugemonSelf().getId());
		assertEquals(controller.getState(), BattleState.INGAME);
	}

	@Test
	public void testApplyReward(){
		Player player = new Player("TestPlayer");

		Bugemon a = BugemonSample.getA();

		Team teamA = new Team(List.of(a));
		Team teamB = new Team(List.of(BugemonSample.getB()));

		Battle battle = new Battle(teamA, teamB, player);
		BattleController controller = new BattleController(player, battle, true);
		
		a.gainXp(50);
		Stats previousStats = new Stats(a.getBaseStats());
		Vector<Reward> rewards = controller.getRewards(a);
		assertTrue(controller.applyReward(a, rewards.get(0)));
		assertEquals(10, getGainedPoint(previousStats, a.getBaseStats()));
	}

}
