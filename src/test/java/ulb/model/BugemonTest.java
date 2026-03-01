package ulb.model;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Vector;
import java.util.List;

import ulb.model.type.Type;
import ulb.utils.Stats;

public class BugemonTest {

	int getGainedPoint(Stats previous, Stats actual){
		Stats difference = new Stats(actual);
		Stats opposite = new Stats(-previous.hp, -previous.attack, -previous.defense, -previous.initiative);
		difference.add(opposite);
		return difference.hp / 2 + difference.initiative / 2 + difference.attack + difference.defense;
	}

	@Test
	public void xpBelowLevel() {
		Bugemon B = new Bugemon("A", Type.AQUA, 10, 29, 35, 16, 1);
		int levelGained = B.gainXP(49);
		assertEquals(1, B.getLevel());
		assertEquals(49, B.getXP());
		assertEquals(0, levelGained);
	}

	@Test
	public void xpAbouveLevel() {
		Bugemon B = new Bugemon("A", Type.AQUA, 10, 29, 35, 16, 1);
		int levelGained = B.gainXP(51);
		assertEquals(2, B.getLevel());
		assertEquals(1, B.getXP());
		assertEquals(1, levelGained);
	}

	@Test
	public void xpAboveMultipleLevels() {
		Bugemon B = new Bugemon("A", Type.AQUA, 10, 29, 35, 16, 1);
		int levelGained = B.gainXP(300);
		assertEquals(4, B.getLevel());
		assertEquals(0, B.getXP());
		assertEquals(3, levelGained);
	}

	@Test
	public void rewardSingleLevelGained(){
		Stats s = new Stats(10,29, 35, 16);
		Bugemon B = new Bugemon("A", Type.AQUA, s.hp, s.attack, s.defense, s.initiative, 1);
		int levelGained = B.gainXP(51);
		B.gainLevelsReward(levelGained);
		assertEquals(10, this.getGainedPoint(s, B.getBaseStats()));
	}

	@Test
	public void rewardMultiLevelGained(){
		Stats s = new Stats(10,29, 35, 16);
		Bugemon B = new Bugemon("A", Type.AQUA, s.hp, s.attack, s.defense, s.initiative, 1);
		int levelGained = B.gainXP(300);
		B.gainLevelsReward(levelGained);
		assertEquals(30, this.getGainedPoint(s, B.getBaseStats()));
	}

	@Test
	public void testResetsFightStats(){
		Stats s = new Stats(-10,-9, -5, -6); // debuff stats
		Bugemon B = new Bugemon("A", Type.AQUA, 100, 20, 10, 10, 1);

		B.changeFightStats(s);

		assertEquals(90, B.getFightStats().hp);
		assertEquals(11, B.getFightStats().attack);
		assertEquals(5, B.getFightStats().defense);
		assertEquals(4, B.getFightStats().initiative);

		B.resetFightStats();

		assertEquals(B.getBaseStats().hp, B.getFightStats().hp);
		assertEquals(B.getBaseStats().attack, B.getFightStats().attack);
		assertEquals(B.getBaseStats().defense, B.getFightStats().defense);
		assertEquals(B.getBaseStats().initiative, B.getFightStats().initiative);
	}
}
