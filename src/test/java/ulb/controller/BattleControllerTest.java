package ulb.controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.Stats;
import ulb.controller.action.UseAbility;
import ulb.controller.action.UseItem;
import ulb.model.Effect;
import ulb.model.item.Item;
import ulb.model.sample.AbilitySample;
import ulb.model.sample.BugemonSample;
import ulb.model.sample.EffectSample;
import ulb.model.Player;
import ulb.model.ability.Ability;
import ulb.model.battle.Battle;
import ulb.model.team.Team;

public class BattleControllerTest {
	BattleController otherPlayerController;

	public void initiateOtherPlayerController(Battle battle){
		otherPlayerController = new BattleController(new Player(), battle, false);
	}

	private void otherPlayerChooseAction(Ability a){
		otherPlayerController.useAction(new UseAbility(a));
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

		// Constructor<BattleSnapshot> constructor = BattleSnapshot.class.getDeclaredConstructor(Battle.class, boolean.class);
		// constructor.setAccessible(true);
		// BattleSnapshot snapshot = constructor.newInstance(battle, true);

		// Field field = BattleController.class.getDeclaredField("battleSnapshot");
		// field.setAccessible(true);
		// field.set(battleController, snapshot);

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

		// Constructor<BattleSnapshot> constructor = BattleSnapshot.class.getDeclaredConstructor(Battle.class, boolean.class);
		// constructor.setAccessible(true);
		// BattleSnapshot snapshot = constructor.newInstance(battle, true);

		// Field field = BattleController.class.getDeclaredField("battleSnapshot");
		// field.setAccessible(true);
		// field.set(battleController, snapshot);

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

		// Constructor<BattleSnapshot> constructor = BattleSnapshot.class.getDeclaredConstructor(Battle.class, boolean.class);
		// constructor.setAccessible(true);
		// BattleSnapshot snapshot = constructor.newInstance(battle, true);

		// Field field = BattleController.class.getDeclaredField("battleSnapshot");
		// field.setAccessible(true);
		// field.set(battleController, snapshot);
		
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

		// Constructor<BattleSnapshot> constructor = BattleSnapshot.class.getDeclaredConstructor(Battle.class,
		// 		boolean.class);
		// constructor.setAccessible(true);
		// Field field = BattleController.class.getDeclaredField("battleSnapshot");
		// field.setAccessible(true);
		// field.set(controller, constructor.newInstance(new Battle(teamA, teamB), true));
		player.setTeam(teamA);

		// controller.handleBattleEnd(false);

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

		// Constructor<BattleSnapshot> constructor = BattleSnapshot.class.getDeclaredConstructor(Battle.class,
		// 		boolean.class);
		// constructor.setAccessible(true);
		// Field field = BattleController.class.getDeclaredField("battleSnapshot");
		// field.setAccessible(true);
		// field.set(controller, constructor.newInstance(new Battle(teamA, teamB), true));

		battle.setFloorNumber(5);

		this.otherPlayerChooseAction(AbilitySample.getH());
		System.err.println("STATE PLAYER B = " + battle.getState(false));
		controller.useAction(new UseAbility(AbilitySample.getI()));
		System.err.println("STATE PLAYER A = " + battle.getState(true));

		// controller.handleBattleEnd(true);
		System.out.println("GAME FINISHED = " + controller.isGameFinished());
		System.err.println(teamA.getBugemon(0).getFightStats().hp);
		System.out.println(teamB.getBugemon(0).getFightStats().hp);
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

		// Constructor<BattleSnapshot> constructor = BattleSnapshot.class.getDeclaredConstructor(Battle.class,
		// 		boolean.class);
		// constructor.setAccessible(true);
		// Field field = BattleController.class.getDeclaredField("battleSnapshot");
		// field.setAccessible(true);
		// field.set(controller, constructor.newInstance(new Battle(teamA, teamB), true));

		battle.setFloorNumber(5);
		battle.enableBossBattle();

		this.otherPlayerChooseAction(AbilitySample.getH());
		controller.useAction(new UseAbility(AbilitySample.getI()));

		// controller.handleBattleEnd(true); 
		assertEquals(4, a.getLevel());
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

		// Constructor<BattleSnapshot> constructor = BattleSnapshot.class.getDeclaredConstructor(Battle.class,
		// 		boolean.class);
		// constructor.setAccessible(true);
		// Field field = BattleController.class.getDeclaredField("battleSnapshot");
		// field.setAccessible(true);
		// field.set(controller, constructor.newInstance(new Battle(teamA, teamB), true));
		player.setTeam(teamA);

		a.changeFightStats(new Stats(1, -10, 0, 0));
		
		this.otherPlayerChooseAction(AbilitySample.getH());
		controller.useAction(new UseAbility(AbilitySample.getI()));

		// controller.handleBattleEnd(false);
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

		// Constructor<BattleSnapshot> constructor = BattleSnapshot.class.getDeclaredConstructor(Battle.class,
		// 		boolean.class);
		// constructor.setAccessible(true);
		// Field field = BattleController.class.getDeclaredField("battleSnapshot");
		// field.setAccessible(true);
		// field.set(controller, constructor.newInstance(new Battle(teamA, teamB), true));
		player.setTeam(teamA);

		a.changeFightStats(new Stats(-50, 0, 0, 0));
		battle.setFloorNumber(9);
		battle.enableBossBattle();

		this.otherPlayerChooseAction(AbilitySample.getH());
		controller.useAction(new UseAbility(AbilitySample.getI()));

		// controller.handleBattleEnd(true);

		if (a.getLevel() > 1)
			assertEquals(a.getBaseStats().getHp(), a.getFightStats().getHp());
	}

}
