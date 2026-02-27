package ulb.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ulb.model.Effect;
import ulb.model.Item;
import ulb.model.Player;

public class BattleControllerTest {

	@Test
	public void testUseItem() {
		Player player = new Player("TestPlayer");
		player.getInventory().getItems().clear();
		BattleController battleController = new BattleController(player);
		
		Effect effect = new Effect("soin", "lanceur", 20);
		Item item = new Item("potion", "Potion", "Restaure 20 PV.", "soin", effect, "potion.png");
		
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
}
