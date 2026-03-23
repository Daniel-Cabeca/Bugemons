package ulb.model.item;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import ulb.controller.BattleController;
import ulb.controller.action.UseAbility;
import ulb.controller.action.UseItem;
import ulb.model.Player;
import ulb.model.ability.Ability;
import ulb.model.battle.Battle;
import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.Stats;
import ulb.model.sample.BugemonSample;
import ulb.model.sample.EffectSample;
import ulb.model.team.Team;
import ulb.model.effect.Effect;
import ulb.model.sample.AbilitySample;

public class ItemTest {
	public Battle battle;
	public BattleController playerController;
	public BattleController otherPlayerController;
	public Player player;

	private void otherPlayerChooseAction(){
		Ability a = AbilitySample.getH();
		otherPlayerController.useAction(new UseAbility(a));
	}

	private void setupBattle(Bugemon... bugemons) {
		List<Bugemon> bugemonList = bugemons.length > 0 ? List.of(bugemons) : List.of(BugemonSample.getA());
		Team teamA = new Team(bugemonList);
		Team teamB = new Team(List.of(BugemonSample.getB()));
		this.player = new Player("TestPlayer");
		this.player.setTeam(teamA);
		Player otherPlayer = new Player("OtherPlayer");
		otherPlayer.setTeam(teamB);
		this.battle = new Battle(teamA, teamB, this.player, otherPlayer);
		this.playerController = new BattleController(this.player, battle, true);
		this.otherPlayerController = new BattleController(otherPlayer, battle, false);
	}

	private void playTurnWithPassiveOpponent(UseItem action) {
		this.playerController.useAction(action);
		otherPlayerChooseAction();
	}

    @Test
	public void healItemAppliesFullEffect() {
		Bugemon bugemon = BugemonSample.getA();
		bugemon.changeFightStats(new Stats(-20, 0, 0, 0));

		setupBattle(bugemon);

		Effect effect = EffectSample.getHeal();
		Item item = new Item("potion", "Potion", "Restaure 10 pv.", "soin", effect, "potion.png");
		player.getInventory().addItem(item, 1);

		playTurnWithPassiveOpponent(new UseItem(item));
		assertEquals(90, bugemon.getFightStats().getHp());
	}

	@Test
	public void healItemAppliesPartialEffect() {
		Bugemon bugemon = BugemonSample.getA();
		bugemon.changeFightStats(new Stats(-5, 0, 0, 0));

		setupBattle(bugemon);

		Effect effect = EffectSample.getHeal();
		Item item = new Item("potion", "Potion", "Restaure 10 pv.", "soin", effect, "potion.png");
		player.getInventory().addItem(item, 1);

		playTurnWithPassiveOpponent(new UseItem(item));

		assertEquals(100, bugemon.getFightStats().getHp());
	}

	@Test
	public void statModifierAppliesEffect() {
		Bugemon bugemon = BugemonSample.getA();
        bugemon.changeFightStats(new Stats(0, -5, 0, 0));

		setupBattle(bugemon);

		Effect effect = EffectSample.getAttackIncreaseSelf();
		Item item = new Item("attack boost", "Attack Boost", "Augmente l'attaque de 10 points.", 
			"stat modifier", effect, "attack_boost.png");
		player.getInventory().addItem(item, 1);

		playTurnWithPassiveOpponent(new UseItem(item));

		assertEquals(15, bugemon.getFightStats().getAttack());
	}

	@Test
	public void multipleStatModifierAppliesEffect() {
		Bugemon bugemon = BugemonSample.getA();
        bugemon.changeFightStats(new Stats(0, -5, -5, 0));

		setupBattle(bugemon);

		Effect effect = EffectSample.getMultiEffect();
		Item item = new Item("mixed boost", "Mixed Boost", "Augmente l'attaque et la défense de 10 points.", 
		"stat modifier multiple", effect, "mixed_boost.png");
		player.getInventory().addItem(item, 1);

		playTurnWithPassiveOpponent(new UseItem(item));

		assertEquals(15, bugemon.getFightStats().getAttack());
		assertEquals(15, bugemon.getFightStats().getDefense());
	}

	@Test
	public void switchItemAppliesEffect() {
		Bugemon bugemonA = BugemonSample.getA();
		Bugemon bugemonB = BugemonSample.getB();
		Team teamA = new Team(List.of(bugemonA, bugemonB));
		Team teamB = new Team(List.of(BugemonSample.getC()));
		player = new Player("TestPlayer");
		player.setTeam(teamA);
		Player otherPlayer = new Player("OtherPlayer");
		otherPlayer.setTeam(teamB);
		battle = new Battle(teamA, teamB, player, otherPlayer);
		playerController = new BattleController(player, battle, true);
		otherPlayerController = new BattleController(otherPlayer, battle, false);

		Effect effect = EffectSample.getSwitchEffect();
		Item item = new Item("switch", "Switch", "Switch a un autre Bugemon.", "switch", 
			effect, "switch.png");
		player.getInventory().addItem(item, 1);

		Bugemon activeBefore = battle.getActiveBugemon(battle.getTeamLabel(true));
		playTurnWithPassiveOpponent(new UseItem(item));

		assertNotEquals(activeBefore, battle.getActiveBugemon(battle.getTeamLabel(true)));
		assertEquals(bugemonB, battle.getActiveBugemon(battle.getTeamLabel(true)));
	}

	@Test
	public void resetMalusItemAppliesEffect() {
		Bugemon bugemon = BugemonSample.getA();
		bugemon.changeFightStats(new Stats(-5, -5, -5, -5)); 

		setupBattle(bugemon);

		Effect effect = EffectSample.getResetMalusEffect();
		Item item = new Item("antidote", "Antidote", "Enlève les malus de stats.", "reset malus", 
			effect, "antidote.png");
		player.getInventory().addItem(item, 1);

		playTurnWithPassiveOpponent(new UseItem(item));

		assertEquals(100, bugemon.getFightStats().getHp());
		assertEquals(10, bugemon.getFightStats().getAttack());
		assertEquals(10, bugemon.getFightStats().getDefense());
		assertEquals(10, bugemon.getFightStats().getInitiative());
	}
    
}
