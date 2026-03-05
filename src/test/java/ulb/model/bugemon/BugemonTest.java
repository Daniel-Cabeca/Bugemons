package ulb.model.bugemon;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Vector;
import java.util.List;

import ulb.model.type.Type;

public class BugemonTest {

	int getGainedPoint(Stats previous, Stats actual){
		Stats difference = new Stats(actual);
		Stats opposite = new Stats(-previous.hp, -previous.attack, -previous.defense, -previous.initiative);
		difference.change(opposite);
		return difference.hp / 2 + difference.initiative / 2 + difference.attack + difference.defense;
	}

	@Test
	public void xpBelowLevel() {
		Bugemon B = new Bugemon(Type.AQUA, 10, 29, 35, 16);
		int levelGained = B.gainXp(49);
		assertEquals(1, B.getLevel());
		assertEquals(49, B.getXp());
		assertEquals(0, levelGained);
	}

	@Test
	public void xpAboveLevel() {
		Bugemon B = new Bugemon(Type.AQUA, 10, 29, 35, 16);
		int levelGained = B.gainXp(51);
		assertEquals(2, B.getLevel());
		assertEquals(1, B.getXp());
		assertEquals(1, levelGained);
	}

	@Test
	public void xpAboveMultipleLevels() {
		Bugemon B = new Bugemon(Type.AQUA, 10, 29, 35, 16);
		int levelGained = B.gainXp(300);
		assertEquals(4, B.getLevel());
		assertEquals(0, B.getXp());
		assertEquals(3, levelGained);
	}

	@Test
	public void rewardSingleLevelGained(){
		Stats s = new Stats(10,29, 35, 16);
		Bugemon B = new Bugemon(Type.AQUA, s.hp, s.attack, s.defense, s.initiative);
		int levelGained = B.gainXp(51);
		B.gainLevelsReward(levelGained);
		assertEquals(10, this.getGainedPoint(s, B.getBaseStats()));
	}

	@Test
	public void rewardMultiLevelGained(){
		Stats s = new Stats(10,29, 35, 16);
		Bugemon B = new Bugemon(Type.AQUA, s.hp, s.attack, s.defense, s.initiative);
		int levelGained = B.gainXp(300);
		B.gainLevelsReward(levelGained);
		assertEquals(30, this.getGainedPoint(s, B.getBaseStats()));
	}

	@Test
	public void testResetsFightStats(){
		Stats s = new Stats(-10,-9, -5, -6); // debuff stats
		Bugemon B = new Bugemon(Type.AQUA, 100, 20, 10, 10);

		B.changeFightStats(s);

		assertEquals(90, B.getFightStats().hp);
		assertEquals(11, B.getFightStats().attack);
		assertEquals(5, B.getFightStats().defense);
		assertEquals(4, B.getFightStats().initiative);

		B.removeStatsDebuffs();

		assertEquals(B.getBaseStats().hp, B.getFightStats().hp);
		assertEquals(B.getBaseStats().attack, B.getFightStats().attack);
		assertEquals(B.getBaseStats().defense, B.getFightStats().defense);
		assertEquals(B.getBaseStats().initiative, B.getFightStats().initiative);
	}
}
