package ulb.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

import ulb.model.Bugemon;
import ulb.model.Effect;
import ulb.model.Item;
import ulb.model.Player;
import ulb.model.ability.Ability;
import ulb.model.battle.Battle;
import ulb.model.battle.BattleSnapshot;
import ulb.model.team.Team;
import ulb.model.type.Type;

public class BattleControllerTest {

	@Test
	public void testUseItem() {
		Player player = new Player("TestPlayer");
		player.getInventory().getItems().clear();
		BattleController battleController = new BattleController(player);
		
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
	public void testDamageUsesBattleSnapshot() throws Exception {
		// Arrange: create a simple battle with two teams
		Bugemon bugemon1 = new Bugemon("pyricore", Type.PYRO, 100, 10, 10, 10, 1);
		Bugemon bugemon2 = new Bugemon("moussil", Type.FLORA, 100, 10, 10, 10, 1);
		Bugemon bugemon3 = new Bugemon("refaquix", Type.AQUA, 100, 10, 10, 10, 1);
		Bugemon bugemon4 = new Bugemon("granitron", Type.LITHO, 100, 10, 10, 10, 1);
		Bugemon bugemon5 = new Bugemon("inferlin", Type.PYRO, 100, 10, 10, 10, 1);
		Bugemon bugemon6 = new Bugemon("florachu", Type.FLORA, 100, 10, 10, 10, 1);

		Team team1 = new Team(List.of(bugemon1, bugemon2, bugemon3, bugemon4, bugemon5, bugemon6));
		Team team2 = new Team(List.of(bugemon1, bugemon2, bugemon3, bugemon4, bugemon5, bugemon6));

		Battle battle = new Battle(team1, team2);

		// Create a BattleSnapshot instance via reflection (constructor is package-private)
		Constructor<BattleSnapshot> constructor =
				BattleSnapshot.class.getDeclaredConstructor(Battle.class, boolean.class);
		constructor.setAccessible(true);
		BattleSnapshot snapshot = constructor.newInstance(battle, true);

		// Wire the snapshot into the controller using reflection on the private field
		Player player = new Player("TestPlayer");
		BattleController controller = new BattleController(player);

		Field field = BattleController.class.getDeclaredField("battleSnapshot");
		field.setAccessible(true);
		field.set(controller, snapshot);

		// Active opponent Bugemon before damage
		int initialHp = snapshot.getBattle().getActiveBugemonB().getFighStats().hp;

		Ability ability = new Ability("1", "Test Ability", Type.PYRO, "simple attack", 10);

		// Act: use the Damage method on the controller
		controller.Damage(ability);

		// Assert: opponent's HP should have decreased
		int finalHp = snapshot.getBattle().getActiveBugemonB().getFighStats().hp;
		assertTrue(finalHp < initialHp);
	}

}
