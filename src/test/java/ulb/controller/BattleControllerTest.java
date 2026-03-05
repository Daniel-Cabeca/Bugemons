package ulb.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.Stats;
import ulb.model.Effect;
import ulb.model.item.Item;
import ulb.model.Player;
import ulb.model.ability.Ability;
import ulb.model.battle.Battle;
import ulb.model.battle.BattleSnapshot;
import ulb.model.team.Team;
import ulb.model.type.Type;

public class BattleControllerTest {

	@Test
	public void testUsedItemRemovedFromInventory() throws Exception {
		Player player = new Player("TestPlayer");
		player.getInventory().getItems().clear();
		BattleController battleController = new BattleController(player);

		Team teamA = new Team(List.of(new Bugemon(Type.PYRO, 100, 10, 10, 10)));
		Team teamB = new Team(List.of(new Bugemon(Type.FLORA, 100, 10, 10, 10)));
		Battle battle = new Battle(teamA, teamB);

		Constructor<BattleSnapshot> constructor = BattleSnapshot.class.getDeclaredConstructor(Battle.class, boolean.class);
		constructor.setAccessible(true);
		BattleSnapshot snapshot = constructor.newInstance(battle, true);

		Field field = BattleController.class.getDeclaredField("battleSnapshot");
		field.setAccessible(true);
		field.set(battleController, snapshot);
		
		Effect effect = new Effect("soin", "lanceur", 20);
		Item item = new Item("potion", "Potion", "Restaure 20 pv.", "soin", effect, "potion.png");
		player.getInventory().addItem(item, 3);
		
		assertTrue(player.getInventory().getItems().containsKey(item));
		assertEquals(3, (int) player.getInventory().getItems().get(item));

		battleController.useItem(item);

		assertTrue(player.getInventory().getItems().containsKey(item));
		assertEquals(2, (int) player.getInventory().getItems().get(item));

		battleController.useItem(item);

		assertEquals(1, (int) player.getInventory().getItems().get(item));

		battleController.useItem(item);

		assertFalse(player.getInventory().getItems().containsKey(item));
	}

	@Test
	public void testHealItemAppliesEffect() throws Exception {
		Player player = new Player("TestPlayer");
		BattleController battleController = new BattleController(player);

		Bugemon bugemon = new Bugemon(Type.PYRO, 50, 10, 10, 10);
		Team teamA = new Team(List.of(bugemon));
		Team teamB = new Team(List.of(new Bugemon(Type.FLORA, 100, 10, 10, 10)));
		Battle battle = new Battle(teamA, teamB);

		Constructor<BattleSnapshot> constructor = BattleSnapshot.class.getDeclaredConstructor(Battle.class, boolean.class);
		constructor.setAccessible(true);
		BattleSnapshot snapshot = constructor.newInstance(battle, true);

		Field field = BattleController.class.getDeclaredField("battleSnapshot");
		field.setAccessible(true);
		field.set(battleController, snapshot);

		Effect effect = new Effect("soin", "lanceur", 20);
		Item item = new Item("potion", "Potion", "Restaure 20 pv.", "soin", effect, "potion.png");
		player.getInventory().addItem(item, 1);

		battleController.useItem(item);

		assertEquals(70, bugemon.getFightStats().getHp());
	}

	@Test
	public void testStatModifierAppliesEffect() throws Exception {
		Player player = new Player("TestPlayer");
		BattleController battleController = new BattleController(player);

		Bugemon bugemon = new Bugemon(Type.PYRO, 100, 10, 10, 10);
		Team teamA = new Team(List.of(bugemon));
		Team teamB = new Team(List.of(new Bugemon(Type.FLORA, 100, 10, 10, 10)));
		Battle battle = new Battle(teamA, teamB);

		Constructor<BattleSnapshot> constructor = BattleSnapshot.class.getDeclaredConstructor(Battle.class, boolean.class);
		constructor.setAccessible(true);
		BattleSnapshot snapshot = constructor.newInstance(battle, true);

		Field field = BattleController.class.getDeclaredField("battleSnapshot");
		field.setAccessible(true);
		field.set(battleController, snapshot);

		Effect effect = new Effect("stat_modifier", "lanceur", "attaque", 5, "permanent");
		Item item = new Item("attack boost", "Attack Boost", "Augmente l'attaque de 5 points.", 
			"stat modifier", effect, "attack_boost.png");
		player.getInventory().addItem(item, 1);

		battleController.useItem(item);

		assertEquals(15, bugemon.getFightStats().getAttack());
	}

	@Test
	public void testMultipleStatModifierAppliesEffect() throws Exception {
		Player player = new Player("TestPlayer");
		BattleController battleController = new BattleController(player);

		Bugemon bugemon = new Bugemon(Type.PYRO, 100, 10, 10, 10);
		Team teamA = new Team(List.of(bugemon));
		Team teamB = new Team(List.of(new Bugemon(Type.FLORA, 100, 10, 10, 10)));
		Battle battle = new Battle(teamA, teamB);

		Constructor<BattleSnapshot> constructor = BattleSnapshot.class.getDeclaredConstructor(Battle.class, boolean.class);
		constructor.setAccessible(true);
		BattleSnapshot snapshot = constructor.newInstance(battle, true);

		Field field = BattleController.class.getDeclaredField("battleSnapshot");
		field.setAccessible(true);
		field.set(battleController, snapshot);

		Map<String, Integer> modifiers = Map.of("attaque", 5, "defense", 3);
		Effect effect = new Effect("stat_modifier_multiple", "lanceur", modifiers, "permanent");
		Item item = new Item("mixed boost", "Mixed Boost", "Augmente l'attaque de 5 points et la défense de 3 points.", 
		"stat modifier multiple", effect, "mixed_boost.png");
		player.getInventory().addItem(item, 1);

		battleController.useItem(item);

		assertEquals(15, bugemon.getFightStats().getAttack());
		assertEquals(13, bugemon.getFightStats().getDefense());
	}

	@Test
	public void testSwitchItemTargetAppliesEffect() throws Exception {
		Player player = new Player("TestPlayer");
		BattleController battleController = new BattleController(player);

		Bugemon bugemonA = new Bugemon(Type.PYRO, 100, 10, 10, 10);
		Bugemon bugemonB = new Bugemon(Type.AQUA, 100, 10, 10, 10);
		Team teamA = new Team(List.of(bugemonA, bugemonB));
		Team teamB = new Team(List.of(new Bugemon(Type.FLORA, 100, 10, 10, 10)));
		Battle battle = new Battle(teamA, teamB);

		Constructor<BattleSnapshot> constructor = BattleSnapshot.class.getDeclaredConstructor(Battle.class, boolean.class);
		constructor.setAccessible(true);
		BattleSnapshot snapshot = constructor.newInstance(battle, true);

		Field field = BattleController.class.getDeclaredField("battleSnapshot");
		field.setAccessible(true);
		field.set(battleController, snapshot);

		Effect effect = new Effect("switch", "lanceur");
		Item item = new Item("switch", "Switch", "Switch a un autre Bugemon.", "switch", 
			effect, "switch.png");
		player.getInventory().addItem(item, 1);

		Bugemon activeBefore = snapshot.getBattle().getActiveBugemonA();
		battleController.useItem(item);

		assertNotEquals(activeBefore, snapshot.getBattle().getActiveBugemonA());
		assertEquals(bugemonB, snapshot.getBattle().getActiveBugemonA());
	}

	@Test
	public void testResetMalusItemAppliesEffect() throws Exception {
		Player player = new Player("TestPlayer");
		BattleController battleController = new BattleController(player);

		Bugemon bugemon = new Bugemon(Type.PYRO, 100, 10, 10, 10);
		bugemon.changeFightStats(new Stats(-20, -5, -5, -5)); // Apply a malus to the bugemon
		Team teamA = new Team(List.of(bugemon));
		Team teamB = new Team(List.of(new Bugemon(Type.FLORA, 100, 10, 10, 10)));
		Battle battle = new Battle(teamA, teamB);

		Constructor<BattleSnapshot> constructor = BattleSnapshot.class.getDeclaredConstructor(Battle.class, boolean.class);
		constructor.setAccessible(true);
		BattleSnapshot snapshot = constructor.newInstance(battle, true);

		Field field = BattleController.class.getDeclaredField("battleSnapshot");
		field.setAccessible(true);
		field.set(battleController, snapshot);

		Effect effect = new Effect("reset_malus", "lanceur");
		Item item = new Item("antidote", "Antidote", "Enlève les malus de stats.", "reset malus", 
			effect, "antidote.png");
		player.getInventory().addItem(item, 1);

		battleController.useItem(item);

		assertEquals(100, bugemon.getFightStats().getHp());
		assertEquals(10, bugemon.getFightStats().getAttack());
		assertEquals(10, bugemon.getFightStats().getDefense());
		assertEquals(10, bugemon.getFightStats().getInitiative());
	}
		

	@Test
	public void testDamageUsesBattleSnapshot() throws Exception {
		// Arrange: create a simple battle with two teams
		Bugemon bugemon1 = new Bugemon(Type.PYRO, 100, 10, 10, 10);
		Bugemon bugemon2 = new Bugemon(Type.FLORA, 100, 10, 10, 10);
		Bugemon bugemon3 = new Bugemon(Type.AQUA, 100, 10, 10, 10);
		Bugemon bugemon4 = new Bugemon(Type.LITHO, 100, 10, 10, 10);
		Bugemon bugemon5 = new Bugemon(Type.PYRO, 100, 10, 10, 10);
		Bugemon bugemon6 = new Bugemon(Type.FLORA, 100, 10, 10, 10);

		Team team1 = new Team(List.of(bugemon1, bugemon2, bugemon3, bugemon4, bugemon5, bugemon6));
		Team team2 = new Team(List.of(bugemon1, bugemon2, bugemon3, bugemon4, bugemon5, bugemon6));

		Battle battle = new Battle(team1, team2);
		BattleSnapshot snapshot = new BattleSnapshot(battle, true);

		// Wire the snapshot into the controller using reflection on the private field
		Player player = new Player("TestPlayer");
		BattleController controller = new BattleController(player);

		Field field = BattleController.class.getDeclaredField("battleSnapshot");
		field.setAccessible(true);
		field.set(controller, snapshot);

		// Active opponent Bugemon before damage
		int initialHp = snapshot.getBattle().getActiveBugemonB().getFightStats().hp;

		Ability ability = new Ability("1", "Test Ability", Type.PYRO, "simple attack", 10);

		// Act: use the useAbility method on the controller
		controller.useAbility(ability);

		// Assert: opponent's HP should have decreased
		int finalHp = snapshot.getBattle().getActiveBugemonB().getFightStats().hp;
		assertTrue(finalHp < initialHp);
	}

}
