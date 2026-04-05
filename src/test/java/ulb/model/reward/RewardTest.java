package ulb.model.reward;

import org.junit.jupiter.api.Test;

import ulb.model.ability.AbilitySet;
import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.BugemonSpecies;
import ulb.model.bugemon.Stats;
import ulb.model.type.Type;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RewardTest {

    private Bugemon createBugemon(String name, Type type, int hp, int attack, int defense, int initiative){
		BugemonSpecies species = new BugemonSpecies(name, name, type, new Stats(hp, attack, defense, initiative), new AbilitySet(), "", false);
		return new Bugemon(species);
	}

    private int getGainedPoint(Stats previous, Stats actual){
		Stats difference = new Stats(actual);
		Stats opposite = new Stats(-previous.getHp(), -previous.getAttack(), -previous.getDefense(), -previous.getInitiative());
		difference.change(opposite);
		return difference.getHp() / 2 + difference.getInitiative() / 2 + difference.getAttack() + difference.getDefense();
	}

    @Test
    public void checkGainsHPReward() {
        Bugemon bugemon = createBugemon("A", Type.AQUA, 10, 29, 35, 16);
        Reward reward = new Reward(bugemon);
        bugemon.gainXp(50);
        reward.configureReward(RewardType.HP);
        reward.applyReward();
        assertEquals(30, bugemon.getBaseStats().getHp());
    }

    @Test
    public void checkGainsAttackReward() {
        Bugemon bugemon = createBugemon("A", Type.AQUA, 10, 29, 35, 16);
        Reward reward = new Reward(bugemon);
        bugemon.gainXp(50);
        reward.configureReward(RewardType.ATTACK);
        reward.applyReward();
        assertEquals(39, bugemon.getBaseStats().getAttack());
    }

    @Test
    public void checkGainsDefenseReward() {
        Bugemon bugemon = createBugemon("A", Type.AQUA, 10, 29, 35, 16);
        Reward reward = new Reward(bugemon);
        bugemon.gainXp(50);
        reward.configureReward(RewardType.DEFENSE);
        reward.applyReward();
        assertEquals(45, bugemon.getBaseStats().getDefense());
    }

    @Test
    public void checkGainsInitiativeReward() {
        Bugemon bugemon = createBugemon("A", Type.AQUA, 10, 29, 35, 16);
        Reward reward = new Reward(bugemon);
        bugemon.gainXp(50);
        reward.configureReward(RewardType.INITIATIVE);
        reward.applyReward();
        assertEquals(36, bugemon.getBaseStats().getInitiative());
    }

    @Test
    public void checkGainsCombinationReward(){
        Bugemon bugemon = createBugemon("A", Type.AQUA, 10, 29, 35, 16);
        Reward reward = new Reward(bugemon);
        bugemon.gainXp(50);
        reward.configureReward(RewardType.COMBINATION);
        Stats previousBugemonStats = new Stats(bugemon.getBaseStats());
        reward.applyReward();
        assertEquals(10, getGainedPoint(previousBugemonStats, bugemon.getBaseStats()));
    }
}
