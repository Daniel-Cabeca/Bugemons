package ulb.model.item;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.jupiter.api.Test;

import ulb.controller.BattleController;
import ulb.model.Player;
import ulb.model.ability.Ability;
import ulb.model.battle.Battle;
import ulb.model.battle.BattleSnapshot;
import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.Stats;
import ulb.model.sample.BugemonSample;
import ulb.model.sample.EffectSample;
import ulb.model.team.Team;
import ulb.model.Effect;
import ulb.model.type.Type;

public class ItemTest {

	private void setupBattle(BattleController controller, Bugemon... bugemons) throws Exception {
		List<Bugemon> bugemonList = bugemons.length > 0 ? List.of(bugemons) : List.of(BugemonSample.getA());
		Team teamA = new Team(bugemonList);
		Team teamB = new Team(List.of(BugemonSample.getB()));
		Battle battle = new Battle(teamA, teamB);

		BattleSnapshot snapshot = new BattleSnapshot(battle, true);

		Field field = BattleController.class.getDeclaredField("battleSnapshot");
		field.setAccessible(true);
		field.set(controller, snapshot);
	}

    @Test
	public void testHealItemAppliesEffect() throws Exception {
		Player player = new Player("TestPlayer");
		BattleController battleController = new BattleController(player);

		Bugemon bugemon = BugemonSample.getA();
		bugemon.changeFightStats(new Stats(-20, 0, 0, 0));

		setupBattle(battleController, bugemon);

		Effect effect = EffectSample.getHeal();
		Item item = new Item("potion", "Potion", "Restaure 10 pv.", "soin", effect, "potion.png");
		player.getInventory().addItem(item, 1);

		battleController.useItem(item);

		assertEquals(90, bugemon.getFightStats().getHp());
	}

	@Test
	public void testOverHealedBugemon() throws Exception {
		Player player = new Player("TestPlayer");
		BattleController battleController = new BattleController(player);

		Bugemon bugemon = BugemonSample.getA();
		bugemon.changeFightStats(new Stats(-5, 0, 0, 0));

		setupBattle(battleController, bugemon);

		Effect effect = EffectSample.getHeal();
		Item item = new Item("potion", "Potion", "Restaure 10 pv.", "soin", effect, "potion.png");
		player.getInventory().addItem(item, 1);

		battleController.useItem(item);

		assertEquals(100, bugemon.getFightStats().getHp());
	}

	@Test
	public void testStatModifierAppliesEffect() throws Exception {
		Player player = new Player("TestPlayer");
		BattleController battleController = new BattleController(player);

		Bugemon bugemon = BugemonSample.getA();
        bugemon.changeFightStats(new Stats(0, -5, 0, 0));

		setupBattle(battleController, bugemon);

		Effect effect = EffectSample.getAttackIncreaseSelf();
		Item item = new Item("attack boost", "Attack Boost", "Augmente l'attaque de 10 points.", 
			"stat modifier", effect, "attack_boost.png");
		player.getInventory().addItem(item, 1);

		battleController.useItem(item);

		assertEquals(15, bugemon.getFightStats().getAttack());
	}

	@Test
	public void testMultipleStatModifierAppliesEffect() throws Exception {
		Player player = new Player("TestPlayer");
		BattleController battleController = new BattleController(player);

		Bugemon bugemon = BugemonSample.getA();
        bugemon.changeFightStats(new Stats(0, -5, -5, 0));

		setupBattle(battleController, bugemon);

		Effect effect = EffectSample.getMultiEffect();
		Item item = new Item("mixed boost", "Mixed Boost", "Augmente l'attaque et la défense de 10 points.", 
		"stat modifier multiple", effect, "mixed_boost.png");
		player.getInventory().addItem(item, 1);

		battleController.useItem(item);

		assertEquals(15, bugemon.getFightStats().getAttack());
		assertEquals(15, bugemon.getFightStats().getDefense());
	}

	@Test
	public void testSwitchItemTargetAppliesEffect() throws Exception {
		Player player = new Player("TestPlayer");
		BattleController battleController = new BattleController(player);

		Bugemon bugemonA = BugemonSample.getA();
		Bugemon bugemonB = BugemonSample.getB();
		Team teamA = new Team(List.of(bugemonA, bugemonB));
		Team teamB = new Team(List.of(BugemonSample.getC()));
		Battle battle = new Battle(teamA, teamB);

		BattleSnapshot snapshot = new BattleSnapshot(battle, true);

		Field field = BattleController.class.getDeclaredField("battleSnapshot");
		field.setAccessible(true);
		field.set(battleController, snapshot);

		Effect effect = EffectSample.getSwitchEffect();
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

		Bugemon bugemon = BugemonSample.getA();
		bugemon.changeFightStats(new Stats(-5, -5, -5, -5)); 

		setupBattle(battleController, bugemon);

		Effect effect = EffectSample.getResetMalusEffect();
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
		Bugemon bugemon1 = BugemonSample.getA();
        Bugemon bugemon2 = BugemonSample.getB();
        Bugemon bugemon3 = BugemonSample.getC();
        Bugemon bugemon4 = BugemonSample.getD();
        Bugemon bugemon5 = BugemonSample.getE();
        Bugemon bugemon6 = BugemonSample.getF();

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

		// Act: use the Damage method on the controller
		controller.useAbility(ability);

		// Assert: opponent's HP should have decreased
		int finalHp = snapshot.getBattle().getActiveBugemonB().getFightStats().hp;
		assertTrue(finalHp < initialHp);
	}
    
}
