package ulb.model.battle;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;


import ulb.model.bugemon.Bugemon;
import ulb.model.ability.Ability;
import ulb.model.team.Team;
import ulb.model.type.Type;
import ulb.controller.action.*;
import ulb.model.item.*;
import java.util.List;
import java.util.Vector;

public class BattleSnapshotTest {
	private Battle getBattleA(){
		Bugemon bugemon1 = new Bugemon("pyricore", Type.PYRO, 100, 10, 10, 10);
		Bugemon bugemon2 = new Bugemon("moussil", Type.FLORA, 100, 10, 10, 10);
		Bugemon bugemon3 = new Bugemon("refaquix", Type.AQUA, 100, 10, 10, 10);
		Bugemon bugemon4 = new Bugemon("granitron", Type.LITHO, 100, 10, 10, 10);
		Bugemon bugemon5 = new Bugemon("inferlin", Type.PYRO, 100, 10, 10, 10);
		Bugemon bugemon6 = new Bugemon("florachu", Type.FLORA, 100, 10, 10, 10);

		Team team1 = new Team(List.of(bugemon1, bugemon2, bugemon3, bugemon4, bugemon5, bugemon6));
		Team team2 = new Team(List.of(bugemon1, bugemon2, bugemon3, bugemon4, bugemon5, bugemon6));

		Battle battle = new Battle(team1, team2);
		return battle;
	}

	@Test
	public void testGetTeamSelf() {
		Battle battle = getBattleA();
		BattleSnapshot snapshotA = new BattleSnapshot(battle, true);
		BattleSnapshot snapshotB = new BattleSnapshot(battle, false);

		assertSame(battle.getTeamA(), snapshotA.getTeamSelf());
		assertSame(battle.getTeamB(), snapshotB.getTeamSelf());
	}

	@Test
	public void testGetTeamOpponent() {
		Battle battle = getBattleA();
		BattleSnapshot snapshotA = new BattleSnapshot(battle, true);
		BattleSnapshot snapshotB = new BattleSnapshot(battle, false);

		assertSame(battle.getTeamB(), snapshotA.getTeamOpponent());
		assertSame(battle.getTeamA(), snapshotB.getTeamOpponent());
	}

	@Test
	public void testGetActiveBugemonSelf() {
		Battle battle = getBattleA();
		BattleSnapshot snapshotA = new BattleSnapshot(battle, true);
		BattleSnapshot snapshotB = new BattleSnapshot(battle, false);

		assertSame(battle.getActiveBugemonA(), snapshotA.getActiveBugemonSelf());
		assertSame(battle.getActiveBugemonB(), snapshotB.getActiveBugemonSelf());
	}

	@Test
	public void testGetActiveBugemonOpponent() {
		Battle battle = getBattleA();
		BattleSnapshot snapshotA = new BattleSnapshot(battle, true);
		BattleSnapshot snapshotB = new BattleSnapshot(battle, false);

		assertSame(battle.getActiveBugemonB(), snapshotA.getActiveBugemonOpponent());
		assertSame(battle.getActiveBugemonA(), snapshotA.getActiveBugemonOpponent());
	}

	@Test
	public void testSetActiveBugemonSelf() {
		Battle battle = getBattleA();
		BattleSnapshot snapshotA = new BattleSnapshot(battle, true);
		BattleSnapshot snapshotB = new BattleSnapshot(battle, false);

		Bugemon newBugemonA = snapshotA.getTeamSelf().getBugemon(3);
		Bugemon newBugemonB = snapshotA.getTeamSelf().getBugemon(3);

		snapshotA.setActiveBugemonSelf(newBugemonA);
		snapshotB.setActiveBugemonSelf(newBugemonB);

		assertSame(battle.getActiveBugemonA(), snapshotA.getActiveBugemonSelf());
		assertSame(battle.getActiveBugemonB(), snapshotB.getActiveBugemonSelf());

		assertSame(newBugemonA, snapshotA.getActiveBugemonSelf());
		assertSame(newBugemonB, snapshotB.getActiveBugemonSelf());
	}

	@Test
	public void testDamageNormalTypeFactor(){
		Bugemon bugemon1 = new Bugemon("pyricore", Type.PYRO, 100, 10, 10, 10);
		Bugemon bugemon2 = new Bugemon("moussil", Type.FLORA, 100, 10, 10, 10);
		Bugemon bugemon3 = new Bugemon("refaquix", Type.AQUA, 100, 10, 10, 10);
		Bugemon bugemon4 = new Bugemon("granitron", Type.LITHO, 100, 10, 10, 10);
		Bugemon bugemon5 = new Bugemon("inferlin", Type.PYRO, 100, 10, 10, 10);
		Bugemon bugemon6 = new Bugemon("florachu", Type.FLORA, 100, 10, 10, 10);

		Team team1 = new Team(List.of(bugemon1, bugemon2, bugemon3, bugemon4, bugemon5, bugemon6));
		Team team2 = new Team(List.of(bugemon1, bugemon2, bugemon3, bugemon4, bugemon5, bugemon6));

		BattleSnapshot snapshot = new BattleSnapshot(new Battle(team1, team2), true);

		Ability abilityTest = new Ability("1", "a", Type.PYRO, "lance une boule d'eau", 10);
		snapshot.useAbility(abilityTest);

		Bugemon damagedBugemon = snapshot.getBattle().getActiveBugemonB();

		assertTrue(damagedBugemon.getFightStats().hp == 90 || damagedBugemon.getFightStats().hp == 85);
	}

	@Test
	public void testDamageHighTypeFactor(){
		Bugemon bugemon1 = new Bugemon("pyricore", Type.PYRO, 100, 10, 10, 10);
		Bugemon bugemon2 = new Bugemon("moussil", Type.FLORA, 100, 10, 10, 10);
		Bugemon bugemon3 = new Bugemon("refaquix", Type.AQUA, 100, 10, 10, 10);
		Bugemon bugemon4 = new Bugemon("granitron", Type.LITHO, 100, 10, 10, 10);
		Bugemon bugemon5 = new Bugemon("inferlin", Type.PYRO, 100, 10, 10, 10);
		Bugemon bugemon6 = new Bugemon("florachu", Type.FLORA, 100, 10, 10, 10);

		Team team1 = new Team(List.of(bugemon1, bugemon2, bugemon3, bugemon4, bugemon5, bugemon6));
		Team team2 = new Team(List.of(bugemon1, bugemon2, bugemon3, bugemon4, bugemon5, bugemon6));

		BattleSnapshot snapshot = new BattleSnapshot(new Battle(team1, team2, bugemon1, bugemon4), true);

		Ability abilityTest = new Ability("1", "a", Type.PYRO, "lance une boule d'eau", 10);
		snapshot.useAbility(abilityTest);

		Bugemon damagedBugemon = snapshot.getBattle().getActiveBugemonB();

		assertTrue(damagedBugemon.getFightStats().hp == 85 || damagedBugemon.getFightStats().hp == 77);
	}

	@Test
	public void testDamageLowTypeFactor(){
		Bugemon bugemon1 = new Bugemon("pyricore", Type.PYRO, 100, 10, 10, 10);
		Bugemon bugemon2 = new Bugemon("moussil", Type.FLORA, 100, 10, 10, 10);
		Bugemon bugemon3 = new Bugemon("refaquix", Type.AQUA, 100, 10, 10, 10);
		Bugemon bugemon4 = new Bugemon("granitron", Type.LITHO, 100, 10, 10, 10);
		Bugemon bugemon5 = new Bugemon("inferlin", Type.PYRO, 100, 10, 10, 10);
		Bugemon bugemon6 = new Bugemon("florachu", Type.FLORA, 100, 10, 10, 10);

		Team team1 = new Team(List.of(bugemon1, bugemon2, bugemon3, bugemon4, bugemon5, bugemon6));
		Team team2 = new Team(List.of(bugemon1, bugemon2, bugemon3, bugemon4, bugemon5, bugemon6));

		BattleSnapshot snapshot = new BattleSnapshot(new Battle(team1, team2, bugemon4, bugemon1), true);

		Ability abilityTest = new Ability("1", "a", Type.LITHO, "lance une boule d'eau", 10);
		snapshot.useAbility(abilityTest);

		Bugemon damagedBugemon = snapshot.getBattle().getActiveBugemonB();


		assertTrue(damagedBugemon.getFightStats().hp == 92 || damagedBugemon.getFightStats().hp == 89);
	}

	@Test
	public void availableActionsDoesNotFailAfterConstruction() {
		Battle battle = getBattleA();
		BattleSnapshot snapshot = new BattleSnapshot(battle, true);
		assertEquals(4, snapshot.getAvailableActions().size());
	}

	@Test
	public void damageOfUsedAbilityNotTypeOfAttack() {
		Bugemon attacker = new Bugemon("att", Type.PYRO, 100, 10, 10, 10);
		Bugemon defender = new Bugemon("def", Type.AQUA, 100, 10, 10, 10);

		Team t1 = new Team(List.of(attacker));
		Team t2 = new Team(List.of(defender));
		BattleSnapshot snap = new BattleSnapshot(new Battle(t1, t2, attacker, defender), true);
		
		// Attaque FLORA contre AQUA --> super efficace / HIGH (1.5)
		Ability floraAttack = new Ability("xyz", "flora", Type.FLORA, "description", 10);
		snap.useAbility(floraAttack);
		int hp = snap.getBattle().getActiveBugemonB().getFightStats().hp;
		assertTrue(hp == 85 || hp == 77);	// 10 * 1.5 (* 1.5)
	}

	@Test
	public void tourEndedAfterUseAction() {
		Bugemon attacker = new Bugemon("att", Type.PYRO, 100, 10, 10, 10);
		Bugemon defender = new Bugemon("def", Type.AQUA, 100, 10, 10, 10);

		Team t1 = new Team(List.of(attacker));
		Team t2 = new Team(List.of(defender));
		BattleSnapshot snap = new BattleSnapshot(new Battle(t1, t2, attacker, defender), true);

		Action useAbility = new UseAbility(new Ability("1", "a", Type.AQUA, "rien", 10));

		snap.useAction(useAbility);

		assertTrue(snap.getTourInfo());
	}

	@Test
	public void tourBegun() {
		Bugemon attacker = new Bugemon("att", Type.PYRO, 100, 10, 10, 10);
		Bugemon defender = new Bugemon("def", Type.AQUA, 100, 10, 10, 10);

		Team t1 = new Team(List.of(attacker));
		Team t2 = new Team(List.of(defender));
		BattleSnapshot snap = new BattleSnapshot(new Battle(t1, t2, attacker, defender), true);

		Vector<Action> actions = snap.getAvailableActions();

		assertFalse(snap.getTourInfo());

	}

}
