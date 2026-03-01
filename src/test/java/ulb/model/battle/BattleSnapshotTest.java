package ulb.model.battle;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import ulb.model.Bugemon;
import ulb.model.ability.Ability;
import ulb.model.team.Team;
import ulb.model.type.Type;
import java.util.List;

public class BattleSnapshotTest {
	private Battle getBattleA(){
		Bugemon bugemon1 = new Bugemon("pyricore", Type.PYRO, 100, 10, 10, 10, 1);
		Bugemon bugemon2 = new Bugemon("moussil", Type.FLORA, 100, 10, 10, 10, 1);
		Bugemon bugemon3 = new Bugemon("refaquix", Type.AQUA, 100, 10, 10, 10, 1);
		Bugemon bugemon4 = new Bugemon("granitron", Type.LITHO, 100, 10, 10, 10, 1);
		Bugemon bugemon5 = new Bugemon("inferlin", Type.PYRO, 100, 10, 10, 10, 1);
		Bugemon bugemon6 = new Bugemon("florachu", Type.FLORA, 100, 10, 10, 10, 1);

		Team team1 = new Team(List.of(bugemon1, bugemon2, bugemon3, bugemon4, bugemon5, bugemon6));
		Team team2 = new Team(List.of(bugemon1, bugemon2, bugemon3, bugemon4, bugemon5, bugemon6));

		Battle battle = new Battle(team1, team2);
		return battle;
	}

	@Test
	public void testGetTeamSelf(){
		Battle battle = getBattleA();
		BattleSnapshot snapshotA = new BattleSnapshot(battle, true);
		BattleSnapshot snapshotB = new BattleSnapshot(battle, false);

		assertSame(battle.getTeamA(), snapshotA.getTeamSelf());
		assertSame(battle.getTeamB(), snapshotB.getTeamSelf());
	}

	@Test
	public void testGetTeamOpponent(){
		Battle battle = getBattleA();
		BattleSnapshot snapshotA = new BattleSnapshot(battle, true);
		BattleSnapshot snapshotB = new BattleSnapshot(battle, false);

		assertSame(battle.getTeamB(), snapshotA.getTeamOpponent());
		assertSame(battle.getTeamA(), snapshotB.getTeamOpponent());
	}

	@Test
	public void testDamageNormalTypeFactor(){
		Bugemon bugemon1 = new Bugemon("pyricore", Type.PYRO, 100, 10, 10, 10, 1);
		Bugemon bugemon2 = new Bugemon("moussil", Type.FLORA, 100, 10, 10, 10, 1);
		Bugemon bugemon3 = new Bugemon("refaquix", Type.AQUA, 100, 10, 10, 10, 1);
		Bugemon bugemon4 = new Bugemon("granitron", Type.LITHO, 100, 10, 10, 10, 1);
		Bugemon bugemon5 = new Bugemon("inferlin", Type.PYRO, 100, 10, 10, 10, 1);
		Bugemon bugemon6 = new Bugemon("florachu", Type.FLORA, 100, 10, 10, 10, 1);

		Team team1 = new Team(List.of(bugemon1, bugemon2, bugemon3, bugemon4, bugemon5, bugemon6));
		Team team2 = new Team(List.of(bugemon1, bugemon2, bugemon3, bugemon4, bugemon5, bugemon6));

		BattleSnapshot snapshot = new BattleSnapshot(new Battle(team1, team2), true);

		Ability abilityTest = new Ability("1", "a", Type.PYRO, "lance une boule d'eau", 10);
		snapshot.useAbility(abilityTest);

		Bugemon damagedBugemon = snapshot.getBattle().getActiveBugemonB();

		assertTrue(damagedBugemon.getFighStats().hp == 90 || damagedBugemon.getFighStats().hp == 85);
	}

	@Test
	public void testDamageHighTypeFactor(){
		Bugemon bugemon1 = new Bugemon("pyricore", Type.PYRO, 100, 10, 10, 10, 1);
		Bugemon bugemon2 = new Bugemon("moussil", Type.FLORA, 100, 10, 10, 10, 1);
		Bugemon bugemon3 = new Bugemon("refaquix", Type.AQUA, 100, 10, 10, 10, 1);
		Bugemon bugemon4 = new Bugemon("granitron", Type.LITHO, 100, 10, 10, 10, 1);
		Bugemon bugemon5 = new Bugemon("inferlin", Type.PYRO, 100, 10, 10, 10, 1);
		Bugemon bugemon6 = new Bugemon("florachu", Type.FLORA, 100, 10, 10, 10, 1);

		Team team1 = new Team(List.of(bugemon1, bugemon2, bugemon3, bugemon4, bugemon5, bugemon6));
		Team team2 = new Team(List.of(bugemon1, bugemon2, bugemon3, bugemon4, bugemon5, bugemon6));

		BattleSnapshot snapshot = new BattleSnapshot(new Battle(team1, team2, bugemon1, bugemon4), true);

		Ability abilityTest = new Ability("1", "a", Type.PYRO, "lance une boule d'eau", 10);
		snapshot.useAbility(abilityTest);

		Bugemon damagedBugemon = snapshot.getBattle().getActiveBugemonB();

		assertTrue(damagedBugemon.getFighStats().hp == 85 || damagedBugemon.getFighStats().hp == 77);
	}

	@Test
	public void testDamageLowTypeFactor(){
		Bugemon bugemon1 = new Bugemon("pyricore", Type.PYRO, 100, 10, 10, 10, 1);
		Bugemon bugemon2 = new Bugemon("moussil", Type.FLORA, 100, 10, 10, 10, 1);
		Bugemon bugemon3 = new Bugemon("refaquix", Type.AQUA, 100, 10, 10, 10, 1);
		Bugemon bugemon4 = new Bugemon("granitron", Type.LITHO, 100, 10, 10, 10, 1);
		Bugemon bugemon5 = new Bugemon("inferlin", Type.PYRO, 100, 10, 10, 10, 1);
		Bugemon bugemon6 = new Bugemon("florachu", Type.FLORA, 100, 10, 10, 10, 1);

		Team team1 = new Team(List.of(bugemon1, bugemon2, bugemon3, bugemon4, bugemon5, bugemon6));
		Team team2 = new Team(List.of(bugemon1, bugemon2, bugemon3, bugemon4, bugemon5, bugemon6));

		BattleSnapshot snapshot = new BattleSnapshot(new Battle(team1, team2, bugemon4, bugemon1), true);

		Ability abilityTest = new Ability("1", "a", Type.PYRO, "lance une boule d'eau", 10);
		snapshot.useAbility(abilityTest);

		Bugemon damagedBugemon = snapshot.getBattle().getActiveBugemonB();

		assertTrue(damagedBugemon.getFighStats().hp == 92 || damagedBugemon.getFighStats().hp == 88);
	}

}
