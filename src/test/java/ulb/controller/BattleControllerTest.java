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
import ulb.model.Effect;
import ulb.model.item.Item;
import ulb.model.sample.BugemonSample;
import ulb.model.sample.EffectSample;
import ulb.model.Player;
import ulb.model.battle.Battle;
import ulb.model.team.Team;

public class BattleControllerTest {

	// @Test
	// public void testCheckItemTrue() throws Exception {
	// 	Player player = new Player("TestPlayer");

	// 	Bugemon bugemon = BugemonSample.getA();
	// 	bugemon.changeFightStats(new Stats(-1, 0, 0, 0));
	// 	Team teamA = new Team(List.of(bugemon));
	// 	Team teamB = new Team(List.of(BugemonSample.getB()));
	// 	Battle battle = new Battle(teamA, teamB, player);

	// 	BattleController battleController = new BattleController(player, battle, true);

	// 	// Constructor<BattleSnapshot> constructor = BattleSnapshot.class.getDeclaredConstructor(Battle.class, boolean.class);
	// 	// constructor.setAccessible(true);
	// 	// BattleSnapshot snapshot = constructor.newInstance(battle, true);

	// 	// Field field = BattleController.class.getDeclaredField("battleSnapshot");
	// 	// field.setAccessible(true);
	// 	// field.set(battleController, snapshot);

	// 	Effect effect = EffectSample.getHeal();
	// 	Item item = new Item("potion", "Potion", "Restaure 10 pv.", "soin", effect, "potion.png");
	// 	player.getInventory().addItem(item, 1);

	// 	assertTrue(battleController.checkItem(item));
	// }

	// @Test
	// public void testCheckItemFalse() throws Exception {
	// 	Player player = new Player("TestPlayer");
		

	// 	Bugemon bugemon = BugemonSample.getA();
	// 	Team teamA = new Team(List.of(bugemon));
	// 	Team teamB = new Team(List.of(BugemonSample.getB()));
	// 	Battle battle = new Battle(teamA, teamB, player);
	// 	BattleController battleController = new BattleController(player, battle, true);

	// 	// Constructor<BattleSnapshot> constructor = BattleSnapshot.class.getDeclaredConstructor(Battle.class, boolean.class);
	// 	// constructor.setAccessible(true);
	// 	// BattleSnapshot snapshot = constructor.newInstance(battle, true);

	// 	// Field field = BattleController.class.getDeclaredField("battleSnapshot");
	// 	// field.setAccessible(true);
	// 	// field.set(battleController, snapshot);

	// 	Effect effect = EffectSample.getHeal();
	// 	Item item = new Item("potion", "Potion", "Restaure 10 pv.", "soin", effect, "potion.png");
	// 	player.getInventory().addItem(item, 1);

	// 	assertFalse(battleController.checkItem(item));
	// }

	// @Test
	// public void testUsedItemRemovedFromInventory() throws Exception {
	// 	Player player = new Player("TestPlayer");
	// 	player.getInventory().getItems().clear();

	// 	Team teamA = new Team(List.of(BugemonSample.getA()));
	// 	Team teamB = new Team(List.of(BugemonSample.getB()));
	// 	Battle battle = new Battle(teamA, teamB, player);

	// 	BattleController battleController = new BattleController(player, battle, true);

	// 	// Constructor<BattleSnapshot> constructor = BattleSnapshot.class.getDeclaredConstructor(Battle.class, boolean.class);
	// 	// constructor.setAccessible(true);
	// 	// BattleSnapshot snapshot = constructor.newInstance(battle, true);

	// 	// Field field = BattleController.class.getDeclaredField("battleSnapshot");
	// 	// field.setAccessible(true);
	// 	// field.set(battleController, snapshot);
		
	// 	Effect effect = EffectSample.getHeal();
	// 	Item item = new Item("potion", "Potion", "Restaure 10 pv.", "soin", effect, "potion.png");
	// 	player.getInventory().addItem(item, 3);
		
	// 	assertTrue(player.getInventory().getItems().containsKey(item));
	// 	assertEquals(3, (int) player.getInventory().getItems().get(item));

	// 	battleController.useItem(item);

	// 	assertTrue(player.getInventory().getItems().containsKey(item));
	// 	assertEquals(2, (int) player.getInventory().getItems().get(item));

	// 	battleController.useItem(item);

	// 	assertEquals(1, (int) player.getInventory().getItems().get(item));

	// 	battleController.useItem(item);

	// 	assertFalse(player.getInventory().getItems().containsKey(item));
	// }


	// @Test
	// public void testNoXpOnLoss() throws Exception {
	// 	Player player = new Player("TestPlayer");

	// 	Bugemon a = BugemonSample.getA();
	// 	Team teamA = new Team(List.of(a));
	// 	Team teamB = new Team(List.of(BugemonSample.getB()));
	// 	Battle battle = new Battle(teamA, teamB, player);

	// 	BattleController controller = new BattleController(player, battle, true);

	// 	// Constructor<BattleSnapshot> constructor = BattleSnapshot.class.getDeclaredConstructor(Battle.class,
	// 	// 		boolean.class);
	// 	// constructor.setAccessible(true);
	// 	// Field field = BattleController.class.getDeclaredField("battleSnapshot");
	// 	// field.setAccessible(true);
	// 	// field.set(controller, constructor.newInstance(new Battle(teamA, teamB), true));
	// 	// player.setTeam(teamA);

	// 	controller.handleBattleEnd(false);
	// 	assertEquals(0, a.getXp());
	// }

	// @Test
	// public void testXpAfterWin() throws Exception {
	// 	Player player = new Player("TestPlayer");
	// 	BattleController controller = new BattleController(player);

	// 	Bugemon a = BugemonSample.getA();
	// 	Team teamA = new Team(List.of(a));
	// 	Team teamB = new Team(List.of(BugemonSample.getB()));
	// 	player.setTeam(teamA);

	// 	Constructor<BattleSnapshot> constructor = BattleSnapshot.class.getDeclaredConstructor(Battle.class,
	// 			boolean.class);
	// 	constructor.setAccessible(true);
	// 	Field field = BattleController.class.getDeclaredField("battleSnapshot");
	// 	field.setAccessible(true);
	// 	field.set(controller, constructor.newInstance(new Battle(teamA, teamB), true));

	// 	controller.setFloorNumber(5);
	// 	controller.handleBattleEnd(true);
	// 	assertEquals(3, a.getLevel());
	// }


	// @Test
	// public void testBossGivesMoreXp() throws Exception {
	// 	Player player = new Player("TestPlayer");
	// 	BattleController controller = new BattleController(player);

	// 	Bugemon a = BugemonSample.getA();
	// 	Team teamA = new Team(List.of(a));
	// 	Team teamB = new Team(List.of(BugemonSample.getB()));
	// 	player.setTeam(teamA);

	// 	Constructor<BattleSnapshot> constructor = BattleSnapshot.class.getDeclaredConstructor(Battle.class,
	// 			boolean.class);
	// 	constructor.setAccessible(true);
	// 	Field field = BattleController.class.getDeclaredField("battleSnapshot");
	// 	field.setAccessible(true);
	// 	field.set(controller, constructor.newInstance(new Battle(teamA, teamB), true));

	// 	controller.setFloorNumber(5);
	// 	controller.setIsBossFight(true);
	// 	controller.handleBattleEnd(true); 
	// 	assertEquals(4, a.getLevel());
	// }

	// @Test
	// public void testDebuffsRemovedAfterBattle() throws Exception {
	// 	Player player = new Player("TestPlayer");
	// 	BattleController controller = new BattleController(player);

	// 	Bugemon a = BugemonSample.getA();
	// 	Team teamA = new Team(List.of(a));
	// 	Team teamB = new Team(List.of(BugemonSample.getB()));

	// 	Constructor<BattleSnapshot> constructor = BattleSnapshot.class.getDeclaredConstructor(Battle.class,
	// 			boolean.class);
	// 	constructor.setAccessible(true);
	// 	Field field = BattleController.class.getDeclaredField("battleSnapshot");
	// 	field.setAccessible(true);
	// 	field.set(controller, constructor.newInstance(new Battle(teamA, teamB), true));
	// 	player.setTeam(teamA);

	// 	a.changeFightStats(new Stats(0, -10, 0, 0));
	// 	controller.handleBattleEnd(false);
	// 	assertTrue(a.getFightStats().getAttack() >= a.getBaseStats().getAttack());
	// }

	// @Test
	// public void testHpRestoredOnLevelUp() throws Exception {
	// 	Player player = new Player("TestPlayer");
	// 	BattleController controller = new BattleController(player);

	// 	Bugemon a = BugemonSample.getA();
	// 	Team teamA = new Team(List.of(a));
	// 	Team teamB = new Team(List.of(BugemonSample.getB()));

	// 	Constructor<BattleSnapshot> constructor = BattleSnapshot.class.getDeclaredConstructor(Battle.class,
	// 			boolean.class);
	// 	constructor.setAccessible(true);
	// 	Field field = BattleController.class.getDeclaredField("battleSnapshot");
	// 	field.setAccessible(true);
	// 	field.set(controller, constructor.newInstance(new Battle(teamA, teamB), true));
	// 	player.setTeam(teamA);

	// 	a.changeFightStats(new Stats(-50, 0, 0, 0));
	// 	controller.setFloorNumber(9);
	// 	controller.setIsBossFight(true);
	// 	controller.handleBattleEnd(true);

	// 	if (a.getLevel() > 1)
	// 		assertEquals(a.getBaseStats().getHp(), a.getFightStats().getHp());
	// }

}
